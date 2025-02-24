import torch
from tqdm import tqdm
from segment.model.unet import UNet
import os
import cv2
import numpy as np

net = None
device = None


def modelInit():
    global net
    global device
    if net is None:
        net = UNet(1, 1)  # 加载网络模型
        device = torch.device('cuda' if torch.cuda.is_available() else "cpu")
        net = net.to(device)
        current_dir = os.path.dirname(os.path.abspath(__file__))
        weights = current_dir + os.sep + 'weight' + os.sep + 'unet.pth'  # 训练好的权重参数路径
        if os.path.exists(weights):  # 加载训练好的权重参数
            # 无gpu用这个
            net.load_state_dict(torch.load(weights, map_location=torch.device('cpu')))
            # 有gpu用这个
            # net.load_state_dict(torch.load(weights))
            print('successfully')
        else:
            print('no loading')
        net.eval()


def segment(in_path, out_path, mask_path):
    modelInit()

    img_names = os.listdir(in_path)  # 需要预测的图片目录
    for image_name in tqdm(img_names):
        image_path = os.path.join(in_path, image_name)

        img = cv2.imread(image_path)
        origin_img = img
        origin_shape = img.shape

        # 转为灰度图
        img = cv2.cvtColor(img, cv2.COLOR_RGB2GRAY)
        img = cv2.resize(img, (224, 224))
        # 转为batch为1，通道为1，大小为512*512的数组
        img = img.reshape(1, 1, img.shape[0], img.shape[1])
        # 转为tensor
        img_tensor = torch.from_numpy(img)
        # 将tensor拷贝到device中，只用cpu就是拷贝到cpu中，用cuda就是拷贝到cuda中。
        img_tensor = img_tensor.to(device=device, dtype=torch.float32)
        # 预测
        pred = net(img_tensor)
        # 提取结果
        pred = np.array(pred.data.cpu()[0])[0]
        pred[pred >= 0.5] = 255
        pred[pred < 0.5] = 0
        pred = cv2.resize(pred, (origin_shape[1], origin_shape[0]), interpolation=cv2.INTER_NEAREST)

        # 保存mask图片
        cv2.imwrite(os.path.join(mask_path, image_name), pred)
        # 保存结果图片
        union_image_mask(out_path, origin_img, pred, image_name)
    return '文件分割成功！'


def union_image_mask(out_path, image, pred, image_name):
    h, w = pred.shape
    mask = np.ones((h, w), dtype='uint8') * 0
    mask[pred[:, :] == 255] = 255
    ret, thresh = cv2.threshold(mask, 127, 255, 0)
    contours, hierarchy = cv2.findContours(thresh, cv2.RETR_TREE, cv2.CHAIN_APPROX_SIMPLE)
    cv2.drawContours(image, contours, -1, (0, 255, 0), 1)
    cv2.imwrite(os.path.join(out_path, image_name), image)
