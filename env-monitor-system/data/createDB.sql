-- 环境监测系统数据库脚本（MySQL 8.0+）
-- 说明：列名 role、percent 等为保留字或易冲突，统一使用反引号包裹。
--
-- 【编辑器红线】若提示「IF / EXISTS」或「非布尔表达式」：这是把本文件当成 T-SQL
-- 或通用 SQL 解析导致的误报。MySQL 中「CREATE DATABASE IF NOT EXISTS」语法合法。
-- 请在 Cursor/VS Code 将语言模式设为 MySQL；在 IntelliJ IDEA 中将 data 目录 SQL 方言设为 MySQL。

CREATE DATABASE IF NOT EXISTS sensor_db
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE sensor_db;

SET NAMES utf8mb4;

-- 以下为参考 DDL；也可依赖 Spring JPA ddl-auto=update 自动建表

CREATE TABLE IF NOT EXISTS `users` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(64) NOT NULL,
  `password_hash` VARCHAR(255) NOT NULL,
  `display_name` VARCHAR(128) DEFAULT NULL,
  `role` VARCHAR(16) NOT NULL COMMENT 'ADMIN / USER',
  `created_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_users_username` (`username`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `light_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `lux` DOUBLE NOT NULL,
  `recorded_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_light_recorded_at` (`recorded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `temperature_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `celsius` DOUBLE NOT NULL,
  `recorded_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_temperature_recorded_at` (`recorded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `gps_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `latitude` DECIMAL(10, 7) NOT NULL,
  `longitude` DECIMAL(10, 7) NOT NULL,
  `recorded_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_gps_recorded_at` (`recorded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `humidity_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `percent` DOUBLE NOT NULL COMMENT '相对湿度 0-100',
  `recorded_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_humidity_recorded_at` (`recorded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS `pressure_data` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `hpa` DOUBLE NOT NULL,
  `recorded_at` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_pressure_recorded_at` (`recorded_at`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
