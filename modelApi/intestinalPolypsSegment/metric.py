import numpy as np
import os
from PIL import Image
import os
import torch
import torch.nn as nn
import torch.nn.functional as F
import torchvision.transforms as transforms
from PIL import Image
import numpy as np


class test_dataset:
    def __init__(self, image_root, gt_root):
        self.images = [image_root + f for f in os.listdir(image_root) if f.endswith('.jpg') or f.endswith('.png')]
        self.gts = [gt_root + f for f in os.listdir(gt_root) if f.endswith('.jpg') or f.endswith('.png')]
        self.images = sorted(self.images)
        self.gts = sorted(self.gts)
        self.transform = transforms.ToTensor()
        self.gt_transform = transforms.ToTensor()
        self.size = len(self.images)
        self.index = 0

    def load_data(self):
        image = self.rgb_loader(self.images[self.index])
        image = self.transform(image).unsqueeze(0)
        gt = self.binary_loader(self.gts[self.index])
        name = self.images[self.index].split('/')[-1]
        if name.endswith('.jpg'):
            name = name.split('.jpg')[0] + '.png'
        self.index += 1
        return image, gt, name

    def rgb_loader(self, path):
        with open(path, 'rb') as f:
            img = Image.open(f)
            return img.convert('RGB')

    def binary_loader(self, path):
        with open(path, 'rb') as f:
            img = Image.open(f)
            return img.convert('L')



def processData(image, gt, name):
    gt = np.asarray(gt, np.float32)  # 0 -255
    gt /= (gt.max() + 1e-8) # 0-1
    image = image
    input = image[0, 1, :, :]
    input = np.array(input)
    # input 500,574
    target = np.array(gt)
    N = gt.shape
    input_flat = np.reshape(input, (-1))
    target_flat = np.reshape(target, (-1))

    return input_flat, target_flat




def metricScore(image_root, gt_root):
    # arr1 = np.arange(0,4)
    # arr2 = np.array([1,2,3,4])
    # print(arr1 * arr2)
    # print(arr2)
    test_loader = test_dataset(image_root, gt_root)
    # accuracySum ,recallSum, iouSum, diceSum = 0.0, 0.0, 0.0, 0.0
    accuracySum, recallSum, specSum, precSum, diceSum, jaccSum = 0.0, 0.0, 0.0, 0.0, 0.0, 0.0
    diceSum = 0.0
    iouSum = 0.0
    # accuracy , sensitivity(recall), specificity(TN/(TN+FP)),precision(TP/(TP+FP)),dice(2*(PC*SE)/(PC+SE)),jaccard index(GT N SR/GR U SR)
    for i in range(test_loader.size):
        image, gt, name = test_loader.load_data()
        input_flat, target_flat = processData(image, gt, name)
        smooth = 1e-8

        TP = (input_flat * target_flat).sum() # TP overlap
        union = input_flat.sum() + target_flat.sum() - TP  # TP + FP + FN
        FP = input_flat.sum() - TP  # FP
        FN = target_flat.sum() - TP # FN
        TN = target_flat.shape[0] - union # TN

        single_I = np.sum(input_flat * target_flat)
        single_U = np.sum(input_flat) + np.sum(target_flat) - single_I

        accuracy = (TP + TN + smooth) / (TP + TN + FP + FN + smooth) # acc
        recall = (TP + smooth) / (TP + FN + smooth) # sensitivity
        specificity = (TN + smooth) / (TN + FP + smooth)
        precision = (TP + smooth) / (TP + FP + smooth)
        dice = (2 * TP + smooth) / (2 * TP + FP + FN  + smooth)
        jaccard = (single_I + smooth) / (single_U + smooth)
        iou = (TP + smooth) / (union + smooth)



        accuracySum = accuracySum + accuracy
        recallSum = recallSum + recall
        specSum = specSum + specificity
        precSum = precSum + precision
        diceSum = diceSum + dice
        jaccSum = jaccSum + jaccard
        iouSum = iouSum + iou


        # print( i, a)
    # f.write(_data_name + ": " + str(b / test_loader.size) + "\n")
    # print('====' * 10 + _data_name + "===" * 10)

    AC, SE, SP, PC, F1, JS = accuracySum / test_loader.size, recallSum / test_loader.size, \
                             specSum / test_loader.size, precSum / test_loader.size, diceSum / test_loader.size, jaccSum / test_loader.size

    print('Accuracy:', accuracySum / test_loader.size)
    print('Recall(sensitivity):', recallSum / test_loader.size)

    print('Dice:', diceSum / test_loader.size)
    print('IoU:', iouSum / test_loader.size)

    print('AC:', AC)
    print('SE:', SE)
    print('SP:', SP)
    print('PC:', PC)
    print('F1:', F1)
    print('JS:', JS)





if __name__ == '__main__':

    import os


    image_root = '/media/wx/HDD/TANet_Result/firstSAM_afterDA_no_ra_bs32_2017/PraNet-12/'
    # image_root = '/home/wx/datasets/TestDataset/Kvasir/images/'
    gt_root = '/media/wx/HDD/datasets/ISIC2017/test/masks/'
    # gt_root = '/home/wx/datasets/TestDataset/Kvasir/masks/'
    metricScore(image_root,gt_root)




