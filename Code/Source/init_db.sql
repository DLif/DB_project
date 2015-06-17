-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: localhost    Database: DbMySQL03
-- ------------------------------------------------------
-- Server version	5.6.24-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `AdministrativeDivision`
--

DROP TABLE IF EXISTS `AdministrativeDivision`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AdministrativeDivision` (
  `idAdministrativeDivision` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL COMMENT 'Not unique, Country and City, for example, may share the same name.',
  `Motto` varchar(45) DEFAULT NULL COMMENT 'For example, US: ''In god we trust''',
  `FoundingYear` smallint(6) DEFAULT NULL,
  `DestructionYear` smallint(6) DEFAULT NULL,
  `Population` bigint(20) DEFAULT NULL,
  `PopularityRating` int(11) NOT NULL COMMENT 'Length of wikipedia article, used to rate the popularity of entries\n',
  PRIMARY KEY (`idAdministrativeDivision`),
  KEY `popularity` (`PopularityRating`) USING BTREE COMMENT 'The popularity rating is used to sort entiries according to their popularity. More popular entries will be used in easier questions, and vise versa.',
  KEY `nameIndex` (`Name`) COMMENT 'To speed up searches by name'
) ENGINE=InnoDB AUTO_INCREMENT=642847 DEFAULT CHARSET=utf8 COMMENT='e.g. Country or a City.\nMay own a famous structure, may have political Leaders, may participate in military actions and may host military actions';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `AdministrativeDivisionLeader`
--

DROP TABLE IF EXISTS `AdministrativeDivisionLeader`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `AdministrativeDivisionLeader` (
  `idLeader` int(11) NOT NULL,
  `idAdministrativeDivision` int(11) NOT NULL,
  PRIMARY KEY (`idLeader`,`idAdministrativeDivision`) COMMENT 'NOTE: this index is also used as an index for idLeader.\nThus, only one another index is required for the foreign key idAdministrativeDivision',
  KEY `administrativeDivisionFK_index` (`idAdministrativeDivision`),
  CONSTRAINT `adminDivisionID_FK` FOREIGN KEY (`idAdministrativeDivision`) REFERENCES `AdministrativeDivision` (`idAdministrativeDivision`),
  CONSTRAINT `leaderID_FK` FOREIGN KEY (`idLeader`) REFERENCES `Leader` (`idLeader`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Leader of what administrative divisions\nNote that the same person may lead multiple cities, or a Country and a City in  different periods in time\nThats why this many to many relation is needed';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Battle`
--

DROP TABLE IF EXISTS `Battle`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Battle` (
  `idBattle` int(11) NOT NULL,
  PRIMARY KEY (`idBattle`),
  CONSTRAINT `militaryActionFK` FOREIGN KEY (`idBattle`) REFERENCES `MilitaryAction` (`idMilitaryAction`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Stores Battles, each Battle is a MilitaryAction';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `City`
--

DROP TABLE IF EXISTS `City`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `City` (
  `idCity` int(11) NOT NULL,
  `idCountry` int(11) DEFAULT NULL COMMENT 'In what Country the City is located in\nAllow nulls, because this information, although important, is sometimes missing\n',
  PRIMARY KEY (`idCity`),
  KEY `CountryFK_idx` (`idCountry`),
  CONSTRAINT `AdministrativeDivision_FK` FOREIGN KEY (`idCity`) REFERENCES `AdministrativeDivision` (`idAdministrativeDivision`),
  CONSTRAINT `Country_FK` FOREIGN KEY (`idCountry`) REFERENCES `Country` (`idCountry`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Represents cities\nA City is an AdministrativeDivision';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Construction`
--

