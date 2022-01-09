-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ris_projekat
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ris_projekat
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ris_projekat` ;
USE `ris_projekat` ;

-- -----------------------------------------------------
-- Table `ris_projekat`.`Role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`Role` (
  `idRole` INT NOT NULL AUTO_INCREMENT,
  `naziv` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idRole`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ris_projekat`.`User`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`User` (
  `idUser` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `ime` VARCHAR(45) NOT NULL,
  `prezime` VARCHAR(45) NOT NULL,
  `idRole` INT NOT NULL,
  `enabled` TINYINT NOT NULL,
  PRIMARY KEY (`idUser`),
  INDEX `fk_User_Role1_idx` (`idRole` ASC) VISIBLE,
  UNIQUE INDEX `email_UNIQUE` (`email` ASC) VISIBLE,
  CONSTRAINT `fk_User_Role1`
    FOREIGN KEY (`idRole`)
    REFERENCES `ris_projekat`.`Role` (`idRole`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ris_projekat`.`Profesor`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`Profesor` (
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idUser`),
  INDEX `fk_Profesor_User1_idx` (`idUser` ASC) VISIBLE,
  CONSTRAINT `fk_Profesor_User1`
    FOREIGN KEY (`idUser`)
    REFERENCES `ris_projekat`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ris_projekat`.`Student`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`Student` (
  `idUser` INT NOT NULL,
  PRIMARY KEY (`idUser`),
  INDEX `fk_Student_User1_idx` (`idUser` ASC) VISIBLE,
  CONSTRAINT `fk_Student_User1`
    FOREIGN KEY (`idUser`)
    REFERENCES `ris_projekat`.`User` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ris_projekat`.`Course`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`Course` (
  `idCourse` INT NOT NULL AUTO_INCREMENT,
  `naziv` VARCHAR(145) NOT NULL,
  `opis` VARCHAR(255) NULL,
  `sadrzaj` VARCHAR(1000) NULL,
  `sifra` VARCHAR(255) NOT NULL,
  `idProfesor` INT NOT NULL,
  PRIMARY KEY (`idCourse`),
  INDEX `fk_Kurs_Profesor1_idx` (`idProfesor` ASC) VISIBLE,
  CONSTRAINT `fk_Kurs_Profesor1`
    FOREIGN KEY (`idProfesor`)
    REFERENCES `ris_projekat`.`Profesor` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ris_projekat`.`Aktivnost`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`Aktivnost` (
  `idAktivnost` INT NOT NULL AUTO_INCREMENT,
  `naziv` VARCHAR(45) NULL,
  `opis` VARCHAR(255) NULL,
  `datum` DATE NULL,
  `idCourse` INT NOT NULL,
  `maxBrPoena` INT NULL,
  INDEX `fk_Aktivnost_Kurs1_idx` (`idCourse` ASC) VISIBLE,
  PRIMARY KEY (`idAktivnost`),
  CONSTRAINT `fk_Aktivnost_Kurs1`
    FOREIGN KEY (`idCourse`)
    REFERENCES `ris_projekat`.`Course` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ris_projekat`.`Obavestenje`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`Obavestenje` (
  `idObavestenje` INT NOT NULL AUTO_INCREMENT,
  `idCourse` INT NOT NULL,
  `datum` DATE NULL,
  `sadrzaj` VARCHAR(500) NULL,
  PRIMARY KEY (`idObavestenje`),
  INDEX `fk_Obavestenje_Kurs1_idx` (`idCourse` ASC) VISIBLE,
  CONSTRAINT `fk_Obavestenje_Kurs1`
    FOREIGN KEY (`idCourse`)
    REFERENCES `ris_projekat`.`Course` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ris_projekat`.`Procitao`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`Procitao` (
  `idProcitao` INT NOT NULL AUTO_INCREMENT,
  `procitao` TINYINT NOT NULL,
  `idObavestenje` INT NOT NULL,
  `idStudent` INT NOT NULL,
  PRIMARY KEY (`idProcitao`),
  INDEX `fk_Procitao_Obavestenje1_idx` (`idObavestenje` ASC) VISIBLE,
  INDEX `fk_Procitao_Student1_idx` (`idStudent` ASC) VISIBLE,
  CONSTRAINT `fk_Procitao_Obavestenje1`
    FOREIGN KEY (`idObavestenje`)
    REFERENCES `ris_projekat`.`Obavestenje` (`idObavestenje`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Procitao_Student1`
    FOREIGN KEY (`idStudent`)
    REFERENCES `ris_projekat`.`Student` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ris_projekat`.`Ocene`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`Ocene` (
  `idOcene` INT NOT NULL AUTO_INCREMENT,
  `idStudent` INT NOT NULL,
  `Aktivnost_Profesor_idProfesor` INT NOT NULL,
  `brPoena` INT NULL,
  `Aktivnost_idAktivnost` INT NOT NULL,
  PRIMARY KEY (`idOcene`),
  INDEX `fk_Ocene_Student1_idx` (`idStudent` ASC) VISIBLE,
  INDEX `fk_Ocene_Aktivnost1_idx` (`Aktivnost_idAktivnost` ASC) VISIBLE,
  CONSTRAINT `fk_Ocene_Student1`
    FOREIGN KEY (`idStudent`)
    REFERENCES `ris_projekat`.`Student` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Ocene_Aktivnost1`
    FOREIGN KEY (`Aktivnost_idAktivnost`)
    REFERENCES `ris_projekat`.`Aktivnost` (`idAktivnost`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ris_projekat`.`Materijal`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`Materijal` (
  `idMaterijal` INT NOT NULL AUTO_INCREMENT,
  `putanja` VARCHAR(855) NOT NULL,
  `naslov` VARCHAR(545) NOT NULL,
  `idCourse` INT NOT NULL,
  `contentType` VARCHAR(45) NULL,
  PRIMARY KEY (`idMaterijal`),
  INDEX `fk_Materijal_Kurs1_idx` (`idCourse` ASC) VISIBLE,
  CONSTRAINT `fk_Materijal_Kurs1`
    FOREIGN KEY (`idCourse`)
    REFERENCES `ris_projekat`.`Course` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ris_projekat`.`Pohadja`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ris_projekat`.`Pohadja` (
  `idPohadja` INT NOT NULL AUTO_INCREMENT,
  `idStudent` INT NOT NULL,
  `idCourse` INT NOT NULL,
  PRIMARY KEY (`idPohadja`),
  INDEX `fk_Student_has_Kurs_Kurs1_idx` (`idCourse` ASC) VISIBLE,
  CONSTRAINT `fk_Student_has_Kurs_Student1`
    FOREIGN KEY (`idStudent`)
    REFERENCES `ris_projekat`.`Student` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Student_has_Kurs_Kurs1`
    FOREIGN KEY (`idCourse`)
    REFERENCES `ris_projekat`.`Course` (`idCourse`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `ris_projekat`.`Role`
-- -----------------------------------------------------
START TRANSACTION;
USE `ris_projekat`;
INSERT INTO `ris_projekat`.`Role` (`idRole`, `naziv`) VALUES (1, 'ADMIN');
INSERT INTO `ris_projekat`.`Role` (`idRole`, `naziv`) VALUES (2, 'PROFESOR');
INSERT INTO `ris_projekat`.`Role` (`idRole`, `naziv`) VALUES (3, 'STUDENT');

COMMIT;

USE `ris_projekat`;

DELIMITER $$
USE `ris_projekat`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ris_projekat`.`User_AFTER_INSERT` AFTER INSERT ON `User` FOR EACH ROW
BEGIN
	IF (NEW.idRole = 2) THEN
		INSERT INTO `ris_projekat`.`Profesor` VALUES(NEW.idUser);
	ELSEIF (NEW.idRole = 3) THEN
		INSERT INTO `ris_projekat`.`Student` VALUES(NEW.idUser);
	END IF;
END$$

USE `ris_projekat`$$
CREATE DEFINER = CURRENT_USER TRIGGER `ris_projekat`.`User_BEFORE_DELETE` BEFORE DELETE ON `User` FOR EACH ROW
BEGIN
	IF (OLD.idRole = 2) THEN
		DELETE FROM `ris_projekat`.`Profesor` WHERE `Profesor`.idUser = OLD.idUser;
	ELSEIF (OLD.idRole = 3) THEN
		DELETE FROM `ris_projekat`.`Student` WHERE `Student`.idUser = OLD.idUser;
	END IF;
END$$


DELIMITER ;
