ALTER TABLE `image_db`.`image_seg` 
ADD COLUMN `single_image_id` INT NULL AFTER `image_id`,
ADD COLUMN `model_id` INT NULL COMMENT '模型id' AFTER `res_data`,
CHANGE COLUMN `image_seg_id` `model_res_id` INT(11) NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `seg_data` `res_data` JSON NULL DEFAULT NULL COMMENT '结果信息' , COMMENT = '图像模型结果信息表' , RENAME TO  `image_db`.`model_result` ;

ALTER TABLE `image_db`.`model_intro` 
CHANGE COLUMN `intro_id` `model_id` INT(11) NOT NULL AUTO_INCREMENT ,
CHANGE COLUMN `pic_path` `model_image` VARCHAR(100) NULL DEFAULT NULL COMMENT '介绍图片路径' , RENAME TO  `image_db`.`model` ;
