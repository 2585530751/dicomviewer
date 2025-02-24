CREATE TABLE `image_db`.`image_doctor_relation` (
  `idr_id` INT NOT NULL AUTO_INCREMENT,
  `image_id` INT NULL COMMENT '图像集id',
  `user_id` INT NULL COMMENT '医生id',
  `status` INT NULL COMMENT '状态',
  PRIMARY KEY (`idr_id`))
ENGINE = InnoDB
COMMENT = '图像和医生关联表';