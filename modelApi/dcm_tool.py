import cv2
import numpy as np
import pydicom
import pydicom.uid
from pydicom.dataset import FileMetaDataset, FileDataset
from pylab import *
import PIL.Image as Image

import SimpleITK as sitk
import os
from skimage import io, color
import radiomics.featureextractor as FEE
import pandas as pd
import dicom2jpg

sys_is_little_endian = (sys.byteorder == 'little')

NumpySupportedTransferSyntax = [
    pydicom.uid.ExplicitVRLittleEndian,
    pydicom.uid.ImplicitVRLittleEndian,
    pydicom.uid.DeflatedExplicitVRLittleEndian,
    pydicom.uid.ExplicitVRBigEndian,
]

param_path = r'Params.yaml'


# 加载 Dicom图像
def get_pixel_data(dicom_dataset):
    """If NumPy is available, return an ndarray of the Pixel Data.
    Raises
    ------
    TypeError
        If there is no Pixel Data or not a supported data type.
    NotImplementedError
        if the transfer syntax is not supported
    AttributeError
        if the decoded amount of data does not match the expected amount
    Returns
    -------
    numpy.ndarray
       The contents of the Pixel Data element (7FE0,0010) as an ndarray.
    """
    if (dicom_dataset.file_meta.TransferSyntaxUID not in
            NumpySupportedTransferSyntax):
        raise NotImplementedError("Pixel Data is compressed in a "
                                  "format pydicom does not yet handle. "
                                  "Cannot return array. Pydicom might "
                                  "be able to convert the pixel data "
                                  "using GDCM if it is installed.")

    if 'PixelData' not in dicom_dataset:
        raise TypeError("No pixel data found in this dataset.")

    # Make NumPy format code, e.g. "uint16", "int32" etc
    # from two pieces of info:
    # dicom_dataset.PixelRepresentation -- 0 for unsigned, 1 for signed;
    # dicom_dataset.BitsAllocated -- 8, 16, or 32
    if dicom_dataset.BitsAllocated == 1:
        # single bits are used for representation of binary data
        format_str = 'uint8'
    elif dicom_dataset.PixelRepresentation == 0:
        format_str = 'uint{}'.format(dicom_dataset.BitsAllocated)
    elif dicom_dataset.PixelRepresentation == 1:
        format_str = 'int{}'.format(dicom_dataset.BitsAllocated)
    else:
        format_str = 'bad_pixel_representation'
    try:
        numpy_dtype = np.dtype(format_str)
    except TypeError:
        msg = ("Data type not understood by NumPy: "
               "format='{}', PixelRepresentation={}, "
               "BitsAllocated={}".format(
            format_str,
            dicom_dataset.PixelRepresentation,
            dicom_dataset.BitsAllocated))
        raise TypeError(msg)

    if dicom_dataset.is_little_endian != sys_is_little_endian:
        numpy_dtype = numpy_dtype.newbyteorder('S')

    pixel_bytearray = dicom_dataset.PixelData

    if dicom_dataset.BitsAllocated == 1:
        # if single bits are used for binary representation, a uint8 array
        # has to be converted to a binary-valued array (that is 8 times bigger)
        try:
            pixel_array = np.unpackbits(
                np.frombuffer(pixel_bytearray, dtype='uint8'))
        except NotImplementedError:
            # PyPy2 does not implement numpy.unpackbits
            raise NotImplementedError(
                'Cannot handle BitsAllocated == 1 on this platform')
    else:
        pixel_array = np.frombuffer(pixel_bytearray, dtype=numpy_dtype)
    length_of_pixel_array = pixel_array.nbytes
    expected_length = dicom_dataset.Rows * dicom_dataset.Columns
    if ('NumberOfFrames' in dicom_dataset and
            dicom_dataset.NumberOfFrames > 1):
        expected_length *= dicom_dataset.NumberOfFrames
    if ('SamplesPerPixel' in dicom_dataset and
            dicom_dataset.SamplesPerPixel > 1):
        expected_length *= dicom_dataset.SamplesPerPixel
    if dicom_dataset.BitsAllocated > 8:
        expected_length *= (dicom_dataset.BitsAllocated // 8)
    padded_length = expected_length
    if expected_length & 1:
        padded_length += 1
    if length_of_pixel_array != padded_length:
        raise AttributeError(
            "Amount of pixel data %d does not "
            "match the expected data %d" %
            (length_of_pixel_array, padded_length))
    if expected_length != padded_length:
        pixel_array = pixel_array[:expected_length]
    if dicom_dataset.Modality.lower().find('ct') >= 0:  # CT图像需要得到其CT值图像
        pixel_array = pixel_array * dicom_dataset.RescaleSlope + dicom_dataset.RescaleIntercept  # 获得图像的CT值
    pixel_array = pixel_array.reshape(dicom_dataset.Rows, dicom_dataset.Columns * dicom_dataset.SamplesPerPixel)
    return pixel_array, dicom_dataset.Rows, dicom_dataset.Columns


def dcm2jpg(dcm_path, pic_path):
    for filename in os.listdir(dcm_path):
        if os.path.isfile(os.path.join(dcm_path, filename)):
            img_data = dicom2jpg.dicom2img(os.path.join(dcm_path, filename))
            dcm_img = Image.fromarray(img_data)
            dcm_img = dcm_img.convert('L')

            # 保存为jpg文件
            pre_name, ext_name = os.path.splitext(filename)
            jpg_path = os.path.join(pic_path, pre_name + '.jpg')
            dcm_img.save(jpg_path)


def jpg2dcm(pic_path, template_dcm_path, target_dcm_path):
    for pic_name in os.listdir(pic_path):
        front, ext = os.path.splitext(pic_name)
        dcm_name = front + '.dcm'
        img_path = os.path.join(pic_path, pic_name)
        save_to_dcm(img_path, target_dcm_path, dcm_name)


def save_to_dcm(image_path, dcm_path, dcm_name):
    img = np.asarray(Image.open(image_path), dtype=np.uint8)

    full_path = dcm_path + os.sep + dcm_name
    # Populate required values for file meta information
    file_meta = FileMetaDataset()
    file_meta.MediaStorageSOPClassUID = '1.2.840.10008.5.1.4.1.1.2'
    file_meta.MediaStorageSOPInstanceUID = "1.2.3"
    file_meta.ImplementationClassUID = "1.2.3.4"

    # Create the FileDataset instance (initially no data elements, but file_meta supplied)
    ds = FileDataset(full_path, {}, file_meta=file_meta, preamble=b"\0" * 128)

    # # Write as a different transfer syntax XXX shouldn't need this but pydicom
    # # 0.9.5 bug not recognizing transfer syntax
    ds.file_meta.TransferSyntaxUID = pydicom.uid.ImplicitVRLittleEndian
    # Set creation date/time
    dt = datetime.datetime.now()
    ds.ContentDate = dt.strftime('%Y%m%d')
    timeStr = dt.strftime('%H%M%S.%f')  # long format with micro seconds
    ds.ContentTime = timeStr

    ds.Modality = "US"
    ds.SeriesInstanceUID = pydicom.uid.generate_uid()
    ds.StudyInstanceUID = pydicom.uid.generate_uid()
    ds.FrameOfReferenceUID = pydicom.uid.generate_uid()

    ds.BitsStored = 8
    ds.BitsAllocated = 8
    ds.SamplesPerPixel = 3
    ds.HighBit = 7
    ds.PlanarConfiguration = 0

    ds.InstanceNumber = 1
    ds.ImagePositionPatient = r"0\0\1"
    ds.ImageOrientationPatient = r"1\0\0\0\-1\0"

    ds.ImagesInAcquisition = "1"
    ds.Rows, ds.Columns = img.shape[:2]
    ds.PixelSpacing = [1, 1]
    ds.PixelRepresentation = 0
    ds.PixelData = img.tobytes()
    ds.PhotometricInterpretation = 'RGB'

    ds.save_as(full_path)


def org2nrrd(dcm_path, mask_path, nrrd_path, nrrd_mask_path):
    for file_name in os.listdir(dcm_path):
        front, ext = os.path.splitext(file_name)
        # 读取图像
        dcm_image = io.imread(os.path.join(dcm_path, file_name), as_gray=True)
        mask_image = io.imread(os.path.join(mask_path, front + '.jpg'), as_gray=True)

        # 将灰度图转换为RGB图像（如果原图是彩色图像，则可以跳过此步骤）
        dcm_image_rgb = color.gray2rgb(dcm_image)
        mask_image_rgb = color.gray2rgb(mask_image)

        # 将图像转换为SimpleITK格式
        dcm_image_sitk = sitk.GetImageFromArray(dcm_image_rgb)
        mask_image_sitk = sitk.GetImageFromArray(mask_image_rgb)

        # 保存为NRRD格式
        sitk.WriteImage(dcm_image_sitk, os.path.join(nrrd_path, front + '.nrrd'))
        sitk.WriteImage(mask_image_sitk, os.path.join(nrrd_mask_path, front + '_mask.nrrd'))


def get_feature(img_path, mask_path, target_path):
    # 使用配置文件初始化特征抽取器
    extractor = FEE.RadiomicsFeatureExtractor(param_path)
    df = None
    for file_name in os.listdir(img_path):
        front, ext = os.path.splitext(file_name)

        mask_image = sitk.ReadImage(os.path.join(mask_path, front + '_mask.nrrd'))
        unique_labels = set(sitk.GetArrayFromImage(mask_image).ravel())

        if 1 in unique_labels:
            # 抽取特征
            extractor.disableAllFeatures()
            extractor.enableFeatureClassByName('firstorder')
            result = extractor.execute(os.path.join(img_path, file_name),
                                       os.path.join(mask_path, front + '_mask.nrrd'), label=1)

            # 将结果写入dataframe
            one = pd.DataFrame.from_dict(result, orient='index', columns=[front])
            if df is None:
                df = one
            else:
                df = pd.concat([df, one], axis=1)
    if df is not None:
        df = df.T
        df.index.name = 'file_name'
        feature_path = os.path.join(target_path, 'feature')
        if not os.path.exists(feature_path):
            os.mkdir(feature_path)
        df.drop(columns=df.loc[:, 'diagnostics_Versions_PyRadiomics':'diagnostics_Mask-original_CenterOfMass'],
                inplace=True)
        df.to_csv(os.path.join(feature_path, 'feature.csv'))
