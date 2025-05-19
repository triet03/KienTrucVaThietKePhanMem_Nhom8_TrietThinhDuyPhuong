-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Server version:               10.11.7-MariaDB - mariadb.org binary distribution
-- Server OS:                    Win64
-- HeidiSQL Version:             12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Dumping database structure for company
CREATE DATABASE IF NOT EXISTS `company` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `company`;

-- Dumping structure for table company.address
CREATE TABLE IF NOT EXISTS `address` (
  `country` int(11) DEFAULT NULL,
  `add_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `city` varchar(255) DEFAULT NULL,
  `number` varchar(255) DEFAULT NULL,
  `street` varchar(255) DEFAULT NULL,
  `zipcode` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`add_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.address: ~11 rows (approximately)
INSERT INTO `address` (`country`, `add_id`, `city`, `number`, `street`, `zipcode`) VALUES
	(700000, 1, 'xx', NULL, 'fdf', '111'),
	(700000, 3, 'xx', NULL, 'fdf', '111'),
	(700000, 4, 'xx', NULL, 'fdf', '111'),
	(888, 6, 'xx', NULL, 'fdf', '70000'),
	(888, 7, 'xx', NULL, 'fdf', '70000'),
	(700000, 8, 'xx', NULL, 'fdf', '111'),
	(4, 9, 'TP. Ho Chi Minh', NULL, 'addd', 'ww'),
	(700000, 10, 'xx', NULL, 'eeee', '111'),
	(700000, 11, 'xx', NULL, 'fdf', '111'),
	(888, 12, 'qqq', NULL, 'sss', '23/12'),
	(700000, 13, 'xx', NULL, 'eeee', '111');

-- Dumping structure for table company.candidate
CREATE TABLE IF NOT EXISTS `candidate` (
  `address` bigint(20) DEFAULT NULL,
  `can_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dob` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`can_id`),
  KEY `FKa8gnyyhbb2qnhp94grci1n0o9` (`address`),
  KEY `FK5q1ksqleh983c18yssyo2dn8l` (`address_id`),
  CONSTRAINT `FK5q1ksqleh983c18yssyo2dn8l` FOREIGN KEY (`address_id`) REFERENCES `address` (`add_id`),
  CONSTRAINT `FKa8gnyyhbb2qnhp94grci1n0o9` FOREIGN KEY (`address`) REFERENCES `address` (`add_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.candidate: ~3 rows (approximately)
INSERT INTO `candidate` (`address`, `can_id`, `dob`, `email`, `full_name`, `phone`, `address_id`) VALUES
	(NULL, 3, '2002-11-22 00:00:00.000000', 'hadota@gmail.com', 'Hà Dô ta', '352413123', 13),
	(NULL, 4, '2024-12-27 00:00:00.000000', 'ngophuc2911@gmail.com', 'dddddd', '0838028014', 11),
	(NULL, 5, '2003-03-02 00:00:00.000000', 'nguyenbaminhtriet0947@gmail.com', 'Minh Triet Nguyen Ba', '0947515138', 9);

-- Dumping structure for table company.candidate_skill
CREATE TABLE IF NOT EXISTS `candidate_skill` (
  `skill_level` tinyint(4) DEFAULT NULL,
  `can_id` bigint(20) NOT NULL,
  `skill_id` bigint(20) NOT NULL,
  `more_infos` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`can_id`,`skill_id`),
  KEY `FKb7cxhiqhcah7c20a2cdlvr1f8` (`skill_id`),
  CONSTRAINT `FKb0m5tm3fi0upa3b3kjx3vrlxs` FOREIGN KEY (`can_id`) REFERENCES `candidate` (`can_id`),
  CONSTRAINT `FKb7cxhiqhcah7c20a2cdlvr1f8` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`skill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.candidate_skill: ~2 rows (approximately)
INSERT INTO `candidate_skill` (`skill_level`, `can_id`, `skill_id`, `more_infos`) VALUES
	(1, 3, 1, 'lam truong nhom'),
	(4, 4, 2, 'cung tam');

-- Dumping structure for table company.company
CREATE TABLE IF NOT EXISTS `company` (
  `address` bigint(20) DEFAULT NULL,
  `com_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `about` varchar(255) DEFAULT NULL,
  `comp_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `web_url` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`com_id`),
  KEY `FKd5occp4cjwihejbxdbpvkp5tv` (`address`),
  CONSTRAINT `FKd5occp4cjwihejbxdbpvkp5tv` FOREIGN KEY (`address`) REFERENCES `address` (`add_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.company: ~1 rows (approximately)
INSERT INTO `company` (`address`, `com_id`, `about`, `comp_name`, `email`, `phone`, `web_url`) VALUES
	(12, 4, 'Hân hạnh tài trợ trang web này', 'Công Ty NBMT', 'nbmt1111@gmail.com', '09231234', 'nbmt.com');

-- Dumping structure for table company.experience
CREATE TABLE IF NOT EXISTS `experience` (
  `can_id` bigint(20) DEFAULT NULL,
  `exp_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `from_date` datetime(6) DEFAULT NULL,
  `to_date` datetime(6) DEFAULT NULL,
  `company` varchar(255) DEFAULT NULL,
  `work_desc` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`exp_id`),
  KEY `FK8d5oqe0wxh52v352i04qnuady` (`can_id`),
  CONSTRAINT `FK8d5oqe0wxh52v352i04qnuady` FOREIGN KEY (`can_id`) REFERENCES `candidate` (`can_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.experience: ~0 rows (approximately)

-- Dumping structure for table company.job
CREATE TABLE IF NOT EXISTS `job` (
  `company` bigint(20) DEFAULT NULL,
  `job_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_desc` varchar(255) DEFAULT NULL,
  `job_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`job_id`),
  KEY `FKbaqlvluu78phmo9ld89um7wnm` (`company`),
  CONSTRAINT `FKbaqlvluu78phmo9ld89um7wnm` FOREIGN KEY (`company`) REFERENCES `company` (`com_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.job: ~3 rows (approximately)
INSERT INTO `job` (`company`, `job_id`, `job_desc`, `job_name`) VALUES
	(4, 1, 'don dep ', 'tap vu'),
	(4, 3, 'giup giam doc xu ly cong viec ,va len lich hop ', 'Thu Ky Giam Doc'),
	(4, 4, 'lập trình web', 'Lập trình Web');

-- Dumping structure for table company.job_skill
CREATE TABLE IF NOT EXISTS `job_skill` (
  `skill_level` tinyint(4) DEFAULT NULL,
  `job_id` bigint(20) NOT NULL,
  `skill_id` bigint(20) NOT NULL,
  `more_infos` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`job_id`,`skill_id`),
  KEY `FKj33qbbf3vk1lvhqpcosnh54u1` (`skill_id`),
  CONSTRAINT `FK9ix4wg520ii2gu2felxdhdup6` FOREIGN KEY (`job_id`) REFERENCES `job` (`job_id`),
  CONSTRAINT `FKj33qbbf3vk1lvhqpcosnh54u1` FOREIGN KEY (`skill_id`) REFERENCES `skill` (`skill_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.job_skill: ~2 rows (approximately)
INSERT INTO `job_skill` (`skill_level`, `job_id`, `skill_id`, `more_infos`) VALUES
	(NULL, 3, 1, NULL),
	(NULL, 4, 3, NULL);

-- Dumping structure for table company.skill
CREATE TABLE IF NOT EXISTS `skill` (
  `skill_type` tinyint(4) DEFAULT NULL,
  `skill_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `skill_desc` varchar(255) DEFAULT NULL,
  `skill_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`skill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.skill: ~3 rows (approximately)
INSERT INTO `skill` (`skill_type`, `skill_id`, `skill_desc`, `skill_name`) VALUES
	(1, 1, 'dung thanh thao word ,excel ,power point', 'Ky nang van phong'),
	(0, 2, 'Quyet dinh thoi gian bat dau va hoan thanh du an hop li', 'Ky nang quan ly thoi gian'),
	(NULL, 3, 'phải chuyên nghiệp', 'lập trình chuyên nghiệp');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
