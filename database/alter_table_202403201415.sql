ALTER TABLE `image_db`.`sys_user` 
ADD COLUMN `user_name` VARCHAR(50) NULL COMMENT '用户名' AFTER `login_count`,
ADD COLUMN `id_card` VARCHAR(20) NULL COMMENT '身份证号' AFTER `user_name`,
ADD COLUMN `birth_of_date` DATE NULL COMMENT '出生日期' AFTER `id_card`,
ADD COLUMN `gender` VARCHAR(5) NULL COMMENT '性别' AFTER `birth_of_date`,
ADD COLUMN `address` VARCHAR(50) NULL COMMENT '地址' AFTER `gender`,
ADD COLUMN `user_height` FLOAT NULL COMMENT '身高' AFTER `address`,
ADD COLUMN `user_weight` FLOAT NULL COMMENT '体重' AFTER `user_height`,
ADD COLUMN `place` VARCHAR(50) NULL COMMENT '位置' AFTER `user_weight`,
ADD COLUMN `head_icon` VARCHAR(100) NULL COMMENT '头像' AFTER `place`;
