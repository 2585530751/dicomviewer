ALTER TABLE `image_db`.`single_image` 
CHANGE COLUMN `single_image_equipment` `single_image_equipment` VARCHAR(50) NULL DEFAULT NULL COMMENT '成像设备' ,
CHANGE COLUMN `single_image_format` `single_image_format` VARCHAR(50) NULL DEFAULT NULL COMMENT '文件格式' ,
CHANGE COLUMN `single_image_check_part` `single_image_check_part` VARCHAR(50) NULL DEFAULT NULL COMMENT '检查部位' ;
