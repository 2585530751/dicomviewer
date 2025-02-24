CREATE TABLE `image_db`.`model_intro` (
  `intro_id` INT NOT NULL AUTO_INCREMENT,
  `model_name` VARCHAR(50) NULL COMMENT '模型名',
  `description` VARCHAR(255) NULL COMMENT '模型介绍',
  `pic_path` VARCHAR(100) NULL COMMENT '介绍图片路径',
  PRIMARY KEY (`intro_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8
COMMENT = '模型介绍表';
