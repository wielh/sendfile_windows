-- MariaDB dump 10.19  Distrib 10.6.4-MariaDB, for Win64 (AMD64)
--
-- Host: localhost    Database: movefile_version3
-- ------------------------------------------------------
-- Server version	10.6.4-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

create database if not exist filesystem ;
use filesystem;

--
-- Table structure for table `tasks`
--

DROP TABLE IF EXISTS `tasks`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tasks` (
  `taskid` int(11) NOT NULL AUTO_INCREMENT,
  `task_name` varchar(100)  NOT NULL,
  `username` varchar(100) NOT NULL,
  `createdatetime` varchar(100)  NOT NULL,
  `move_or_copy` varchar(100) NOT NULL DEFAULT 'copy' COMMENT 'option={move,copy}',
  `execute_timing` varchar(100) NOT NULL COMMENT 'option={now,periodical}',
  `period` varchar(100) DEFAULT NULL,
  `day_of_month` int(11) DEFAULT NULL,
  `day_of_week` varchar(10) DEFAULT NULL,
  `hour` int(11) DEFAULT NULL,
  `minute` int(11) DEFAULT NULL,
  `timeout` float DEFAULT NULL,
  `source_connection_way` varchar(100) NOT NULL DEFAULT 'localhost/connected remote' COMMENT 'option={temporary cifs connection,temporary nfs connection,localhost/connected remote}',
  `source_ip` varchar(100) DEFAULT NULL,
  `source_connection_username` varchar(100) CHARACTER SET utf8mb3 DEFAULT NULL,
  `encrypted_source_connection_password` varchar(100) DEFAULT NULL,
  `source_mount_disk` varchar(100) DEFAULT NULL COMMENT 'source and target disk are mount point for windows',
  `source_folder` varchar(2000) NOT NULL,
  `source_subfolder` varchar(2000) DEFAULT NULL COMMENT '(1) *: move/copy all folder and file (2) *.* :move/copy all file (3) use ; to sperate distinct file or folder',
  `target_connection_way` varchar(100) NOT NULL DEFAULT 'localhost/connected remote' COMMENT 'option={temporary cifs connection,temporary nfs connection,localhost/connected remote}',
  `target_ip` varchar(100) DEFAULT NULL,
  `target_connection_username` varchar(100) DEFAULT NULL,
  `encrypted_target_connection_password` varchar(100) DEFAULT NULL,
  `target_mount_disk` varchar(100) DEFAULT NULL,
  `target_folder` varchar(2000) NOT NULL,
  PRIMARY KEY (`taskid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tasks`
--

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;

create user 'filesystemUser'@'%' IDENTIFIED BY 'filesystemUser';
GRANT Alter ON filesystem.tasks TO 'filesystemUser'@'%';
GRANT Delete ON filesystem.tasks TO 'filesystemUser'@'%';
GRANT Insert ON filesystem.tasks TO 'filesystemUser'@'%';
GRANT Select ON filesystem.tasks TO 'filesystemUser'@'%';
GRANT Update ON filesystem.tasks TO 'filesystemUser'@'%';
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2021-10-18 10:20:01
