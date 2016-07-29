/**
 * Author:  buptsse-zero <GGGZ-1101-28@Live.cn>
 * Created: May 4, 2016
 */

DROP DATABASE IF EXISTS SubwayTicketDB;
CREATE DATABASE SubwayTicketDB DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE SubwayTicketDB;

CREATE TABLE Account(
  PhoneNumber VARCHAR(20) NOT NULL PRIMARY KEY,
  Password VARCHAR(20) NOT NULL,
  RegisterDate DATE NOT NULL
);

INSERT INTO Account(PhoneNumber, Password, RegisterDate)
          VALUES('123456', '123456', CURRENT_DATE);

CREATE TABLE SystemMessage(
  ReleaseTime DATETIME NOT NULL PRIMARY KEY,
  Title VARCHAR(40) NOT NULL,
  Content TEXT NOT NULL
);

INSERT INTO SystemMessage(ReleaseTime, Title, Content)
          VALUES('2016-7-27 22:00:00', '地铁自助取票系统简介',
                 '<p>&nbsp;&nbsp;&nbsp;&nbsp;本系统是由AlohaWorld团队开发的一套地铁网络购票系统，包括网页端与Android手机端，用于参加
                  第七届中国大学生服务外包创新创业大赛。<br/>
                  &nbsp;&nbsp;&nbsp;&nbsp;乘客可以在此系统中购买地铁票，支付后即可获得取票用的提取码和相应的二维码。乘客在地铁站凭提取码或二维码即可直接取票，
                  免去排队购买临时票的麻烦。<br/>
                  &nbsp;&nbsp;&nbsp;&nbsp;本系统具有如下特色:<br/>
                  &nbsp;&nbsp;&nbsp;&nbsp;1.购票常用设置：乘客可设置一些常用地铁站和常用路线，提高购票效率。<br/>
                  &nbsp;&nbsp;&nbsp;&nbsp;2.优良的界面设计：网页端采用现代的Material Design设计，界面简洁大气。<br/>
                  &nbsp;&nbsp;&nbsp;&nbsp;3.简洁的用户交互：大容量网页减少页面跳转次数，使多个操作能尽量在一个页面内完成。
                  </p>');

INSERT INTO SystemMessage(ReleaseTime, Title, Content)
VALUES('2016-7-28 10:05:34', '地铁自助取票系统网页端Alpha版完成',
       '<p>&nbsp;&nbsp;&nbsp;&nbsp;经过一个多月的开发，网页端的Alpha版本目前已经完成，基本功能均已具备。
        </p>');

INSERT INTO SystemMessage(ReleaseTime, Title, Content)
VALUES('2016-7-29 14:41:21', 'Alpha版网页端使用测试说明-20160729',
       '<p>&nbsp;&nbsp;&nbsp;&nbsp;由于本系统只是用于参加大赛用的项目，并不是真正的商业级项目，
        因此与实际的购票系统还是会有所差距。在使用网页端时请注意如下事项：<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;1.网页端只支持现代浏览器，请使用较新的浏览器浏览网页，若您使用IE浏览器，请保证IE的版本在10(Windows8自带IE的版本)及以上。<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;2.注册时不必填写真实的手机号，只要是纯数字即可。<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;3.验证码不会真正发送到手机上，收到发送成功的提示后，输入123456即可通过验证。<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;4.支付功能只是模拟，并不会真正花钱支付。<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;5.Android手机客户端暂时还不可用，请勿下载安装。<br/>
        &nbsp;&nbsp;&nbsp;&nbsp;6.测试期间，服务器随时有可能会重置数据库，清除所有账号。<br/>
        <br/>
        &nbsp;&nbsp;&nbsp;&nbsp;使用过程中若发现问题，请报告问题至邮箱: <a href="mailto:GGGZ-1101-28@Live.cn">GGGZ-1101-28@Live.cn</a>
        </p>');

CREATE TABLE City(
  CityID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  CityName VARCHAR(40) NOT NULL
);

INSERT INTO City(CityID, CityName) VALUES (1, '北京');

CREATE TABLE SubwayLine(
  SubwayLineID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  CityID INT NOT NULL,
  CONSTRAINT SubwayLineCityIDFK FOREIGN KEY (CityID) REFERENCES City(CityID)
      ON UPDATE CASCADE ON DELETE CASCADE,
  SubwayLineName VARCHAR(20) NOT NULL
);

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
  Title VARCHAR(40) NOT NULL,
  ReleaseTime DATETIME NOT NULL,
  Content TEXT NOT NULL
);

INSERT INTO StationMessage(StationMessageID, Publisher, Title, ReleaseTime, Content)
      VALUES (1, '北京地铁', '关于永久停用天通苑北站的公告', CURRENT_TIME, '<p>天通苑北地铁站已被炸毁，该站将永久停用。</p>');

CREATE TABLE SubwayStation(
  SubwayStationID INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
  SubwayLineID INT NOT NULL,
  CONSTRAINT SubwayStationSubwayLineIDFK FOREIGN KEY (SubwayLineID) REFERENCES SubwayLine(SubwayLineID)
      ON UPDATE CASCADE ON DELETE CASCADE,
  SubwayStationName VARCHAR(30) NOT NULL,
  SubwayStationEnglishName VARCHAR(50),
  Available BOOLEAN NOT NULL DEFAULT TRUE,
  StationMessageID INT,
  CONSTRAINT SubwayStationMessageIDFK FOREIGN KEY (StationMessageID) REFERENCES StationMessage(StationMessageID)
      ON UPDATE CASCADE ON DELETE CASCADE
);

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName, SubwayStationEnglishName, Available, StationMessageID)
      VALUES (151, 15, '天通苑北', 'TianTongYuanBei', FALSE, 1);

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName, SubwayStationEnglishName)
      VALUES (152, 15, '灯市口', 'DengShiKou');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName, SubwayStationEnglishName)
      VALUES (1131, 113, '五道口', 'WuDaoKou');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName, SubwayStationEnglishName)
      VALUES (121, 12, '安定门', 'AnDingMen');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName, SubwayStationEnglishName)
      VALUES (111, 11, '五棵松', 'WuKeSong');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName, SubwayStationEnglishName)
      VALUES (1101, 110, '安贞门', 'AnZhenMen');

