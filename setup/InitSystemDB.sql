/**
 * Author:  buptsse-zero <GGGZ-1101-28@Live.cn>
 * Created: May 4, 2016
 */

DROP DATABASE IF EXISTS SubwayTicketDB;
CREATE DATABASE SubwayTicketDB;
USE SubwayTicketDB;

CREATE TABLE Account(
  PhoneNumber VARCHAR(20) PRIMARY KEY,
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



