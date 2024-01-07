DROP DATABASE IF EXISTS muscle_hut;
CREATE DATABASE IF NOT EXISTS muscle_hut;
USE muscle_hut;

DROP TABLE IF EXISTS user;
CREATE TABLE IF NOT EXISTS user(
    email VARCHAR(40) PRIMARY KEY,
    name VARCHAR(20)  NOT NULL,
    password VARCHAR(20)
    );

DROP TABLE IF EXISTS employee;
CREATE TABLE IF NOT EXISTS employee(
    e_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    address VARCHAR(30),
    phone VARCHAR(10),
    email VARCHAR(30),
    salary VARCHAR(30)
    );


DROP TABLE IF EXISTS inventory;
CREATE TABLE IF NOT EXISTS inventory(
    i_id VARCHAR(10) PRIMARY KEY,
    category VARCHAR(30),
    name VARCHAR(30),
    count INT(50)
    );


DROP TABLE IF EXISTS member;
CREATE TABLE IF NOT EXISTS member(
    m_id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(20) NOT NULL,
    address varchar(20),
    phone VARCHAR(10),
    email VARCHAR(30)
    );
DROP TABLE IF EXISTS schedule;
CREATE TABLE IF NOT EXISTS schedule(
                                     s_id VARCHAR(10) PRIMARY KEY,
                                     mem_id VARCHAR(10),
                                     date DATE NOT NULL,
                                     FOREIGN KEY (mem_id) REFERENCES member(m_id) ON DELETE CASCADE ON UPDATE CASCADE
);

DROP TABLE IF EXISTS exercise;
CREATE TABLE IF NOT EXISTS exercise(
                                       code VARCHAR(10) PRIMARY KEY,
                                       description VARCHAR(20) NOT NULL,
                                       price DOUBLE NOT NULL,
                                       count_of_month INT NOT NULL
);

DROP TABLE IF EXISTS schedule_detail;
CREATE TABLE IF NOT EXISTS schedule_detail(
                                           s_id VARCHAR(10),
                                           code VARCHAR(10),
                                           count INT NOT NULL,
                                           price DOUBLE NOT NULL,
                                           FOREIGN KEY (s_id) REFERENCES schedule(s_id) ON DELETE CASCADE ON UPDATE CASCADE,
                                           FOREIGN KEY (code) REFERENCES exercise(code) ON DELETE CASCADE ON UPDATE CASCADE
);
