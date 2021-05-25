-- --------------------------------------------------------
-- 主機:                           127.0.0.1
-- 服務器版本:                        10.3.7-MariaDB - mariadb.org binary distribution
-- 服務器操作系統:                      Win64
-- HeidiSQL 版本:                  9.4.0.5125
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;


-- 導出 tokendb 的資料庫結構
CREATE DATABASE IF NOT EXISTS `tokendb` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `tokendb`;

-- 導出  表 tokendb.access_token 結構
CREATE TABLE IF NOT EXISTS `access_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `client_id` int(11) NOT NULL COMMENT 'client_info.id',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'username in member center',
  `scope` int(11) DEFAULT NULL COMMENT 'auth scope',
  `access_token` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'jwt token',
  `refresh_token` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'refresh token',
  `token_state` int(11) NOT NULL COMMENT '0 normal , 1 destory',
  `date_created` datetime DEFAULT NULL,
  `expire_date` datetime DEFAULT NULL COMMENT 'token auto expire date',
  PRIMARY KEY (`id`),
  UNIQUE KEY `access_token` (`access_token`),
  KEY `username` (`username`),
  KEY `scope` (`scope`),
  KEY `token_state` (`token_state`),
  KEY `refresh_token` (`refresh_token`),
  KEY `client_id` (`client_id`),
  CONSTRAINT `FK_access_token_client_info` FOREIGN KEY (`client_id`) REFERENCES `client_info` (`id`),
  CONSTRAINT `FKrmrkj8xv1fdga1x9p7fvnxbcb` FOREIGN KEY (`client_id`) REFERENCES `client_info` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在導出表  tokendb.access_token 的資料：~0 rows (大約)
/*!40000 ALTER TABLE `access_token` DISABLE KEYS */;
/*!40000 ALTER TABLE `access_token` ENABLE KEYS */;

-- 導出  表 tokendb.client_info 結構
CREATE TABLE IF NOT EXISTS `client_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `developer_id` int(11) NOT NULL COMMENT 'developer.id the developer create this clien',
  `client_id` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'oauth client id',
  `client_secret` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'oauth client secret',
  `redirect_uri` varchar(256) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'oauth redirect uri',
  `scope` int(11) NOT NULL COMMENT 'allow oauth scope',
  `client_state` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'init,reviewing,publish,deny',
  `client_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'WEB_APP,JS_APP,NATIVE_APP,SERVER',
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'remark information',
  `reason` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'review reason',
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL COMMENT 'last update date',
  `last_update_user` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `client_id` (`client_id`),
  KEY `client_secret` (`client_secret`),
  KEY `redirect_uri` (`redirect_uri`),
  KEY `scope` (`scope`),
  KEY `client_state` (`client_state`),
  KEY `client_type` (`client_type`),
  KEY `note` (`note`),
  KEY `developer_id` (`developer_id`),
  CONSTRAINT `FK2xl7nci9w55cakxt9lkuoe0go` FOREIGN KEY (`developer_id`) REFERENCES `developer` (`id`),
  CONSTRAINT `FK8oohx2b1wx670yi6qtktfk4e8` FOREIGN KEY (`developer_id`) REFERENCES `developer` (`id`),
  CONSTRAINT `FK_client_info_developer` FOREIGN KEY (`developer_id`) REFERENCES `developer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在導出表  tokendb.client_info 的資料：~4 rows (大約)
