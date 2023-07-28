CREATE SCHEMA IF NOT EXISTS `registroIncidentes` DEFAULT CHARACTER SET utf8 ;
USE `u614793440_triage` ;

CREATE TABLE IF NOT EXISTS `user` (
  `idUser` INT NOT NULL auto_increment,
  `username` VARCHAR(45) NOT NULL,
  `pword` VARCHAR(45) NOT NULL,
  `fullName` VARCHAR(45) NOT NULL,
  `mail` VARCHAR(70) NOT NULL,
  `userDependency` VARCHAR(45) NULL,
  `appAccess` VARCHAR(70) NULL,
  PRIMARY KEY (`idUser`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `emergency` (
  `idEmergency` INT NOT NULL AUTO_INCREMENT,
  `em_Name` VARCHAR(45) NOT NULL,
  `em_Location` VARCHAR(70) NOT NULL,
  `em_RegDate` DATETIME NOT NULL,
  `em_Lat` DOUBLE NOT NULL,
  `em_Lon` DOUBLE NOT NULL,
  `em_Alt` DOUBLE NOT NULL,
  `em_Type` VARCHAR(45) NOT NULL,
  `em_Type2` VARCHAR(45),
  `em_IsClosed` TINYINT NULL,
  `em_CloseDate` DATETIME NULL,
  `Emergencycol` VARCHAR(45) NULL,
  PRIMARY KEY (`idEmergency`))
ENGINE = InnoDB;

CREATE TABLE IF NOT EXISTS `emergency_staff` (
  `User_idUser` INT NOT NULL,
  `Emergency_idEmergency` INT NOT NULL,
  CONSTRAINT `fk_user_has_emergency_User`
    FOREIGN KEY (`user_idUser`)
    REFERENCES `user` (`idUser`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_emergency_Emergency1`
    FOREIGN KEY (`emergency_idEmergency`)
    REFERENCES `emergency` (`idEmergency`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

DROP TABLE IF EXISTS `injured`;
CREATE TABLE `injured` (
  `idPatient` int NOT NULL AUTO_INCREMENT,
  `idEm` int NOT NULL,
  `location` varchar(120) NOT NULL,
  `Color` varchar(45) NOT NULL,
  `isContaminated` tinyint NOT NULL,
  `ContType` varchar(45) NULL,
  `regUser` int NOT NULL,
  `state` varchar(30) NOT NULL,
  `regDate` datetime NOT NULL,
  `latitude` double NOT NULL,
  `longitude` double NOT NULL,
  `altitude` double NOT NULL,
  `detination` varchar(70) DEFAULT NULL,
  `ambulance` varchar(30) DEFAULT NULL,
  `bed` varchar(30) DEFAULT NULL,
  `name` varchar(70) DEFAULT NULL,
  `gender` varchar(20) DEFAULT NULL,
  `age` int DEFAULT NULL,
  `injuries` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`idPatient`)
) ENGINE=InnoDB;

CREATE TABLE IF NOT EXISTS `emergency_patients` (
  `idEmergency` INT NOT NULL,
  `idPatient` INT NOT NULL,
  CONSTRAINT `emergency`
	FOREIGN KEY (`idEmergency`)
    REFERENCES `emergency` (`idEmergency`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `patient`
    FOREIGN KEY (`idPatient`)
    REFERENCES `injured` (`idPatient`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION)
ENGINE = InnoDB;

delimiter //
CREATE TRIGGER add_patients AFTER INSERT ON injured
FOR EACH ROW
BEGIN
	DECLARE patient INT(11);
    DECLARE emergency INT(11);
    SET patient = (select idPatient from injured order by idPatient desc limit 1);
    SET emergency = (select idEm from injured order by idPatient desc limit 1);
    INSERT INTO emergency_patients(idEmergency, idPatient) VALUES (emergency, patient);
END; //
