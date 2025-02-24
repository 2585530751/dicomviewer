ALTER TABLE `image_db`.`patient_study` 
ADD COLUMN `department_id` INT NULL COMMENT '科室id' AFTER `patient_id`,
ADD COLUMN `department_name` VARCHAR(45) NULL COMMENT '科室名' AFTER `department_id`,
ADD COLUMN `department_desc` VARCHAR(45) NULL COMMENT '科室名称' AFTER `department_name`;
