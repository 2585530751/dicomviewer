ALTER TABLE `image_db`.`patient` 
CHANGE COLUMN `gender` `patient_gender` VARCHAR(2) NULL DEFAULT NULL COMMENT '性别，男或女' ,
CHANGE COLUMN `height` `patient_height` FLOAT NULL DEFAULT NULL COMMENT '身高，单位：厘米cm' ,
CHANGE COLUMN `weight` `patient_weight` FLOAT NULL DEFAULT NULL COMMENT '体重，单位：千克kg' ;
