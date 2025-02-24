CREATE DATABASE  IF NOT EXISTS `image_db` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `image_db`;
-- MySQL dump 10.13  Distrib 8.0.31, for Win64 (x86_64)
--
-- Host: localhost    Database: image_db
-- ------------------------------------------------------
-- Server version	5.7.40-log

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
-- Table structure for table `image_department_relation`
--

DROP TABLE IF EXISTS `image_department_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `image_department_relation` (
  `idr_id` int(11) NOT NULL AUTO_INCREMENT,
  `image_id` int(11) DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL,
  `department_name` varchar(45) DEFAULT NULL,
  `department_desc` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`idr_id`),
  UNIQUE KEY `idx_image_department_relation_image_id_department_id` (`image_id`,`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='图像和科室关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `patient_department_relation`
--

DROP TABLE IF EXISTS `patient_department_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patient_department_relation` (
  `pdr_id` int(11) NOT NULL AUTO_INCREMENT,
  `patient_id` int(11) DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL,
  `department_name` varchar(45) DEFAULT NULL,
  `department_desc` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`pdr_id`),
  UNIQUE KEY `idx_patient_department_relation_patient_id_department_id` (`patient_id`,`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8 COMMENT='病人和科室关联表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `study_department_relation`
--

DROP TABLE IF EXISTS `study_department_relation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `study_department_relation` (
  `sdr_id` int(11) NOT NULL AUTO_INCREMENT,
  `study_id` int(11) DEFAULT NULL,
  `department_id` int(11) DEFAULT NULL,
  `department_name` varchar(45) DEFAULT NULL,
  `department_desc` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`sdr_id`),
  UNIQUE KEY `idx_study_department_relation_study_id_department_id` (`study_id`,`department_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8 COMMENT='检查和科室关联表，检查和科室多对多关联';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-04-30 17:41:53
