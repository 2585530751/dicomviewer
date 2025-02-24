import sys

from flask import Flask, request, Response, jsonify
from flask import send_file
import json
from werkzeug.utils import secure_filename
import os
import datetime
from segment.predict import segment
from classify.predict import classify
from lungdetect.detect import lungDetect
from intestinalPolypsSegment.predict import intestinalPolypsSegmentApi
import zipfile
import tempfile
from pylab import *
import dcm_tool
import shutil

app = Flask('modelApi')
app.config['UPLOAD_FOLDER'] = 'uploads'
app.config['ALLOWED_EXTENSIONS'] = {'png', 'jpg', 'jpeg', 'dcm'}
app.config['MAX_CONTENT_LENGTH'] = 1000 * 1024 * 1024  # 100MB
app.config['PROPAGATE_EXCEPTIONS'] = True
app.config['PERMANENT_SESSION_LIFETIME'] = datetime.timedelta(minutes=5)


# 判断文件名是否是我们支持的格式
def allowed_file(filename):
    return '.' in filename and \
        filename.rsplit('.', 1)[1] in app.config['ALLOWED_EXTENSIONS']


# 接收json和返回json数据
@app.route('/model/first', methods=['POST'])
def modelFirst():
    print(request.headers)
    print(request.json)
    res = {'res': 'json result'}
    return Response(json.dumps(res), mimetype='application/json')


def zip_dir(source_dir, output_zip):
    shutil.make_archive(output_zip, 'zip', source_dir)


def del_files(dir_path):
    if os.path.isfile(dir_path):
        try:
            os.remove(dir_path)  # 这个可以删除单个文件，不能删除文件夹
        except BaseException as e:
            print(e)
    elif os.path.isdir(dir_path):
        file_lis = os.listdir(dir_path)
        for file_name in file_lis:
            tf = os.path.join(dir_path, file_name)
            del_files(tf)
        # 删除空文件夹
        os.rmdir(dir_path)


# 分割模型
@app.route('/model/segment', methods=['POST'])
def modelSegment():
    upload_files = request.files.getlist("files")
    # 创建一个临时文件目录名
    now = datetime.datetime.now()
    now_str = now.strftime('%Y%m%d%H%M%S')
    # 保存文件到临时目录
    root_dir = os.path.join(app.root_path, app.config['UPLOAD_FOLDER'], now_str)
    in_path = os.path.join(root_dir, 'input')
    out_path = os.path.join(root_dir, 'output')
    out_dcm_path = os.path.join(root_dir, 'output_dcm')
    if not os.path.exists(app.root_path + os.sep + app.config['UPLOAD_FOLDER']):
        os.mkdir(app.root_path + os.sep + app.config['UPLOAD_FOLDER'])
    if not os.path.exists(root_dir):
        os.mkdir(root_dir)
    if not os.path.exists(in_path):
        os.mkdir(in_path)
    dcm_path = os.path.join(in_path, 'dcm')
    if not os.path.exists(dcm_path):
        os.mkdir(dcm_path)
    pic_path = os.path.join(in_path, 'pic')
    if not os.path.exists(pic_path):
        os.mkdir(pic_path)
    mask_path = os.path.join(in_path, 'mask')
    if not os.path.exists(mask_path):
        os.mkdir(mask_path)
    if not os.path.exists(out_path):
        os.mkdir(out_path)
    if not os.path.exists(out_dcm_path):
        os.mkdir(out_dcm_path)
    isDcm = True
    for file in upload_files:
        if file and allowed_file(file.filename):
            file_name = secure_filename(file.filename)
            pre_name, ext_name = os.path.splitext(file_name)
            if ext_name == '.jpg' or ext_name == '.png':
                file.save(os.path.join(pic_path, file_name))
                isDcm = False
            else:
                file.save(os.path.join(dcm_path, file_name))
                print('save dcm')
        else:
            break
    # 将dicom文件转为jpg，结果保存到input中的pic文件夹
    if isDcm:
        dcm_tool.dcm2jpg(dcm_path, pic_path)
    # 调用模型对保存的图像文件进行分割，结果保存到output目录
    segment(pic_path, out_path, mask_path)

    # 将分割的图像保存为dicom格式
    dcm_tool.jpg2dcm(out_path, dcm_path, out_dcm_path)

    # 对图片进行特征提取，并生成csv文件
    nrrd_path = os.path.join(in_path, 'nrrd')
    if not os.path.exists(nrrd_path):
        os.mkdir(nrrd_path)
    nrrd_mask_path = os.path.join(in_path, 'nrrd_mask')
    if not os.path.exists(nrrd_mask_path):
        os.mkdir(nrrd_mask_path)
    dcm_tool.org2nrrd(dcm_path, mask_path, nrrd_path, nrrd_mask_path)
    dcm_tool.get_feature(nrrd_path, nrrd_mask_path, out_dcm_path)

    # 将分割结果打包到临时目录下
    fd, path = tempfile.mkstemp(suffix='', prefix='output_dcm')
    zip_dir(out_dcm_path, path)
    del_files(root_dir)
    return send_file(path + ".zip")


