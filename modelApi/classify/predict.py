import os
import torch
from classify.model.dense_ca import DenseNet
from torchvision import transforms
from PIL import Image
from torch.autograd import Variable

test_transform = transforms.Compose([
        transforms.Grayscale(3),
        transforms.Resize((224, 224)),
        # transforms.CenterCrop(224),
        transforms.ToTensor(),
        transforms.Normalize([0.228, 0.228, 0.228],
                             [0.215, 0.215, 0.215])
    ])

model = None
device = None

def modelInit():
    global model
    global device
    if model is None:
        device = torch.device("cuda" if torch.cuda.is_available() else "cpu")

        current_dir = os.path.dirname(os.path.abspath(__file__))
        weight_path = current_dir + os.sep + 'weight' + os.sep + 'densenet169_se.pth'  # 训练好的权重参数路径

        model = DenseNet()
        model = model.to(device)

        if os.path.exists(weight_path):  # 加载权重
            # 无gpu用这个
            model.load_state_dict(torch.load(weight_path, map_location=torch.device('cpu')))
            # 有gpu用这个
            # model.load_state_dict(torch.load(weight_path))
            print('successful load weight！')
        else:
            print('not successful load weight')
        # 测试阶段
        model.eval()


def classify(pic_path, out_path):
    modelInit()
    resMap = {}
    with torch.no_grad():
        for file_name in os.listdir(pic_path):
            print(file_name)
            front, ext = os.path.splitext(file_name)
            image = Image.open(os.path.join(pic_path, file_name))  # 图像文件全路径
            image = test_transform(image)
            image.unsqueeze_(0)
            image = Variable(image).to(device)
            output = model(image)  # 输入模型，执行前向预测
            _, predict = torch.max(output.data, 1)
            print(predict.data.item())  # 0代表良性，1代表恶性
            resMap[file_name] = predict.data.item()
    return resMap