INSERT INTO SubwayStation(SubwayStationID, SubwayLineID, SubwayStationName, SubwayStationEnglishName)
      VALUES (181, 18, '什刹海', 'ShiChaHai');

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

CREATE TABLE TicketOrder(
  TicketOrderID VARCHAR(30) NOT NULL PRIMARY KEY,
  TicketOrderTime DATETIME NOT NULL,
  UserID VARCHAR(20) NOT NULL,
  CONSTRAINT OrderUserIDFK FOREIGN KEY (UserID) REFERENCES Account(PhoneNumber)
    ON UPDATE CASCADE ON DELETE CASCADE,
  StartStationID INT NOT NULL,
  CONSTRAINT OrderStartStationIDFK FOREIGN KEY (StartStationID) REFERENCES SubwayStation(SubwayStationID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  EndStationID INT NOT NULL,
  CONSTRAINT OrderEndStationIDFK FOREIGN KEY (EndStationID) REFERENCES SubwayStation(SubwayStationID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  TicketPrice FLOAT NOT NULL,
  ExtractAmount INT NOT NULL DEFAULT 0,
  Amount INT NOT NULL CHECK (Amount >= 1),
  Status CHAR(1) NOT NULL DEFAULT 'A',
  ExtractCode VARCHAR(15) UNIQUE,
  Comment VARCHAR(50)
);

CREATE TABLE PreferSubwayStation(
  UserID VARCHAR(20) NOT NULL,
  CONSTRAINT PreferSubwayStationUserIDFK FOREIGN KEY (UserID) REFERENCES Account(PhoneNumber)
    ON UPDATE CASCADE ON DELETE CASCADE,
  StationID INT NOT NULL,
  CONSTRAINT PreferSubwayStationStationIDFK FOREIGN KEY (StationID) REFERENCES SubwayStation(SubwayStationID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  AddTime DATETIME NOT NULL,
  PRIMARY KEY (UserID, StationID)
);

CREATE TABLE PreferRoute(
  UserID VARCHAR(20) NOT NULL,
  CONSTRAINT PreferRouteUserIDFK FOREIGN KEY (UserID) REFERENCES Account(PhoneNumber)
    ON UPDATE CASCADE ON DELETE CASCADE,
  StartStationID INT NOT NULL,
  CONSTRAINT PreferRouteStartStationIDFK FOREIGN KEY (StartStationID) REFERENCES SubwayStation(SubwayStationID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  EndStationID INT NOT NULL,
  CONSTRAINT PreferRouteEndStationIDFK FOREIGN KEY (EndStationID) REFERENCES SubwayStation(SubwayStationID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  AddTime DATETIME NOT NULL,
  PRIMARY KEY (UserID, StartStationID, EndStationID)
);

CREATE TABLE HistoryRoute(
  UserID VARCHAR(20) NOT NULL,
  CONSTRAINT HistoryRouteUserIDFK FOREIGN KEY (UserID) REFERENCES Account(PhoneNumber)
    ON UPDATE CASCADE ON DELETE CASCADE,
  StartStationID INT NOT NULL,
  CONSTRAINT HistoryRouteStartStationIDFK FOREIGN KEY (StartStationID) REFERENCES SubwayStation(SubwayStationID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  EndStationID INT NOT NULL,
  CONSTRAINT HistoryRouteEndStationIDFK FOREIGN KEY (EndStationID) REFERENCES SubwayStation(SubwayStationID)
    ON UPDATE CASCADE ON DELETE CASCADE,
  AddTime DATETIME NOT NULL,
  PRIMARY KEY (UserID, StartStationID, EndStationID)
)

