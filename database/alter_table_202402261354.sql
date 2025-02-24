ALTER TABLE `image_db`.`image` 
CHANGE COLUMN `image_equipment` `image_equipment` VARCHAR(50) NULL DEFAULT NULL COMMENT '成像设备，区分是哪种医学设备产生的图像' ,
CHANGE COLUMN `image_format` `image_format` VARCHAR(50) NULL DEFAULT NULL COMMENT '文件格式，如dicom' ,
CHANGE COLUMN `image_check_part` `image_check_part` VARCHAR(50) NULL DEFAULT NULL COMMENT '图像检查的身体部位' ;
