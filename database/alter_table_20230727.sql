ALTER TABLE `image_db`.`sys_dict` 
CHANGE COLUMN `id_deleted` `is_deleted` INT(11) NULL DEFAULT NULL COMMENT '是否被删除，1已被删除，0未被删除' ;
ALTER TABLE `image_db`.`image` 
ADD COLUMN `patient_name` VARCHAR(50) NULL COMMENT '病人名称' AFTER `patient_id`;