/*!40000 ALTER TABLE `client_info` DISABLE KEYS */;
INSERT INTO `client_info` (`id`, `developer_id`, `client_id`, `client_secret`, `redirect_uri`, `scope`, `client_state`, `client_type`, `note`, `reason`, `date_created`, `last_updated`, `last_update_user`) VALUES
	(18, 14, '60309538-6e88-4e11-8277-dc79747ad64f', '3d6d22cc-9271-4008-b9f9-c1ce9a1a4d80', 'http://www.google.com', 4, 'init', 'JS_APP', '測試用途...', NULL, '2018-09-20 12:18:59', '2018-09-20 14:40:53', 'arc'),
	(19, 14, '62d03592-f828-4aa8-967e-5a3e70976fc4', '445457e0-a2d2-4b2f-b314-989c867ba768', 'http://www.yahoo.com.tw', 18, 'init', 'NATIVE_APP', '手機測試', 'xxxx', '2018-09-20 14:17:55', '2018-09-20 16:35:17', 'arc'),
	(20, 15, 'bc220205-17af-4adb-844e-ffaa386d5d07', '21d08e9f-8e9c-45de-8f9c-551eb03d8b71', 'http://www.google.com', 104, 'init', 'NATIVE_APP', 'sdfasdfasdfas', NULL, '2018-09-20 14:46:31', '2018-09-20 14:48:08', 'guest'),
	(21, 14, '45749883-c371-4e76-a64c-3318d3ee7640', 'db01986c-76a8-4f10-8567-b5a69e29eb24', 'http://127.0.0.1:7080/ArcareAuthorizationModule/clientService/webServer', 54, 'publish', 'WEB_APP', '.........', NULL, '2018-09-20 16:44:03', '2018-09-21 09:28:47', 'arc');
/*!40000 ALTER TABLE `client_info` ENABLE KEYS */;

-- 導出  表 tokendb.client_info_detail 結構
CREATE TABLE IF NOT EXISTS `client_info_detail` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `client_id` int(11) NOT NULL COMMENT 'client_info.client_id',
  `sign_key` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'key use to sign token',
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sign_key` (`sign_key`),
  KEY `client_id` (`client_id`),
  CONSTRAINT `FK25kfb23hrwn389ic5704v3v7d` FOREIGN KEY (`client_id`) REFERENCES `client_info` (`id`),
  CONSTRAINT `FK_client_info_detail_client_info` FOREIGN KEY (`client_id`) REFERENCES `client_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在導出表  tokendb.client_info_detail 的資料：~4 rows (大約)
/*!40000 ALTER TABLE `client_info_detail` DISABLE KEYS */;
INSERT INTO `client_info_detail` (`id`, `client_id`, `sign_key`, `date_created`, `last_updated`) VALUES
	(6, 18, '6bd2e23a-9b23-4a08-af01-e0eb048f4e7a', '2018-09-20 12:18:59', '2018-09-20 12:18:59'),
	(7, 19, 'd7a66a6f-8a0b-4bb6-8f2b-ad2d2c7d010d', '2018-09-20 14:17:55', '2018-09-20 14:17:55'),
	(8, 20, '62c362fb-250b-4b6f-97e0-268224d24f28', '2018-09-20 14:46:31', '2018-09-20 14:46:31'),
	(9, 21, 'ff9c15cf-7572-46da-b982-156decaee239', '2018-09-20 16:44:03', '2018-09-20 16:44:03');
/*!40000 ALTER TABLE `client_info_detail` ENABLE KEYS */;

-- 導出  表 tokendb.client_type 結構
CREATE TABLE IF NOT EXISTS `client_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `client_type` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `client_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在導出表  tokendb.client_type 的資料：~4 rows (大約)
/*!40000 ALTER TABLE `client_type` DISABLE KEYS */;
INSERT INTO `client_type` (`id`, `client_type`, `client_name`) VALUES
	(1, 'WEB_APP', 'Web Service Application'),
	(2, 'JS_APP', 'JavaScript Browser Application'),
	(3, 'NATIVE_APP', 'Desktop/Mobile Application'),
	(4, 'SERVER', 'SERVER');
/*!40000 ALTER TABLE `client_type` ENABLE KEYS */;

-- 導出  表 tokendb.developer 結構
CREATE TABLE IF NOT EXISTS `developer` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'developer name',
  `password` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'developer password',
  `email` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'developer email fo notify',
  `user_type` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'admin,dev',
  `date_created` datetime DEFAULT NULL COMMENT 'developer regist date',
  `last_updated` datetime DEFAULT NULL COMMENT 'developer last login',
  `last_update_user` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `username` (`username`),
  KEY `password` (`password`),
  KEY `user_type` (`user_type`),
  KEY `email` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在導出表  tokendb.developer 的資料：~2 rows (大約)
