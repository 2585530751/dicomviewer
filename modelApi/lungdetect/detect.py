import os
from PIL import Image
from tqdm import tqdm
from lungdetect.yolo import YOLO

yo = None


def modelInit():
    global yo
    yo = YOLO()


def lungDetect(pic_path, out_path):
    modelInit()
    for img_name in os.listdir(pic_path):
        try:
            image = Image.open(os.path.join(pic_path, img_name))
        except:
            print('Open Error! Try again!')
        else:
            r_image = yo.detect_image(image, False, False)
            r_image.save(os.path.join(out_path, img_name), quality=100, subsampling=0)