@app.route('/model/intestinalPolypsSegment', methods=['POST'])
def intestinalPolypsSegmentModel():
    upload_files = request.files.getlist("files")
    # 创建一个临时文件目录名
    now = datetime.datetime.now()
    now_str = now.strftime('%Y%m%d%H%M%S')
    # 保存文件到临时目录
    root_dir = os.path.join(app.root_path, app.config['UPLOAD_FOLDER'], now_str)
    in_path = os.path.join(root_dir, 'input')
    out_path = os.path.join(root_dir, 'output')
    dcm_path = os.path.join(in_path, 'dcm')
    pic_path = os.path.join(in_path, 'pic')
    out_dcm_path = os.path.join(root_dir, 'output_dcm')
    if not os.path.exists(os.path.join(app.root_path, app.config['UPLOAD_FOLDER'])):
        os.mkdir(os.path.join(app.root_path, app.config['UPLOAD_FOLDER']))
    if not os.path.exists(root_dir):
        os.mkdir(root_dir)
    if not os.path.exists(in_path):
        os.mkdir(in_path)
    if not os.path.exists(out_path):
        os.mkdir(out_path)
    if not os.path.exists(dcm_path):
        os.mkdir(dcm_path)
    if not os.path.exists(pic_path):
        os.mkdir(pic_path)
    if not os.path.exists(out_dcm_path):
        os.mkdir(out_dcm_path)
    mask_path = os.path.join(in_path, 'mask')
    if not os.path.exists(mask_path):
        os.mkdir(mask_path)
    isDcm = True
    for file in upload_files:
        if file and allowed_file(file.filename):
            file_name = secure_filename(file.filename)
            pre_name, ext_name = os.path.splitext(file_name)
            if ext_name == '.jpg' or ext_name == '.png':
                file.save(os.path.join(pic_path, file_name))
                isDcm = False
            else:
                file.save(os.path.join(dcm_path, file_name))
                print('save dcm')
        else:
            break

    # 将dicom文件转为jpg，结果保存到input中的pic文件夹
    if isDcm:
        dcm_tool.dcm2jpg(dcm_path, pic_path)

    # 调用模型对保存的图像文件进行分割，结果保存到output目录
    intestinalPolypsSegmentApi(pic_path, out_path, mask_path)

    # 将结果图像保存为dicom格式
    dcm_tool.jpg2dcm(out_path, dcm_path, out_dcm_path)

    # 对图片进行特征提取，并生成csv文件
    nrrd_path = os.path.join(in_path, 'nrrd')
    if not os.path.exists(nrrd_path):
        os.mkdir(nrrd_path)
    nrrd_mask_path = os.path.join(in_path, 'nrrd_mask')
    if not os.path.exists(nrrd_mask_path):
        os.mkdir(nrrd_mask_path)
    dcm_tool.org2nrrd(dcm_path, mask_path, nrrd_path, nrrd_mask_path)
    dcm_tool.get_feature(nrrd_path, nrrd_mask_path, out_dcm_path)

    # 将分割结果打包到临时目录下
    fd, path = tempfile.mkstemp(suffix='', prefix='output_dcm')
    zip_dir(out_dcm_path, path)
    del_files(root_dir)
    return send_file(path + ".zip")


