/**
 * Author:  buptsse-zero <GGGZ-1101-28@Live.cn>
 * Created: May 4, 2016
 */

DROP DATABASE IF EXISTS SubwayTicketDB;
CREATE DATABASE SubwayTicketDB;
USE SubwayTicketDB;

CREATE TABLE Account(
  PhoneNumber VARCHAR(20) NOT NULL PRIMARY KEY,
  Password VARCHAR(20) NOT NULL,
  RegisterDate DATE NOT NULL
) DEFAULT CHARSET = utf8;

INSERT INTO Account(PhoneNumber, Password, RegisterDate)
          VALUES('123456', '123456', CURRENT_DATE);

CREATE TABLE SystemMessage(
  ReleaseTime DATETIME NOT NULL PRIMARY KEY,
  Title VARCHAR(40) NOT NULL,
  Content TEXT NOT NULL
) DEFAULT CHARSET = utf8;

INSERT INTO SystemMessage(ReleaseTime, Title, Content)
          VALUES('2016-6-7 9:03:34', 'SystemMessage1', 'This is system message 1.');

INSERT INTO SystemMessage(ReleaseTime, Title, Content)
          VALUES('2016-6-8 11:27:55', 'SystemMessage2', 'This is system message 2.');

INSERT INTO SystemMessage(ReleaseTime, Title, Content)
          VALUES('2016-6-8 22:46:07', 'SystemMessage3', 'This is system message 3.');

CREATE TABLE City(
  CityID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  CityName VARCHAR(40) NOT NULL
) DEFAULT CHARSET = utf8;

INSERT INTO City(CityID, CityName) VALUES (1, '北京');

CREATE TABLE SubwayLine(
  SubwayLineID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  CityID INT NOT NULL,
  CONSTRAINT SubwayLineCityIDFK FOREIGN KEY (CityID) REFERENCES City(CityID)
      ON UPDATE CASCADE ON DELETE CASCADE,
  SubwayLineName VARCHAR(20) NOT NULL
) DEFAULT CHARSET = utf8;

INSERT INTO SubwayLine(SubwayLineID, CityID, SubwayLineName)
      VALUES (11, 1, '一号线');

INSERT INTO SubwayLine(SubwayLineID, CityID, SubwayLineName)
      VALUES (12, 1, '二号线');

INSERT INTO SubwayLine(SubwayLineID, CityID, SubwayLineName)
      VALUES (15, 1, '五号线');

INSERT INTO SubwayLine(SubwayLineID, CityID, SubwayLineName)
      VALUES (18, 1, '八号线');

INSERT INTO SubwayLine(SubwayLineID, CityID, SubwayLineName)
      VALUES (110, 1, '十号线');

INSERT INTO SubwayLine(SubwayLineID, CityID, SubwayLineName)
      VALUES (113, 1, '十三号线');

CREATE TABLE StationMessage(
  StationMessageID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  Publisher VARCHAR(30) NOT NULL,
  ReleaseTime DATETIME NOT NULL,
  Content TEXT NOT NULL
) DEFAULT CHARSET = utf8;

CREATE TABLE SubwayStation(
  SubwayStationID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  SubwayLineID INT NOT NULL,
  CONSTRAINT SubwayStationSubwayLineIDFK FOREIGN KEY (SubwayLineID) REFERENCES SubwayLine(SubwayLineID)
      ON UPDATE CASCADE ON DELETE CASCADE,
  SubwayStationName VARCHAR(30) NOT NULL,
  Available BOOLEAN NOT NULL DEFAULT TRUE,
  StationMessageID INT,
  CONSTRAINT SubwayStationMessageIDFK FOREIGN KEY (StationMessageID) REFERENCES StationMessage(StationMessageID)
      ON UPDATE CASCADE ON DELETE CASCADE
) DEFAULT CHARSET = utf8;

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName)
      VALUES (151, 15, '天通苑北');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName)
      VALUES (152, 15, '灯市口');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName)
      VALUES (1131, 113, '五道口');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName)
      VALUES (121, 12, '安定门');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName)
      VALUES (111, 11, '五棵松');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName)
      VALUES (1101, 110, '安贞门');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName)
      VALUES (181, 18, '什刹海');

CREATE TABLE TicketPrice(
  SubwayStationAID INT NOT NULL,
  CONSTRAINT SubwayStationAIDFK FOREIGN KEY (SubwayStationAID) REFERENCES SubwayStation(SubwayStationID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  SubwayStationBID INT NOT NULL,
  CONSTRAINT SubwayStationBIDFK FOREIGN KEY (SubwayStationBID) REFERENCES SubwayStation(SubwayStationID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  PRIMARY KEY(SubwayStationAID, SubwayStationBID),
  Price FLOAT NOT NULL CHECK (Price >= 0)
);

/*Please import TicketPrice.csv into the table TicketPrice manually.*/
