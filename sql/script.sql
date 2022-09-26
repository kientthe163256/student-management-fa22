-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema student_management_fa22
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema student_management_fa22
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `student_management_fa22` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci ;
USE `student_management_fa22` ;

-- -----------------------------------------------------
-- Table `student_management_fa22`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_management_fa22`.`role` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `role_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 4
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_management_fa22`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_management_fa22`.`account` (
  `username` CHAR(8) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `enabled` TINYINT NOT NULL DEFAULT '1',
  `role_id` INT NOT NULL,
  `first_name` VARCHAR(45) NULL DEFAULT NULL,
  `last_name` VARCHAR(45) NULL DEFAULT NULL,
  `dob` DATE NULL DEFAULT NULL,
  `code` CHAR(8) NOT NULL,
  PRIMARY KEY (`username`),
  INDEX `fk_account_roleid_idx` (`role_id` ASC) VISIBLE,
  CONSTRAINT `fk_account_role_id`
    FOREIGN KEY (`role_id`)
    REFERENCES `student_management_fa22`.`role` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_management_fa22`.`subject`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_management_fa22`.`subject` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `subject_name` VARCHAR(45) NULL DEFAULT NULL,
  `no_credit` INT NULL DEFAULT NULL,
  `deleted` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_management_fa22`.`teacher`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_management_fa22`.`teacher` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `subject_id` INT NULL DEFAULT NULL,
  `deleted` TINYINT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_subject_id_idx` (`subject_id` ASC) VISIBLE,
  CONSTRAINT `fk_subject_id`
    FOREIGN KEY (`subject_id`)
    REFERENCES `student_management_fa22`.`subject` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_management_fa22`.`classroom`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_management_fa22`.`classroom` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `classroom_name` CHAR(6) NOT NULL,
  `current_no_student` INT NULL DEFAULT NULL,
  `no_student` INT NOT NULL,
  `deleted` TINYINT NULL DEFAULT '0',
  `class_type` VARCHAR(45) NULL DEFAULT NULL,
  `teacher_id` INT NULL DEFAULT NULL,
  PRIMARY KEY (`id`, `classroom_name`),
  INDEX `fk_classroom_teacher_id_idx` (`teacher_id` ASC) VISIBLE,
  CONSTRAINT `fk_classroom_teacher_id`
    FOREIGN KEY (`teacher_id`)
    REFERENCES `student_management_fa22`.`teacher` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 2
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_management_fa22`.`student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_management_fa22`.`student` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `academic_session` INT NULL DEFAULT NULL,
  `class_id` INT NULL DEFAULT NULL,
  `deleted` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `class_id_idx` (`class_id` ASC) INVISIBLE,
  CONSTRAINT `class_id`
    FOREIGN KEY (`class_id`)
    REFERENCES `student_management_fa22`.`classroom` (`id`))
ENGINE = InnoDB
AUTO_INCREMENT = 6
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


-- -----------------------------------------------------
-- Table `student_management_fa22`.`mark`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `student_management_fa22`.`mark` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `subject_id` INT NULL DEFAULT NULL,
  `student_id` INT NULL DEFAULT NULL,
  `grade` DECIMAL(3,1) NULL DEFAULT NULL,
  `weight` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_mark_subject_id_idx` (`subject_id` ASC) VISIBLE,
  INDEX `fk_mark_student_id_idx` (`student_id` ASC) VISIBLE,
  CONSTRAINT `fk_mark_student_id`
    FOREIGN KEY (`student_id`)
    REFERENCES `student_management_fa22`.`student` (`id`),
  CONSTRAINT `fk_mark_subject_id`
    FOREIGN KEY (`subject_id`)
    REFERENCES `student_management_fa22`.`subject` (`id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8mb4
COLLATE = utf8mb4_0900_ai_ci;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