DROP TABLE IF EXISTS `Construction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Construction` (
  `idConstruction` int(11) NOT NULL AUTO_INCREMENT,
  `idAdministrativeDivision` int(11) NOT NULL COMMENT 'City or Country the Construction is located in',
  `Name` varchar(45) NOT NULL,
  `PopularityRating` int(11) NOT NULL,
  PRIMARY KEY (`idConstruction`),
  UNIQUE KEY `Name_UNIQUE` (`Name`),
  KEY `adminDivisionFK_index` (`idAdministrativeDivision`),
  KEY `popularityIndex` (`PopularityRating`) COMMENT 'Used to quickly sort popular Constructions',
  CONSTRAINT `adminDivision` FOREIGN KEY (`idAdministrativeDivision`) REFERENCES `AdministrativeDivision` (`idAdministrativeDivision`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Famous Constructions in cities or countries: Big Ben, Eiffel tower, etc.\nEach Construction must be located inside a Country/City ';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Continent`
--

DROP TABLE IF EXISTS `Continent`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Continent` (
  `idContinent` smallint(6) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL COMMENT 'In theory should be unique, in practice yago is full of duplicate data and we cannot simply choose the correct version (may result in data loss, if other entities use rely on that duplicate data)',
  PRIMARY KEY (`idContinent`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=1291 DEFAULT CHARSET=utf8 COMMENT='Stores Continents\n';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Country`
--

DROP TABLE IF EXISTS `Country`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Country` (
  `idCountry` int(11) NOT NULL,
  `idContinent` smallint(6) DEFAULT NULL COMMENT 'Sometimes the continet is not provided in the data\nTherefore we allow NULLs',
  `idCapitalCity` int(11) DEFAULT NULL COMMENT 'Sometimes the information may be missing, therefore we allow NULLs\nDifferent Countries/Empires/Dynasties, may have had the same capital cities at different periods of time (thus not unique)',
  `idCurrency` int(11) DEFAULT NULL COMMENT 'official Currency used in Country',
  PRIMARY KEY (`idCountry`),
  KEY `Continent_idx` (`idContinent`),
  KEY `Currency_idx` (`idCurrency`),
  KEY `capital_idx` (`idCapitalCity`),
  CONSTRAINT `Continent` FOREIGN KEY (`idContinent`) REFERENCES `Continent` (`idContinent`),
  CONSTRAINT `Currency` FOREIGN KEY (`idCurrency`) REFERENCES `Currency` (`idCurrency`),
  CONSTRAINT `capital` FOREIGN KEY (`idCapitalCity`) REFERENCES `City` (`idCity`),
  CONSTRAINT `parent` FOREIGN KEY (`idCountry`) REFERENCES `AdministrativeDivision` (`idAdministrativeDivision`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Represents countries\nA Country is an AdministrativeDivision';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Currency`
--

DROP TABLE IF EXISTS `Currency`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Currency` (
  `idCurrency` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL COMMENT 'In theory should be unique, in practice yago is full of duplicate data and we cannot simply choose the correct version (may result in data loss, if other entities use rely on that duplicate data)',
  PRIMARY KEY (`idCurrency`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=22632 DEFAULT CHARSET=utf8 COMMENT='Money/Currency types';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Language`
--

DROP TABLE IF EXISTS `Language`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Language` (
  `idLanguage` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL,
  PRIMARY KEY (`idLanguage`),
  UNIQUE KEY `Name_UNIQUE` (`Name`)
) ENGINE=InnoDB AUTO_INCREMENT=114975 DEFAULT CHARSET=utf8 COMMENT='Stores Languages';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `LanguagesInCountries`
--

DROP TABLE IF EXISTS `LanguagesInCountries`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `LanguagesInCountries` (
  `idLanguage` int(11) NOT NULL,
  `idCountry` int(11) NOT NULL,
  PRIMARY KEY (`idLanguage`,`idCountry`),
  KEY `CountryID_idx` (`idCountry`),
  CONSTRAINT `CountryID_FK` FOREIGN KEY (`idCountry`) REFERENCES `Country` (`idCountry`),
  CONSTRAINT `LanguageID_FK` FOREIGN KEY (`idLanguage`) REFERENCES `Language` (`idLanguage`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Official launguages of countries';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `Leader`
--

DROP TABLE IF EXISTS `Leader`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Leader` (
  `idLeader` int(11) NOT NULL AUTO_INCREMENT,
  `FullName` varchar(45) NOT NULL,
  `BornIn` int(11) DEFAULT NULL COMMENT 'In what AdministrativeDivision this Leader was born',
  `DiedIn` int(11) DEFAULT NULL COMMENT 'In what AdministrativeDivision this Leader died',
  `PopularityRating` int(11) NOT NULL COMMENT 'Wiki article length, used to sort Leaders according to their popularity',
  `BirthYear` smallint(6) DEFAULT NULL,
  `BirthMonth` tinyint(4) DEFAULT NULL,
  `BirthDay` tinyint(4) DEFAULT NULL,
  `DeathYear` smallint(6) DEFAULT NULL,
  PRIMARY KEY (`idLeader`),
  KEY `bornInFK_idx` (`BornIn`),
  KEY `diedInFK_idx` (`DiedIn`),
  KEY `popularityIndex` (`PopularityRating`) COMMENT 'Used to quickly sort popular Leaders',
  KEY `leaderName` (`FullName`) USING BTREE COMMENT 'To speedup name lookups',
  CONSTRAINT `bornIn` FOREIGN KEY (`BornIn`) REFERENCES `AdministrativeDivision` (`idAdministrativeDivision`),
  CONSTRAINT `diedIn` FOREIGN KEY (`DiedIn`) REFERENCES `AdministrativeDivision` (`idAdministrativeDivision`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Leaders of AdministrativeDivisions, e.g. presidents and mayors';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MilitaryAction`
--

DROP TABLE IF EXISTS `MilitaryAction`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MilitaryAction` (
  `idMilitaryAction` int(11) NOT NULL AUTO_INCREMENT,
  `Name` varchar(45) NOT NULL COMMENT 'Should be preferable label',
  `Year` smallint(6) DEFAULT NULL COMMENT 'Integer type to support years before 1000 AD, use negative values for BC years',
  `Month` tinyint(4) DEFAULT NULL,
  `Day` tinyint(4) DEFAULT NULL,
  `PopularityRating` int(11) NOT NULL COMMENT 'Wiki article length',
  PRIMARY KEY (`idMilitaryAction`),
  UNIQUE KEY `Name_UNIQUE` (`Name`),
  KEY `popularity` (`PopularityRating`) USING BTREE COMMENT 'Quickly sort popular military actions'
) ENGINE=InnoDB AUTO_INCREMENT=87577 DEFAULT CHARSET=utf8 COMMENT='Stores military action records: Wars, Battles, etc.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MilitaryActionLocations`
--

DROP TABLE IF EXISTS `MilitaryActionLocations`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MilitaryActionLocations` (
  `idMilitaryAction` int(11) NOT NULL,
  `idAdministrativeDivision` int(11) NOT NULL,
  PRIMARY KEY (`idMilitaryAction`,`idAdministrativeDivision`),
  KEY `administrativeDivisionIndex` (`idAdministrativeDivision`),
  CONSTRAINT `actionFK` FOREIGN KEY (`idMilitaryAction`) REFERENCES `MilitaryAction` (`idMilitaryAction`),
  CONSTRAINT `participantFK` FOREIGN KEY (`idAdministrativeDivision`) REFERENCES `AdministrativeDivision` (`idAdministrativeDivision`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Where the military actions occured, might be in/near cities or in countries (administrative divisions)';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `MilitaryActionParticipants`
--

DROP TABLE IF EXISTS `MilitaryActionParticipants`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `MilitaryActionParticipants` (
  `idMilitaryAction` int(11) NOT NULL,
  `idAdministrativeDivision` int(11) NOT NULL,
  PRIMARY KEY (`idMilitaryAction`,`idAdministrativeDivision`),
  KEY `administrativeDivisionFK_idx` (`idAdministrativeDivision`),
  CONSTRAINT `administrativeDivisionFK` FOREIGN KEY (`idAdministrativeDivision`) REFERENCES `AdministrativeDivision` (`idAdministrativeDivision`),
  CONSTRAINT `miltActionFK` FOREIGN KEY (`idMilitaryAction`) REFERENCES `MilitaryAction` (`idMilitaryAction`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Which countries or cities participated in what military actions';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `User`
--

DROP TABLE IF EXISTS `User`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `User` (
  `idUser` int(11) NOT NULL AUTO_INCREMENT,
  `userName` varchar(45) NOT NULL,
  `highScore` int(11) NOT NULL DEFAULT '0',
  `password` varchar(45) NOT NULL,
  PRIMARY KEY (`idUser`),
  UNIQUE KEY `UserName_UNIQUE` (`userName`),
  KEY `sortingByScore` (`highScore`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 COMMENT='Represents a registered User\nEach User has a unique Username, password, and highscore.';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `War`
--

DROP TABLE IF EXISTS `War`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `War` (
  `idWar` int(11) NOT NULL,
  PRIMARY KEY (`idWar`),
  CONSTRAINT `MilitaryActionID` FOREIGN KEY (`idWar`) REFERENCES `MilitaryAction` (`idMilitaryAction`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='Stores War records, each War is a MilitaryAction';
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2015-06-15 12:25:30
