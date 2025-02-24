import torch
import torch.nn.functional as F
import numpy as np
import os, argparse
# from scipy import misc
from imageio import imwrite

from intestinalPolypsSegment.lib.PraNet_Res2Net import PraNet
from intestinalPolypsSegment.utils.dataloader import test_dataset
from PIL import Image
import cv2

parser = argparse.ArgumentParser()
parser.add_argument('--testsize', type=int, default=352, help='testing size')
parser.add_argument('--pth_path', type=str, default='./snapshots/PraNet_Res2Net/PraNet-19.pth')

net = None
device = None
def modelInit():
    global net
    global device
    if net is None:
        net = PraNet()  # 加载网络模型
        device = torch.device('cuda' if torch.cuda.is_available() else "cpu")
        net = net.to(device)
        current_dir = os.path.dirname(os.path.abspath(__file__))
        weights = current_dir + os.sep + 'weight' + os.sep + 'PraNet-19.pth'  # 训练好的权重参数路径
        if os.path.exists(weights):  # 加载训练好的权重参数
            # 无gpu用这个
            # net.load_state_dict(torch.load(weights, map_location=torch.device('cpu')))
            # 有gpu用这个
            net.load_state_dict(torch.load(weights))
            print('successfully')
        else:
            print('no loading')
        net.eval()

def mergeTwoImages(original_image_path, mask_image_path,output_image_path):
    # 加载原图（B路径）和mask图（A路径）
    # 假设B.png是原图，且为RGBA格式
    # 假设A.png是分割后输出的mask图
    # 合成后图像的保存路径

    original_image = Image.open(original_image_path)
    mask_image = Image.open(mask_image_path)

    # 确保mask图和原图尺寸一致
    assert original_image.size == mask_image.size, "Images must be the same size"

    # 将PIL图像转换为numpy数组
    original_array = np.array(original_image)
    mask_array = np.array(mask_image)

    # 检查原图是否是RGBA格式，并适应性地处理mask图像
    if original_array.shape[2] == 4:  # RGBA格式
        # 如果mask不是单通道，则转换为灰度图
        if len(mask_array.shape) > 2:
            mask_array = mask_array[:, :, 3]  # 假设mask图的Alpha通道包含了分割信息
        # 扩展mask维度以匹配RGBA通道
        mask_rgba = np.stack([mask_array] * 4, axis=2)
    else:  # RGB格式
        # 如果原图是RGB且mask不是单通道，转换为灰度
        if len(mask_array.shape) > 2:
            mask_array = np.mean(mask_array, axis=2)
            # 转换为8位整数
        mask_array = (mask_array * 255).astype(np.uint8)
        # 扩展mask维度以匹配RGB通道
        mask_rgba = np.stack([mask_array] * 3, axis=2)

        # 设置透明度
    alpha = 0.5

    # 叠加图像
    overlaid_rgba = np.array(original_image) * (1 - alpha) + mask_rgba * alpha

    # 裁剪数值以确保它们在有效范围内
    overlaid_rgba = np.clip(overlaid_rgba, 0, 255).astype(np.uint8)

    # 转换回PIL图像
    overlaid_image = Image.fromarray(overlaid_rgba)

    # 保存叠加后的图像到输出路径
    overlaid_image.save(output_image_path)

    print(f"The overlaid image has been saved to {output_image_path}")

def mergeTwoImages2(original_image_path, mask_image_path,output_image_path):
    # 加载原图
    original_image = cv2.imread(original_image_path)

    # 加载mask图像
    mask = cv2.imread(mask_image_path, 0)  # 以灰度模式读取mask

    # 创建一个与原图像相同大小的彩色空白图像，用于构造半透明的分割区域
    semi_transparent_overlay = np.zeros_like(original_image)

    # 设置分割区域的透明度和颜色
    # 例如，我们设置为半透明的红色，你可以根据需要调整颜色和透明度
    transparency = 0.5
    color = [0, 0, 255]  # 红色，BGR格式

    # 在叠加图像上应用mask，并将选定的颜色与透明度应用到对应区域
    semi_transparent_overlay[mask != 0] = [int(color[0] * transparency), int(color[1] * transparency),
                                           int(color[2] * transparency)]

    # 只在mask区域叠加图像，保持原图其他区域不变
    output_image = np.copy(original_image)  # 复制原图，以免修改原图数据
    output_image[mask != 0] = cv2.addWeighted(output_image[mask != 0], 1 - transparency,
                                              semi_transparent_overlay[mask != 0], transparency, 0)

    # 保存或显示叠加后的图像
    cv2.imwrite(output_image_path, output_image)
    # 或者显示图像
    # cv2.imshow('Overlayed Image', output_image)
    # cv2.waitKey(0)
    # cv2.destroyAllWindows()


def intestinalPolypsSegmentApi(pic_path, out_path, mask_path):
    modelInit()
    test_loader = test_dataset(pic_path + os.sep, 352)

    for i in range(test_loader.size):
        image, gt, name = test_loader.load_data()
        gt = np.asarray(gt, np.float32)
        gt /= (gt.max() + 1e-8)
        image = image.cuda()

        res5, res4, res3, res2 = net(image)
        res = res2
        res = F.upsample(res, size=gt.shape, mode='bilinear', align_corners=False)
        res = res.sigmoid().data.cpu().numpy().squeeze()
        res = (res - res.min()) / (res.max() - res.min() + 1e-8)
        res = res * 255
        # misc.imsave(save_path+name, res)
        res = res.astype(np.uint8)
        full_path = os.path.join(out_path, name)
        print(full_path)
        imwrite(os.path.join(mask_path, name), res)

        if is_grayscale(os.path.join(mask_path, name)):
            print("这是灰度图")
        else:
            mergeTwoImages2(pic_path + os.sep + name, os.path.join(mask_path, name), full_path)


def is_grayscale(img_path):
    try:
        # 尝试读取图像
        img = cv2.imread(img_path)

        # 检查图像是否成功读取
        if img is None:
            print(f"无法读取文件: {img_path}")
            return False

            # 检查通道数
        return len(img.shape) == 2 or (len(img.shape) == 3 and img.shape[2] == 1)
    except Exception as e:
        # 捕获并打印任何异常
        print(f"发生错误: {e}")
        return False