# 分类模型
@app.route('/model/classify', methods=['POST'])
def modelClassify():
    upload_files = request.files.getlist("files")
    # 创建一个临时文件目录名
    now = datetime.datetime.now()
    now_str = now.strftime('%Y%m%d%H%M%S')
    # 保存文件到临时目录
    root_dir = os.path.join(app.root_path, app.config['UPLOAD_FOLDER'], now_str)
    in_path = os.path.join(root_dir, 'input')
    out_path = os.path.join(root_dir, 'output')
    dcm_path = os.path.join(in_path, 'dcm')
    pic_path = os.path.join(in_path, 'pic')
    out_dcm_path = os.path.join(root_dir, 'output_dcm')
    if not os.path.exists(os.path.join(app.root_path, app.config['UPLOAD_FOLDER'])):
        os.mkdir(os.path.join(app.root_path, app.config['UPLOAD_FOLDER']))
    if not os.path.exists(root_dir):
        os.mkdir(root_dir)
    if not os.path.exists(in_path):
        os.mkdir(in_path)
    if not os.path.exists(out_path):
        os.mkdir(out_path)
    if not os.path.exists(dcm_path):
        os.mkdir(dcm_path)
    if not os.path.exists(pic_path):
        os.mkdir(pic_path)
    if not os.path.exists(out_dcm_path):
        os.mkdir(out_dcm_path)
    isDcm = True
    for file in upload_files:
        if file and allowed_file(file.filename):
            file_name = secure_filename(file.filename)
            pre_name, ext_name = os.path.splitext(file_name)
            if ext_name == '.jpg' or ext_name == '.png':
                file.save(os.path.join(pic_path, file_name))
                isDcm = False
            else:
                file.save(os.path.join(dcm_path, file_name))
                print('save dcm')
        else:
            break
    # 将dicom文件转为jpg，结果保存到input中的pic文件夹
    if isDcm:
        dcm_tool.dcm2jpg(dcm_path, pic_path)
    # 调用模型对保存的图像文件进行分类，结果返回
    resList = classify(pic_path, out_path)
    del_files(root_dir)
    return jsonify(resList)


# 肺结节检测
@app.route('/model/lungDetect', methods=['POST'])
def modelLungDetect():
    upload_files = request.files.getlist("files")
    # 创建一个临时文件目录名
    now = datetime.datetime.now()
    now_str = now.strftime('%Y%m%d%H%M%S')
    # 保存文件到临时目录
    root_dir = os.path.join(app.root_path, app.config['UPLOAD_FOLDER'], now_str)
    in_path = os.path.join(root_dir, 'input')
    out_path = os.path.join(root_dir, 'output')
    dcm_path = os.path.join(in_path, 'dcm')
    pic_path = os.path.join(in_path, 'pic')
    out_dcm_path = os.path.join(root_dir, 'output_dcm')
    if not os.path.exists(os.path.join(app.root_path, app.config['UPLOAD_FOLDER'])):
        os.mkdir(os.path.join(app.root_path, app.config['UPLOAD_FOLDER']))
    if not os.path.exists(root_dir):
        os.mkdir(root_dir)
    if not os.path.exists(in_path):
        os.mkdir(in_path)
    if not os.path.exists(out_path):
        os.mkdir(out_path)
    if not os.path.exists(dcm_path):
        os.mkdir(dcm_path)
    if not os.path.exists(pic_path):
        os.mkdir(pic_path)
    if not os.path.exists(out_dcm_path):
        os.mkdir(out_dcm_path)
    isDcm = True
    for file in upload_files:
        if file and allowed_file(file.filename):
            file_name = secure_filename(file.filename)
            pre_name, ext_name = os.path.splitext(file_name)
            if ext_name == '.jpg' or ext_name == '.png':
                file.save(os.path.join(pic_path, file_name))
                isDcm = False
            else:
                file.save(os.path.join(dcm_path, file_name))
                print('save dcm')
        else:
            break
    # 将dicom文件转为jpg，结果保存到input中的pic文件夹
    if isDcm:
        dcm_tool.dcm2jpg(dcm_path, pic_path)
    # 调用模型对保存的图像文件进行分割，结果保存到output目录
    lungDetect(pic_path, out_path)

    # 将结果图像保存为dicom格式
    dcm_tool.jpg2dcm(out_path, dcm_path, out_dcm_path)

    # 将分割结果打包到临时目录下
    fd, path = tempfile.mkstemp(suffix='', prefix='output_dcm')
    zip_dir(out_dcm_path, path)
    del_files(root_dir)
    return send_file(path + ".zip")


if __name__ == '__main__':
    # from waitress import serve
    # serve(app, host="0.0.0.0", port=8085)
    app.run(port=8085, debug=True)