/*!40000 ALTER TABLE `developer` DISABLE KEYS */;
INSERT INTO `developer` (`id`, `username`, `password`, `email`, `user_type`, `date_created`, `last_updated`, `last_update_user`) VALUES
	(14, 'arc', '', 'ptx48691@gmail.com', 'admin', '2018-09-20 12:18:23', '2018-09-21 09:15:56', 'arc'),
	(15, 'guest', '', 'xxxx@gmail.com', 'dev', '2018-09-20 14:45:56', '2018-09-20 16:13:38', 'arc');
/*!40000 ALTER TABLE `developer` ENABLE KEYS */;

-- 導出  表 tokendb.grant_code 結構
CREATE TABLE IF NOT EXISTS `grant_code` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `client_id` int(11) NOT NULL COMMENT 'client_info.id',
  `username` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'username in member center',
  `scope` int(11) NOT NULL COMMENT 'grant scope',
  `grant_code` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'grant code for exchange access_token',
  `code_state` int(11) DEFAULT NULL COMMENT '0 not use , 1 expire',
  `date_created` datetime DEFAULT NULL COMMENT 'grant_code generated date',
  `expire_date` datetime DEFAULT NULL COMMENT 'grant_code expire date',
  PRIMARY KEY (`id`),
  UNIQUE KEY `grant_code` (`grant_code`),
  KEY `username` (`username`),
  KEY `scope` (`scope`),
  KEY `code_state` (`code_state`),
  KEY `FK8ckayj6v7g4mgsejo1ebi6pfe` (`client_id`),
  CONSTRAINT `FK8ckayj6v7g4mgsejo1ebi6pfe` FOREIGN KEY (`client_id`) REFERENCES `client_info` (`id`),
  CONSTRAINT `FK_grant_code_client_info` FOREIGN KEY (`client_id`) REFERENCES `client_info` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在導出表  tokendb.grant_code 的資料：~0 rows (大約)
/*!40000 ALTER TABLE `grant_code` DISABLE KEYS */;
INSERT INTO `grant_code` (`id`, `client_id`, `username`, `scope`, `grant_code`, `code_state`, `date_created`, `expire_date`) VALUES
	(1, 21, 'zzz', 54, '38110013-c746-4627-a357-304ef41a1c71', 0, '2018-09-21 10:36:14', '2018-09-21 10:46:14');
/*!40000 ALTER TABLE `grant_code` ENABLE KEYS */;

-- 導出  表 tokendb.resource 結構
CREATE TABLE IF NOT EXISTS `resource` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `func_version` int(11) NOT NULL COMMENT 'verison',
  `func_name` varchar(255) COLLATE utf8mb4_unicode_ci NOT NULL COMMENT 'function name',
  `uri` varchar(2048) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `require_scope` int(11) NOT NULL COMMENT 'function require scope',
  `note` varchar(255) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `date_created` datetime DEFAULT NULL,
  `last_updated` datetime DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `version_function` (`func_version`,`func_name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 正在導出表  tokendb.resource 的資料：~0 rows (大約)
/*!40000 ALTER TABLE `resource` DISABLE KEYS */;
/*!40000 ALTER TABLE `resource` ENABLE KEYS */;

-- 導出  表 tokendb.scope 結構
CREATE TABLE IF NOT EXISTS `scope` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `scope_value` int(11) DEFAULT NULL COMMENT 'value',
  `scope_name` varchar(50) COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'name',
  `note` text COLLATE utf8mb4_unicode_ci DEFAULT NULL COMMENT 'note',
  PRIMARY KEY (`id`),
  KEY `scope` (`scope_name`)
) ENGINE=InnoDB AUTO_INCREMENT=129 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='範圍';

-- 正在導出表  tokendb.scope 的資料：~8 rows (大約)
/*!40000 ALTER TABLE `scope` DISABLE KEYS */;
INSERT INTO `scope` (`id`, `scope_value`, `scope_name`, `note`) VALUES
	(1, 1, 'all', '所有權限'),
	(2, 2, 'create', '新增資料'),
	(3, 4, 'read', '查詢資料'),
	(4, 8, 'update', '更新資料'),
	(5, 16, 'delete', '刪除資料'),
	(6, 32, 'batch', '批次處理'),
	(7, 64, 'mail', '發送郵件'),
	(8, 128, 'message', '發送信息');
/*!40000 ALTER TABLE `scope` ENABLE KEYS */;

/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
