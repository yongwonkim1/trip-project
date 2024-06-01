-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema ssafytrip
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ssafytrip
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ssafytrip` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `ssafytrip` ;

-- -----------------------------------------------------
-- Table `ssafytrip`.`sido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`sido` (
  `sido_code` INT NOT NULL,
  `sido_name` VARCHAR(30) NULL DEFAULT NULL,
  PRIMARY KEY (`sido_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ssafytrip`.`gugun`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`gugun` (
  `gugun_code` INT NOT NULL,
  `gugun_name` VARCHAR(30) NULL DEFAULT NULL,
  `sido_code` INT NOT NULL,
  PRIMARY KEY (`gugun_code`, `sido_code`),
  INDEX `gugun_to_sido_sido_code_fk_idx` (`sido_code` ASC) VISIBLE,
  CONSTRAINT `gugun_to_sido_sido_code_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `ssafytrip`.`sido` (`sido_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ssafytrip`.`attraction_info`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`attraction_info` (
  `content_id` INT NOT NULL,
  `content_type_id` INT NULL DEFAULT NULL,
  `title` VARCHAR(100) NULL DEFAULT NULL,
  `addr1` VARCHAR(100) NULL DEFAULT NULL,
  `addr2` VARCHAR(50) NULL DEFAULT NULL,
  `zipcode` VARCHAR(50) NULL DEFAULT NULL,
  `tel` VARCHAR(50) NULL DEFAULT NULL,
  `first_image` VARCHAR(200) NULL DEFAULT NULL,
  `first_image2` VARCHAR(200) NULL DEFAULT NULL,
  `readcount` INT NULL DEFAULT NULL,
  `sido_code` INT NULL DEFAULT NULL,
  `gugun_code` INT NULL DEFAULT NULL,
  `latitude` DECIMAL(20,17) NULL DEFAULT NULL,
  `longitude` DECIMAL(20,17) NULL DEFAULT NULL,
  `mlevel` VARCHAR(2) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  INDEX `attraction_to_content_type_id_fk_idx` (`content_type_id` ASC) VISIBLE,
  INDEX `attraction_to_sido_code_fk_idx` (`sido_code` ASC) VISIBLE,
  INDEX `attraction_to_gugun_code_fk_idx` (`gugun_code` ASC) VISIBLE,
  CONSTRAINT `attraction_to_content_type_id_fk`
    FOREIGN KEY (`content_type_id`)
    REFERENCES `ssafytrip`.`content_type` (`content_type_id`),
  CONSTRAINT `attraction_to_gugun_code_fk`
    FOREIGN KEY (`gugun_code`)
    REFERENCES `ssafytrip`.`gugun` (`gugun_code`),
  CONSTRAINT `attraction_to_sido_code_fk`
    FOREIGN KEY (`sido_code`)
    REFERENCES `ssafytrip`.`sido` (`sido_code`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ssafytrip`.`attraction_description`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`attraction_description` (
  `content_id` INT NOT NULL,
  `homepage` VARCHAR(100) NULL DEFAULT NULL,
  `overview` VARCHAR(10000) NULL DEFAULT NULL,
  `telname` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  CONSTRAINT `attraction_detail_to_attraciton_id_fk`
    FOREIGN KEY (`content_id`)
    REFERENCES `ssafytrip`.`attraction_info` (`content_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ssafytrip`.`attraction_detail`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`attraction_detail` (
  `content_id` INT NOT NULL,
  `cat1` VARCHAR(3) NULL DEFAULT NULL,
  `cat2` VARCHAR(5) NULL DEFAULT NULL,
  `cat3` VARCHAR(9) NULL DEFAULT NULL,
  `created_time` VARCHAR(14) NULL DEFAULT NULL,
  `modified_time` VARCHAR(14) NULL DEFAULT NULL,
  `booktour` VARCHAR(5) NULL DEFAULT NULL,
  PRIMARY KEY (`content_id`),
  CONSTRAINT `attraction_detail_to_basic_content_id_fk`
    FOREIGN KEY (`content_id`)
    REFERENCES `ssafytrip`.`attraction_info` (`content_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb3;


-- -----------------------------------------------------
-- Table `ssafytrip`.`review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`review` (
  `rid` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(45) NOT NULL,
  `aid` INT NOT NULL,
  `title` VARCHAR(45) NULL DEFAULT NULL,
  `contents` VARCHAR(1000) NULL DEFAULT NULL,
  `rating` INT NULL DEFAULT NULL,
  `view_count` INT NULL DEFAULT '0',
  `date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`rid`))
ENGINE = InnoDB
AUTO_INCREMENT = 21
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`user` (
  `user_id` VARCHAR(40) NOT NULL,
  `user_pw` VARCHAR(45) NOT NULL,
  `user_name` VARCHAR(45) NOT NULL,
  `nickname` VARCHAR(45) NOT NULL,
  `profile_img` VARCHAR(100) NULL DEFAULT NULL,
  `gender` VARCHAR(10) NOT NULL,
  `birth` DATE NOT NULL,
  `refreshToken` VARCHAR(255) NULL DEFAULT NULL,
  `sido_code` INT NOT NULL,
  `mtti` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`comment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`comment` (
  `cid` INT NOT NULL AUTO_INCREMENT,
  `rid` INT NOT NULL,
  `user_id` VARCHAR(40) NOT NULL,
  `content` VARCHAR(400) NULL DEFAULT NULL,
  `date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`cid`),
  INDEX `fk_comment_review_idx` (`rid` ASC) VISIBLE,
  INDEX `fk_comment_user_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_comment_review`
    FOREIGN KEY (`rid`)
    REFERENCES `ssafytrip`.`review` (`rid`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_comment_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `ssafytrip`.`user` (`user_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 10
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`plan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`plan` (
  `pid` INT NOT NULL AUTO_INCREMENT,
  `user_id` VARCHAR(45) NOT NULL,
  `title` VARCHAR(100) NOT NULL,
  `style` VARCHAR(50) NULL DEFAULT NULL,
  `image` VARCHAR(200) NULL DEFAULT NULL,
  `startDate` DATE NOT NULL,
  `Duration` INT NOT NULL,
  PRIMARY KEY (`pid`))
ENGINE = InnoDB
AUTO_INCREMENT = 17
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`detailplan`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`detailplan` (
  `dpid` INT NOT NULL AUTO_INCREMENT,
  `pid` INT NOT NULL,
  `userId` VARCHAR(45) NOT NULL,
  `aid` INT NOT NULL,
  `day` INT NOT NULL,
  `porder` INT NOT NULL,
  PRIMARY KEY (`dpid`),
  INDEX `pid_idx` (`pid` ASC) VISIBLE,
  INDEX `aid_idx` (`aid` ASC) VISIBLE,
  CONSTRAINT `detailPlan_to_attraction_info_fk`
    FOREIGN KEY (`aid`)
    REFERENCES `ssafytrip`.`attraction_info` (`content_id`),
  CONSTRAINT `detailPlan_to_plan_fk`
    FOREIGN KEY (`pid`)
    REFERENCES `ssafytrip`.`plan` (`pid`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`jjim`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`jjim` (
  `jid` INT NOT NULL AUTO_INCREMENT,
  `aid` INT NOT NULL,
  `user_id` VARCHAR(45) NOT NULL,
  `gugun_code` INT NOT NULL,
  `sido_code` INT NOT NULL,
  PRIMARY KEY (`jid`),
  INDEX `fk_jjim_aid` (`aid` ASC) VISIBLE,
  INDEX `fk_jjim_user` (`user_id` ASC) VISIBLE,
  INDEX `fk_jjim_gugun` (`gugun_code` ASC) VISIBLE,
  INDEX `fk_jjim_sido` (`sido_code` ASC) VISIBLE,
  CONSTRAINT `fk_jjim_aid`
    FOREIGN KEY (`aid`)
    REFERENCES `ssafytrip`.`attraction_info` (`content_id`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_jjim_gugun`
    FOREIGN KEY (`gugun_code`)
    REFERENCES `ssafytrip`.`gugun` (`gugun_code`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_jjim_sido`
    FOREIGN KEY (`sido_code`)
    REFERENCES `ssafytrip`.`sido` (`sido_code`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_jjim_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `ssafytrip`.`user` (`user_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
AUTO_INCREMENT = 7
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`mtti_content_type`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`mtti_content_type` (
  `mtti_type` VARCHAR(10) NOT NULL,
  `content_type_ids` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`mtti_type`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`review_img`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`review_img` (
  `fid` INT NOT NULL AUTO_INCREMENT,
  `rid` INT NOT NULL,
  `savedPath` VARCHAR(100) NULL DEFAULT NULL,
  PRIMARY KEY (`fid`),
  INDEX `rid_idx` (`rid` ASC) VISIBLE,
  CONSTRAINT `fk_review_img_review`
    FOREIGN KEY (`rid`)
    REFERENCES `ssafytrip`.`review` (`rid`)
    ON DELETE CASCADE,
  CONSTRAINT `rid`
    FOREIGN KEY (`rid`)
    REFERENCES `ssafytrip`.`review` (`rid`))
ENGINE = InnoDB
AUTO_INCREMENT = 17
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `ssafytrip`.`review_like`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ssafytrip`.`review_like` (
  `rid` INT NOT NULL,
  `user_id` VARCHAR(40) NOT NULL,
  PRIMARY KEY (`rid`, `user_id`),
  INDEX `user_id_idx` (`user_id` ASC) VISIBLE,
  CONSTRAINT `fk_review_like_review`
    FOREIGN KEY (`rid`)
    REFERENCES `ssafytrip`.`review` (`rid`)
    ON DELETE CASCADE,
  CONSTRAINT `fk_review_like_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `ssafytrip`.`user` (`user_id`)
    ON DELETE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
