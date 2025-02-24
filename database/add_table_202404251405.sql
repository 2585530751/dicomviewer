ALTER TABLE `image_db`.`patient` 
ADD COLUMN `patient_id_dcm` VARCHAR(64) NULL AFTER `user_id`;

ALTER TABLE `image_db`.`image` 
ADD COLUMN `series_number` VARCHAR(64) NULL COMMENT '序列id' AFTER `is_deleted`,
ADD COLUMN `series_instance_uid` VARCHAR(64) NULL COMMENT '序列实例id' AFTER `series_id`,
ADD COLUMN `modality` VARCHAR(45) NULL COMMENT '检查模态' AFTER `series_instance_uid`,
ADD COLUMN `series_desc` VARCHAR(255) NULL COMMENT '描述' AFTER `modality`,
ADD COLUMN `series_date` VARCHAR(45) NULL COMMENT '日期' AFTER `series_desc`,
ADD COLUMN `series_time` VARCHAR(45) NULL COMMENT '时间' AFTER `series_date`,
ADD COLUMN `image_position` VARCHAR(45) NULL COMMENT '图像位置' AFTER `series_time`,
ADD COLUMN `spacing_between_slices` VARCHAR(45) NULL COMMENT '序列中相邻切片之间的间距' AFTER `image_position`,
ADD COLUMN `mr_acquisition_type` VARCHAR(45) NULL COMMENT '磁共振(MR)图像的采集类型' AFTER `spacing_between_slices`,
ADD COLUMN `study_id` INT NULL COMMENT '关联的检查id' AFTER `mr_acquisition_type`;

ALTER TABLE `image_db`.`single_image` 
ADD COLUMN `single_image_position` VARCHAR(45) NULL AFTER `is_deleted`,
ADD COLUMN `single_image_orientation` VARCHAR(45) NULL AFTER `single_image_position`,
ADD COLUMN `slice_thickness` VARCHAR(45) NULL AFTER `single_image_orientation`,
ADD COLUMN `slice_location` VARCHAR(45) NULL AFTER `slice_thickness`,
ADD COLUMN `single_image_rows` VARCHAR(45) NULL COMMENT '图像行分辨率' AFTER `slice_location`,
ADD COLUMN `single_image_columns` VARCHAR(45) NULL COMMENT '图像列分辨率' AFTER `single_image_rows`,
ADD COLUMN `pixel_spacing` VARCHAR(45) NULL COMMENT '像素间距' AFTER `single_image_columns`,
ADD COLUMN `bits_allocated` VARCHAR(45) NULL COMMENT '分配的位数' AFTER `pixel_spacing`,
ADD COLUMN `bits_stored` VARCHAR(45) NULL COMMENT '存储的位数' AFTER `bits_allocated`,
ADD COLUMN `high_bit` VARCHAR(45) NULL COMMENT '高位' AFTER `bits_stored`,
ADD COLUMN `pixel_representation` VARCHAR(45) NULL COMMENT '像素数据的表现类型' AFTER `high_bit`,
ADD COLUMN `window_center` VARCHAR(45) NULL COMMENT '窗位' AFTER `pixel_representation`,
ADD COLUMN `window_width` VARCHAR(45) NULL COMMENT '窗宽' AFTER `window_center`,
ADD COLUMN `rescale_intercept` VARCHAR(45) NULL COMMENT '截距' AFTER `window_width`,
ADD COLUMN `rescale_slope` VARCHAR(45) NULL COMMENT '斜率' AFTER `rescale_intercept`,
ADD COLUMN `rescale_type` VARCHAR(64) NULL COMMENT '输出值的单位' AFTER `rescale_slope`,
ADD COLUMN `single_image_type` VARCHAR(45) NULL AFTER `rescale_type`,
ADD COLUMN `sop_instance_uid` VARCHAR(64) NULL COMMENT 'sop实例uid' AFTER `single_image_type`,
ADD COLUMN `content_date` VARCHAR(45) NULL COMMENT '影像的拍摄日期' AFTER `sop_instance_uid`,
ADD COLUMN `content_time` VARCHAR(45) NULL COMMENT '影像的拍摄时间' AFTER `content_date`,
ADD COLUMN `single_image_number` VARCHAR(45) NULL COMMENT '图像码' AFTER `content_time`,
ADD COLUMN `samples_per_pixel` VARCHAR(45) NULL COMMENT '图像上的采样率' AFTER `single_image_number`,
ADD COLUMN `photometric_interpretation` VARCHAR(45) NULL COMMENT '光度计的解释' AFTER `samples_per_pixel`;

CREATE TABLE `image_db`.`patient_study` (
  `study_id` int(11) NOT NULL AUTO_INCREMENT,
  `study_id_dcm` varchar(64) DEFAULT NULL COMMENT 'dicom文件中的检查id',
  `study_instance_uid` varchar(64) DEFAULT NULL COMMENT '检查实例号',
  `study_date` varchar(45) DEFAULT NULL COMMENT '检查日期',
  `study_time` varchar(45) DEFAULT NULL COMMENT '检查时间',
  `accession_number` varchar(45) DEFAULT NULL COMMENT '检查号，标识做检查的次序',
  `modalities_in_study` varchar(45) DEFAULT NULL COMMENT '一个检查中含有的不同检查类型',
  `body_part_examined` varchar(45) DEFAULT NULL COMMENT '检查的部位',
  `study_description` varchar(64) DEFAULT NULL COMMENT '检查的描述',
  `patient_age` varchar(45) DEFAULT NULL COMMENT '患者做检查时的年龄',
  `study_status` varchar(45) DEFAULT NULL COMMENT '检查状态',
  `patient_id` int(11) DEFAULT NULL COMMENT '关联的病人id',
  PRIMARY KEY (`study_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='患者检查表';

