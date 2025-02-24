CREATE DATABASE  IF NOT EXISTS `image_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `image_db`;
-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: localhost    Database: image_db
-- ------------------------------------------------------
-- Server version	5.7.29-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `doctor`
--

DROP TABLE IF EXISTS `doctor`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor` (
  `doctor_id` int(11) NOT NULL AUTO_INCREMENT,
  `doctor_name` varchar(50) DEFAULT NULL COMMENT '名称',
  `phone_number` varchar(20) DEFAULT NULL COMMENT '手机号',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮件',
  `org_id` int(11) DEFAULT NULL COMMENT '组织id',
  `org_name` varchar(50) DEFAULT NULL COMMENT '组织名',
  `introduction` varchar(255) DEFAULT NULL COMMENT '简介',
  `speciality` varchar(100) DEFAULT NULL COMMENT '专长，擅长',
  `level` int(11) DEFAULT NULL COMMENT '职位，级别',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`doctor_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='医生信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor`
--

LOCK TABLES `doctor` WRITE;
/*!40000 ALTER TABLE `doctor` DISABLE KEYS */;
/*!40000 ALTER TABLE `doctor` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `doctor_patient_relation`
--

DROP TABLE IF EXISTS `doctor_patient_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `doctor_patient_relation` (
  `dpr_id` int(11) NOT NULL AUTO_INCREMENT,
  `doctor_id` int(11) DEFAULT NULL,
  `patient_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`dpr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='医生病人关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `doctor_patient_relation`
--

LOCK TABLES `doctor_patient_relation` WRITE;
/*!40000 ALTER TABLE `doctor_patient_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `doctor_patient_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image`
--

DROP TABLE IF EXISTS `image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image` (
  `image_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '图像id',
  `image_name` varchar(50) DEFAULT NULL COMMENT '图像原始名称包含后缀',
  `image_equipment` int(11) DEFAULT NULL COMMENT '成像设备，区分是哪种医学设备产生的图像',
  `image_format` int(11) DEFAULT NULL COMMENT '文件格式，如dicom',
  `image_path` varchar(255) DEFAULT NULL COMMENT '保存图像文件的相对路径',
  `image_desc` varchar(255) DEFAULT NULL COMMENT '图像描述',
  `image_count` int(11) DEFAULT NULL COMMENT '图像的序列数量',
  `image_check_part` int(11) DEFAULT NULL COMMENT '图像检查的身体部位',
  `image_check_time` date DEFAULT NULL COMMENT '图像检查时间，是指图像在医疗设备上形成的时间',
  `patient_id` int(11) DEFAULT NULL COMMENT '图像关联的病人id',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建者id',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建者名称',
  `create_time` date DEFAULT NULL COMMENT '图像在本系统的创建时间',
  `image_status` int(11) DEFAULT NULL COMMENT '图像在本系统中的状态',
  `is_deleted` int(11) DEFAULT NULL COMMENT '图像是否被删除，1已被删除，0未被删除',
  PRIMARY KEY (`image_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='医学影像表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image`
--

LOCK TABLES `image` WRITE;
/*!40000 ALTER TABLE `image` DISABLE KEYS */;
/*!40000 ALTER TABLE `image` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_analysis`
--

DROP TABLE IF EXISTS `image_analysis`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_analysis` (
  `image_ana_id` int(11) NOT NULL AUTO_INCREMENT,
  `image_id` int(11) DEFAULT NULL COMMENT '分析的图像id',
  `creator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `create_time` date DEFAULT NULL COMMENT '操作时间',
  `ana_data` json DEFAULT NULL COMMENT '分析信息',
  PRIMARY KEY (`image_ana_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图像分析信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_analysis`
--

LOCK TABLES `image_analysis` WRITE;
/*!40000 ALTER TABLE `image_analysis` DISABLE KEYS */;
/*!40000 ALTER TABLE `image_analysis` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `image_seg`
--

DROP TABLE IF EXISTS `image_seg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_seg` (
  `image_seg_id` int(11) NOT NULL AUTO_INCREMENT,
  `image_id` int(11) DEFAULT NULL COMMENT '分割的图像id',
  `creator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `create_time` date DEFAULT NULL COMMENT '操作时间',
  `seg_data` json DEFAULT NULL COMMENT '分割信息',
  PRIMARY KEY (`image_seg_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='图像分割信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `image_seg`
--

LOCK TABLES `image_seg` WRITE;
/*!40000 ALTER TABLE `image_seg` DISABLE KEYS */;
/*!40000 ALTER TABLE `image_seg` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `log_operate`
--

DROP TABLE IF EXISTS `log_operate`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `log_operate` (
  `operate_log_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '操作日志id',
  `operate_type` int(11) DEFAULT NULL COMMENT '操作类型',
  `operate_content` varchar(255) DEFAULT NULL COMMENT '操作内容',
  `operator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `operator_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `operate_time` date DEFAULT NULL COMMENT '操作时间',
  `log_type` int(11) DEFAULT NULL COMMENT '日志类别',
  PRIMARY KEY (`operate_log_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='操作日志表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `log_operate`
--

LOCK TABLES `log_operate` WRITE;
/*!40000 ALTER TABLE `log_operate` DISABLE KEYS */;
/*!40000 ALTER TABLE `log_operate` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `medical_report`
--

DROP TABLE IF EXISTS `medical_report`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `medical_report` (
  `report_id` int(11) NOT NULL AUTO_INCREMENT,
  `report_name` varchar(50) DEFAULT NULL COMMENT '报告名',
  `image_id` int(11) DEFAULT NULL COMMENT '关联的图像',
  `creator_id` int(11) DEFAULT NULL COMMENT '操作人id',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '操作人名称',
  `create_time` date DEFAULT NULL COMMENT '操作时间',
  `report_data` json DEFAULT NULL COMMENT '报告内容',
  PRIMARY KEY (`report_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='医学图像报告表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `medical_report`
--

LOCK TABLES `medical_report` WRITE;
/*!40000 ALTER TABLE `medical_report` DISABLE KEYS */;
/*!40000 ALTER TABLE `medical_report` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `organization`
--

DROP TABLE IF EXISTS `organization`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `organization` (
  `org_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '组织id',
  `org_parent_id` int(11) DEFAULT NULL COMMENT '父级组织id',
  `org_parent_name` varchar(50) DEFAULT NULL COMMENT '父级组织名称',
  `org_name` varchar(50) DEFAULT NULL COMMENT '组织名称',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `count` int(11) DEFAULT NULL COMMENT '组织人员数量',
  PRIMARY KEY (`org_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='组织表，记录用户所在的组织，对用户进行分类，不涉及权限的分配，用户可以没有组织，但有且仅有一个组织';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `organization`
--

LOCK TABLES `organization` WRITE;
/*!40000 ALTER TABLE `organization` DISABLE KEYS */;
/*!40000 ALTER TABLE `organization` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patient`
--

DROP TABLE IF EXISTS `patient`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient` (
  `patient_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '唯一id，自增',
  `patient_name` varchar(50) DEFAULT NULL COMMENT '病人名称',
  `patient_id_card_number` varchar(20) DEFAULT NULL COMMENT '病人身份证号',
  `gender` varchar(2) DEFAULT NULL COMMENT '性别，男或女',
  `height` float DEFAULT NULL COMMENT '身高，单位：厘米cm',
  `weight` float DEFAULT NULL COMMENT '体重，单位：千克kg',
  `date_of_birth` date DEFAULT NULL COMMENT '出生日期，年-月-日',
  `phone_number` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `telephone_number` varchar(20) DEFAULT NULL COMMENT '电话号码',
  `fax_number` varchar(20) DEFAULT NULL COMMENT '传真号码',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮件',
  `address` varchar(100) DEFAULT NULL COMMENT '住址',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  PRIMARY KEY (`patient_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 COMMENT='病人基本信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patient`
--

LOCK TABLES `patient` WRITE;
/*!40000 ALTER TABLE `patient` DISABLE KEYS */;
INSERT INTO `patient` VALUES (1,'张三','360721199311016018','男',NULL,NULL,'1993-11-01',NULL,NULL,NULL,NULL,NULL,NULL),(2,'李四','360721199206016017','女',NULL,NULL,'1992-06-01',NULL,NULL,NULL,NULL,NULL,NULL),(3,'王琦','360721199707226016','男',180,70,'1997-07-22','13570678896','','','','',NULL),(4,'周卫国','360721199606226014','男',178,66,'1996-06-22','15872428680','','','','',NULL),(5,'赵四','360721198503206015','女',166,55,'1985-03-20','17285586038','','','','',NULL);
/*!40000 ALTER TABLE `patient` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict`
--

DROP TABLE IF EXISTS `sys_dict`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict` (
  `dict_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典id',
  `dict_name` varchar(50) DEFAULT NULL COMMENT '字典名',
  `dict_code` varchar(20) DEFAULT NULL COMMENT '字典编码',
  `description` varchar(255) DEFAULT NULL COMMENT '字典描述',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人名',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `update_id` int(11) DEFAULT NULL COMMENT '更新人id',
  `update_name` varchar(50) DEFAULT NULL COMMENT '更新人名',
  `update_time` date DEFAULT NULL COMMENT '更新时间',
  `id_deleted` int(11) DEFAULT NULL COMMENT '是否被删除，1已被删除，0未被删除',
  PRIMARY KEY (`dict_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统字典表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict`
--

LOCK TABLES `sys_dict` WRITE;
/*!40000 ALTER TABLE `sys_dict` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dict_item`
--

DROP TABLE IF EXISTS `sys_dict_item`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_item` (
  `dict_item_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '字典项id',
  `dict_id` int(11) DEFAULT NULL COMMENT '字典id',
  `dict_item_text` varchar(50) DEFAULT NULL COMMENT '字典项文本',
  `dict_item_value` int(11) DEFAULT NULL COMMENT '字典项值，和字典id唯一确定',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  `sort_order` int(11) DEFAULT NULL COMMENT '排序',
  `status` int(11) DEFAULT NULL COMMENT '状态，1启用，0不启用',
  `creator_id` int(11) DEFAULT NULL COMMENT '创建人id',
  `creator_name` varchar(50) DEFAULT NULL COMMENT '创建人名',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `update_id` int(11) DEFAULT NULL COMMENT '更新人id',
  `update_name` varchar(50) DEFAULT NULL COMMENT '更新人名',
  `update_time` date DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`dict_item_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统字典项表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_dict_item`
--

LOCK TABLES `sys_dict_item` WRITE;
/*!40000 ALTER TABLE `sys_dict_item` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_dict_item` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_group`
--

DROP TABLE IF EXISTS `sys_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_group` (
  `group_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '分组id',
  `group_parent_id` int(11) DEFAULT NULL COMMENT '父级分组id',
  `group_parent_name` varchar(50) DEFAULT NULL COMMENT '父级分组名称',
  `group_name` varchar(50) DEFAULT NULL COMMENT '分组名称',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`group_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分组表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_group`
--

LOCK TABLES `sys_group` WRITE;
/*!40000 ALTER TABLE `sys_group` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_group` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_group_perm_relation`
--

DROP TABLE IF EXISTS `sys_group_perm_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_group_perm_relation` (
  `gpr_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关系id',
  `group_id` int(11) DEFAULT NULL COMMENT '分组id',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
  `permission_type` int(11) DEFAULT NULL COMMENT '权限类型',
  PRIMARY KEY (`gpr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='分组权限关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_group_perm_relation`
--

LOCK TABLES `sys_group_perm_relation` WRITE;
/*!40000 ALTER TABLE `sys_group_perm_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_group_perm_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_permission`
--

DROP TABLE IF EXISTS `sys_permission`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_permission` (
  `permission_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '权限id',
  `permission_parent_id` int(11) DEFAULT NULL COMMENT '父级权限id',
  `permission_parent_name` varchar(50) DEFAULT NULL COMMENT '父级权限名称',
  `permission_name` varchar(50) DEFAULT NULL COMMENT '权限名称',
  `create_time` date DEFAULT NULL COMMENT '创建时间',
  `description` varchar(255) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='权限表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_permission`
--

LOCK TABLES `sys_permission` WRITE;
/*!40000 ALTER TABLE `sys_permission` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_permission` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user`
--

DROP TABLE IF EXISTS `sys_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `org_id` int(11) DEFAULT NULL COMMENT '组织id',
  `org_name` varchar(50) DEFAULT NULL COMMENT '组织名称',
  `account` varchar(50) DEFAULT NULL COMMENT '账号',
  `password` varchar(100) DEFAULT NULL COMMENT '密码',
  `name` varchar(50) DEFAULT NULL COMMENT '姓名',
  `phone_number` varchar(20) DEFAULT NULL COMMENT '手机号码',
  `email` varchar(50) DEFAULT NULL COMMENT '电子邮件',
  `create_time` date DEFAULT NULL COMMENT '用户创建时间',
  `login_count` int(11) DEFAULT NULL COMMENT '登录次数',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user`
--

LOCK TABLES `sys_user` WRITE;
/*!40000 ALTER TABLE `sys_user` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_group_relation`
--

DROP TABLE IF EXISTS `sys_user_group_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_group_relation` (
  `ugr_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关系id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `group_id` int(11) DEFAULT NULL COMMENT '分组id',
  PRIMARY KEY (`ugr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户分组关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_group_relation`
--

LOCK TABLES `sys_user_group_relation` WRITE;
/*!40000 ALTER TABLE `sys_user_group_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_group_relation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_user_perm_relation`
--

DROP TABLE IF EXISTS `sys_user_perm_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_user_perm_relation` (
  `upr_id` int(11) NOT NULL AUTO_INCREMENT COMMENT '关系id',
  `user_id` int(11) DEFAULT NULL COMMENT '用户id',
  `permission_id` int(11) DEFAULT NULL COMMENT '权限id',
  `permission_type` int(11) DEFAULT NULL COMMENT '权限类型',
  PRIMARY KEY (`upr_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='用户权限关系表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_user_perm_relation`
--

LOCK TABLES `sys_user_perm_relation` WRITE;
/*!40000 ALTER TABLE `sys_user_perm_relation` DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_user_perm_relation` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2023-07-22 15:40:46
