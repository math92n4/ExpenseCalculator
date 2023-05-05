CREATE DATABASE IF NOT EXISTS expenses_db;
USE expenses_db;

CREATE TABLE IF NOT EXISTS users (
    userid   INT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255)       NOT NULL,
    email    VARCHAR(100)       NOT NULL
);

CREATE TABLE IF NOT EXISTS `groups` (
    groupid   INT PRIMARY KEY AUTO_INCREMENT,
    groupname VARCHAR(50) NOT NULL,
    groupdesc TEXT
);

CREATE TABLE IF NOT EXISTS user_group (
    userid  INT,
    groupid INT,
    FOREIGN KEY (userid) REFERENCES users (userid),
    FOREIGN KEY (groupid) REFERENCES `groups` (groupid)
);

CREATE TABLE IF NOT EXISTS expenses (
    expenseid INT PRIMARY KEY AUTO_INCREMENT,
    expense   DECIMAL(10, 2) NOT NULL,
    userid    INT,
    groupid   INT,
    FOREIGN KEY (userid) REFERENCES users (userid),
    FOREIGN KEY (groupid) REFERENCES `groups` (groupid)
);
