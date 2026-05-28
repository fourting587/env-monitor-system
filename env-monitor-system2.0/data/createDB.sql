CREATE DATABASE IF NOT EXISTS env_monitor2_db
  DEFAULT CHARACTER SET utf8mb4
  COLLATE utf8mb4_unicode_ci;

USE env_monitor2_db;

SET NAMES utf8mb4;

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
