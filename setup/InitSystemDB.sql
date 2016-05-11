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
