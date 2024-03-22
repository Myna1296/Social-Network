CREATE SCHEMA IF NOT EXISTS `social-network` DEFAULT CHARACTER SET utf8;

CREATE TABLE IF NOT EXISTS `social-network`.`user` (
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
  `token` VARCHAR(255) NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_email` (`email`)
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `social-network`.`otp` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(255) NOT NULL,
  `code` VARCHAR(45) NOT NULL,
  `created_date` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`email`) REFERENCES `user`(`email`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `social-network`.`friendship` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_sender` INT NOT NULL,
  `user_receiver` INT NOT NULL,
  `accepted` BOOLEAN NOT NULL,
  `created_date` DATETIME NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_sender`) REFERENCES `social-network`.`user`(`id`),
  FOREIGN KEY (`user_receiver`) REFERENCES `social-network`.`user`(`id`)
)ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `social-network`.`status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `title` VARCHAR(255) NOT NULL,
  `status_image` VARCHAR(255) NULL,
  `status_text` TEXT NOT NULL,
  `createdate` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `social-network`.`comment` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `status_id` INT NOT NULL,
  `comment_text` TEXT NOT NULL,
  `createdate` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`status_id`) REFERENCES `status`(`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `social-network`.`like_status` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `user_id` INT NOT NULL,
  `status_id` INT NOT NULL,
  `createdate` DATETIME NOT NULL,
  PRIMARY KEY (`id`),
  FOREIGN KEY (`user_id`) REFERENCES `user`(`id`),
  FOREIGN KEY (`status_id`) REFERENCES `status`(`id`)
) ENGINE = InnoDB DEFAULT CHARACTER SET = utf8;