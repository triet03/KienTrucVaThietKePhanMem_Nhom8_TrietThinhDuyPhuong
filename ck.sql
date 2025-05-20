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
  `id` bigint(20) NOT NULL,
  PRIMARY KEY (`add_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.address: ~6 rows (approximately)
INSERT INTO `address` (`country`, `add_id`, `city`, `number`, `street`, `zipcode`, `id`) VALUES
	(700000, 1, 'xx', NULL, 'fdf', '111', 0),
	(700000, 3, 'xx', NULL, 'fdf', '111', 0),
	(700000, 4, 'xx', NULL, 'fdf', '111', 0),
	(888, 6, 'xx', NULL, 'fdf', '70000', 0),
	(888, 7, 'xx', NULL, 'fdf', '70000', 0),
	(700000, 8, 'xx', NULL, 'fdf', '111', 0),
	(700000, 9, 'xx', NULL, 'fdf', '111', 0);

-- Dumping structure for table company.application
CREATE TABLE IF NOT EXISTS `application` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company` varchar(255) DEFAULT NULL,
  `cover_letter` varchar(255) DEFAULT NULL,
  `cv_file_name` varchar(255) DEFAULT NULL,
  `date_applied` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `experience` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `skills` varchar(255) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.application: ~3 rows (approximately)
INSERT INTO `application` (`id`, `company`, `cover_letter`, `cv_file_name`, `date_applied`, `email`, `experience`, `full_name`, `phone`, `position`, `skills`, `status`) VALUES
	(1, 'TNHH 1 thanh Vien1', 'das', '4ada87de-e99f-4efd-9010-5f72d0c567d1_1131w-PQxzeV9wixU.webp', '2025-05-16', 'admin@gmail.com', 'asd', 'Triet', '0947515138', 'Nhân viên Kinh Doanh', 'dsa', 'Đang chờ duyệt'),
	(3, 'TNHH 1 thanh Vien1', 'dsf', '13f231cd-6927-4842-95f3-291db0722f6b_mau-cv-thanh-lich.png', '2025-05-19', 'anh@gmail.com', 'hfd', 'Anh', '1234567890', 'Chuyên viên Marketing\r\n', 'sdf', 'Đang chờ duyệt'),
	(4, 'HelloViecLam', 'sda', 'f935239f-6cbf-400d-b15a-0dfe492a437a_images.png', '2025-05-19', 'a@gmail.com', 'asdf', 'Mr.A', '0123795995', 'Giám Sát Công Trình Xây Dựng', 'gfgew', 'Đang chờ duyệt');

-- Dumping structure for table company.candidate
CREATE TABLE IF NOT EXISTS `candidate` (
  `address` bigint(20) DEFAULT NULL,
  `can_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `dob` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `full_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`can_id`),
  KEY `FKa8gnyyhbb2qnhp94grci1n0o9` (`address`),
  KEY `FK5q1ksqleh983c18yssyo2dn8l` (`address_id`),
  KEY `idx_email` (`email`),
  CONSTRAINT `FK5q1ksqleh983c18yssyo2dn8l` FOREIGN KEY (`address_id`) REFERENCES `address` (`add_id`),
  CONSTRAINT `FKa8gnyyhbb2qnhp94grci1n0o9` FOREIGN KEY (`address`) REFERENCES `address` (`add_id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.candidate: ~2 rows (approximately)
INSERT INTO `candidate` (`address`, `can_id`, `dob`, `email`, `full_name`, `phone`, `address_id`, `password`) VALUES
	(NULL, 3, '2024-12-19 00:00:00.000000', 'np2911azza@gmail.com', 'Ngô Quang Phúc', '0838028014', 9, NULL),
	(NULL, 4, '2024-12-27 00:00:00.000000', 'ngophuc2911@gmail.com', 'Ngô Quang Tuấn', '0838028014', 8, NULL),
	(NULL, 5, NULL, 'nguyenbaminhtriet0947@gmail.com', 'Triet', NULL, NULL, '$2a$10$iSim9xCwNiDvAVkpTvnvg.TrfpRW3Smiq3raXrmOPOhn2CZ0ebIFe');

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
	(4, 4, 2, 'cung tam'),
	(1, 4, 9, NULL);

-- Dumping structure for table company.company
CREATE TABLE IF NOT EXISTS `company` (
  `address` bigint(20) DEFAULT NULL,
  `com_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `about` varchar(255) DEFAULT NULL,
  `comp_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `web_url` varchar(255) DEFAULT NULL,
  `email_verified` bit(1) NOT NULL DEFAULT b'0',
  `password` varchar(255) DEFAULT NULL,
  `verification_token` varchar(255) DEFAULT NULL,
  `address_id` bigint(20) DEFAULT NULL,
  `add_id` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`com_id`),
  KEY `FKd5occp4cjwihejbxdbpvkp5tv` (`address`),
  CONSTRAINT `FKd5occp4cjwihejbxdbpvkp5tv` FOREIGN KEY (`address`) REFERENCES `address` (`add_id`)
) ENGINE=InnoDB AUTO_INCREMENT=49 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.company: ~6 rows (approximately)
INSERT INTO `company` (`address`, `com_id`, `about`, `comp_name`, `email`, `phone`, `web_url`, `email_verified`, `password`, `verification_token`, `address_id`, `add_id`) VALUES
	(7, 4, 'abc', 'TNHH 1 thanh Vien1', 'ngophuc2911@gmail.com', '0838028014', 'motthanhvien.com', b'0', 'phuc3333', NULL, NULL, NULL),
	(4, 11, 'Công ty uy tín', 'Công ty CCS', 'ccs@gmail.com', '0837115531', 'css.com', b'1', 'cssssss', NULL, NULL, NULL),
	(1, 34, 'xcdf', 'HelloViecLam', 'hellovieclam@gmail.com', '0833441121', 'sd.com', b'1', '$2a$10$ztmTxLmOj3cLhGrZZRiHzesGJ6qXhacDG.0N79QJb49MhhEWpI2cG', NULL, NULL, NULL),
	(NULL, 36, 'Triết tự lập', 'TrietKN', 'nguyenbaminhtriet0947@gmail.com', '0947515138', 'TrietKN.com', b'1', '$2a$10$IPFAczZMYM3QgVozSiEBB.GwIx8J5c0mVGIA8KvVWy.W/VIDamJli', NULL, NULL, NULL),
	(NULL, 42, 'sdfwefrwer', 'sas', 'nguyenbaminhtriet0947+`@gmail.com', '0947515138', 'TrietKN.com', b'1', '$2a$10$/hcU2A1hHEymL01GJfTZU.PBLziZj3PUtg4uwO.TkyiUEELSB9Thm', NULL, NULL, NULL),
	(NULL, 47, 'asd', 'Yoho', 'nguyenbaminhtriet0947+2@gmail.com', '0947515138', 'asd', b'1', '$2a$10$QL3nHyomE83c5RKNg7wqRuqJOGhcOOD4P.KcAWOTz4O98pIKXz766', NULL, NULL, NULL);

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
  `salary` int(255) NOT NULL,
  `status` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`job_id`),
  KEY `FKbaqlvluu78phmo9ld89um7wnm` (`company`),
  CONSTRAINT `FKbaqlvluu78phmo9ld89um7wnm` FOREIGN KEY (`company`) REFERENCES `company` (`com_id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.job: ~14 rows (approximately)
INSERT INTO `job` (`company`, `job_id`, `job_desc`, `job_name`, `salary`, `status`) VALUES
	(4, 1, 'Tiếp nhận và xử lý đơn hàng từ khách hàng hiện có của Công ty ', 'Nhân viên Kinh Doanh', 7000000, 'APPROVED'),
	(34, 2, ' Triển khai bản vẽ thi công.', 'Giám Sát Công Trình Xây Dựng', 12000000, 'APPROVED'),
	(11, 3, 'Đứng máy sản xuất & thực hiện sửa chữa, bảo trì máy móc khi có yêu cầu', 'Kỹ Thuật Vận Hành Máy', 15000000, 'APPROVED'),
	(11, 4, 'Hỗ trợ thông dịch cho các đối tác nước ngoài', 'Thông dịch viên Tiếng Anh', 10000000, 'APPROVED'),
	(11, 5, 'Tư vấn và chăm sóc khách hàng về sản phẩm.	', 'Nhân viên Tư vấn Bán hàng\n', 8000000, 'APPROVED'),
	(4, 6, 'Lập kế hoạch và triển khai các chiến dịch marketing.	', 'Chuyên viên Marketing\n', 17000000, 'APPROVED'),
	(34, 7, 'Lên ý tưởng và thiết kế giao diện website.	', 'UI/UX Designer\n', 12000000, 'APPROVED'),
	(36, 10, 'thiết kế các dự án và làm hồ sơ cho dự án', 'Nhân viên thiết kế & Chuyên làm hồ sơ dự án', 18000000, NULL),
	(34, 16, 'thực hiện công việc bồi bàn, lau dọn', 'Phục vụ nhà hàng', 9000000, NULL),
	(11, 17, 'Trả lời các câu hỏi, yêu cầu của khách hàng qua điện thoại', 'Nhân viên tư vấn điện thoại', 8000000, NULL),
	(11, 21, 'thực hiện công việc bồi bàn, lau dọn', 'Phục vụ nhà hàng', 10000000, NULL),
	(34, 22, 'thiết kế các dự án và làm hồ sơ cho dự án', 'Nhân viên thiết kế & Chuyên làm hồ sơ dự án', 15000000, 'APPROVED'),
	(42, 23, 'Sắp xếp, phân loại hàng hóa trong kho. Thực hiện các công việc khác theo yêu cầu của quản lý', 'Công nhân kho', 10000000, 'REJECTED'),
	(4, 26, 'asdasdad', 'Nhân viên thiết kế & Chuyên làm hồ sơ dự án', 111111111, 'REJECTED');

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

-- Dumping data for table company.job_skill: ~14 rows (approximately)
INSERT INTO `job_skill` (`skill_level`, `job_id`, `skill_id`, `more_infos`) VALUES
	(NULL, 1, 1, NULL),
	(NULL, 2, 2, NULL),
	(NULL, 3, 3, NULL),
	(NULL, 4, 4, NULL),
	(NULL, 6, 6, NULL),
	(NULL, 10, 1, NULL),
	(NULL, 16, 2, NULL),
	(NULL, 16, 4, NULL),
	(NULL, 17, 4, NULL),
	(NULL, 21, 4, NULL),
	(NULL, 22, 1, NULL),
	(NULL, 22, 2, NULL),
	(NULL, 23, 6, NULL),
	(NULL, 26, 1, NULL);

-- Dumping structure for table company.skill
CREATE TABLE IF NOT EXISTS `skill` (
  `skill_type` tinyint(4) DEFAULT NULL,
  `skill_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `skill_desc` varchar(255) DEFAULT NULL,
  `skill_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`skill_id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.skill: ~10 rows (approximately)
INSERT INTO `skill` (`skill_type`, `skill_id`, `skill_desc`, `skill_name`) VALUES
	(1, 1, 'Thành thạo thao tác trên các ứng dụng văn phòng: Word, Excel,...', 'Kỹ năng văn phòng'),
	(0, 2, 'Quyết định thời gian bắt đầu và kết thúc, sắp xếp thời gian giữa các phiên họp', 'Kỹ năng quản lý thời gian'),
	(2, 3, 'Thành thạo với các con số, tính toán nhanh', 'Chuyên môn toán học'),
	(1, 4, 'Nói chuyện hoà đồng, vui vẻ, nhanh nhạy, gây cảm tình cho đối phương', 'Kỹ năng giao tiếp'),
	(3, 5, 'Có khả năng lãnh đạo, điều phối công việc trong 1 nhóm', 'Quản lý team'),
	(1, 6, 'Có khả năng làm việc nhóm, hỗ trợ và phối hợp tốt với đồng nghiệp.	', 'Kỹ năng làm việc nhóm\n'),
	(2, 7, 'Biết cách phân tích dữ liệu và đưa ra quyết định dựa trên dữ liệu.	', 'Phân tích dữ liệu\n'),
	(3, 8, 'Thành thạo sử dụng các công cụ đồ họa như Photoshop, Illustrator.	', 'Thiết kế đồ họa\n'),
	(2, 9, 'Giao tiếp tốt bằng tiếng Anh trong công việc và trao đổi với khách hàng quốc tế.	', 'Kỹ năng giao tiếp tiếng Anh\n'),
	(1, 10, 'Có thể lập trình với nhiều ngôn ngữ như Java, Python, C++.	', 'Kỹ năng lập trình\n');

-- Dumping structure for table company.verification_code
CREATE TABLE IF NOT EXISTS `verification_code` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(6) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL,
  `expiry_date` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Dumping data for table company.verification_code: ~0 rows (approximately)

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
