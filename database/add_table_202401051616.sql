CREATE TABLE `image_db`.`single_image` (
  `single_image_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图像id',
  `single_image_name` varchar(50) DEFAULT NULL COMMENT '图像文件完整名称',
  `single_image_path` varchar(255) DEFAULT NULL COMMENT '图像文件保存路径包括文件名',
  `type` int(11) DEFAULT NULL COMMENT '图像文件类型',
  `image_id` int(11) DEFAULT NULL COMMENT '关联的图像集合id',
  `single_image_id_rel` int(11) DEFAULT NULL COMMENT '关联的图像文件id',
  `operate_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operate_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `operate_time` date DEFAULT NULL COMMENT '操作时间',
  `status` int(11) DEFAULT NULL COMMENT '状态',
  `single_image_equipment` int(11) DEFAULT NULL COMMENT '成像设备',
  `single_image_format` int(11) DEFAULT NULL COMMENT '文件格式',
  `single_image_desc` varchar(255) DEFAULT NULL COMMENT '文件描述',
  `single_image_check_part` int(11) DEFAULT NULL COMMENT '检查部位',
  `single_image_check_time` date DEFAULT NULL COMMENT '成像时间',
  `patient_id` int(11) DEFAULT NULL COMMENT '病人id',
  `patient_name` varchar(50) DEFAULT NULL COMMENT '病人名称',
  `is_deleted` int(11) DEFAULT NULL COMMENT '是否删除',
  PRIMARY KEY (`single_image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='保存一张图像文件的信息，可以是原始图像或经过操作后的图像'