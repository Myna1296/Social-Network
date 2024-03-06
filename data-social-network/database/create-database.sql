CREATE SCHEMA `social-network` DEFAULT CHARACTER SET utf8 ;

CREATE TABLE `social-network`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_name` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  `birthday` DATETIME NULL,
  `phone` VARCHAR(255) NULL,
  `sex` INT NULL,
  `address` VARCHAR(255) NULL,
  `image` VARCHAR(255) NULL,
  `job` VARCHAR(255) NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE `social-network`.`otp` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `code` VARCHAR(45) NOT NULL,
  `created_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

