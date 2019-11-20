DROP DATABASE IF EXISTS onlineshop;
CREATE DATABASE `onlineshop`; 
USE `onlineshop`;

CREATE TABLE user (
 id  int (11) not null auto_increment,
 login varchar(50) not null,
 password varchar (50) not null collate utf8_bin,
 firstName varchar(50) not null,
 lastName varchar (50) not null,
 patronymic varchar (50),
 role ENUM ('ADMIN', 'CLIENT'),
 primary key (id),
 unique key user (login)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
 CREATE TABLE admin (
 userid int (11) not null,
 position varchar (50) not null,
 primary key (userid),
 foreign key(userid) references user(id) on delete cascade
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;

 CREATE TABLE client (
 userid int (11) not null,
 email varchar(50) not null,
 address varchar (50) not null,
 phone varchar (50) not null,
 foreign key(userid) references user(id) on delete cascade,
 primary key (userid),
 key (userid)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;

 CREATE TABLE session (
 id  int (11) not null auto_increment,
 userid int (11) not null,
 javasessionid varchar (50),
  primary key (id)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
 CREATE TABLE deposit (
 id int(11) not null auto_increment,
 clientid int (11) not null,
 deposit int default 0,
 version int (11)default 0,
 foreign key(clientid) references client(userid) on delete cascade,
 primary key (id)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
 CREATE TABLE category (
 id  int (11) not null auto_increment,
 parentid int (11),
 name varchar (50) not null,
  primary key (id),
   unique key category (name)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
 CREATE TABLE product (
 id  int (11)  not null auto_increment,
 name varchar (50) not null ,
 price int (11),
 count int (11),
  version int (11)default 0,
 primary key (id)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;

 CREATE TABLE product_category (
 id  int (11) not null auto_increment,
 productid int(11),
 categoryid int (11),
  primary key (id),
  foreign key(productid) references product(id) on delete cascade,
  foreign key(categoryid) references category(id) on delete cascade
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
 CREATE TABLE basket (
 id  int (11) not null auto_increment,
 productid int(11),
 name varchar (50) not null,
 price int (11),
 count int (11),
 clientid int (11), 
 primary key (id),
 foreign key(productid) references product(id) on delete set null
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
  
 CREATE TABLE purchases (
 id  int (11) not null auto_increment,
 productid int(11),
 name varchar (50) not null,
 count int (11),
 clientid int (11),
 price int,
 date_time timestamp default current_timestamp,
 foreign key(productid) references product(id) on delete set null,
  primary key (id)
 ) ENGINE=INNODB DEFAULT CHARSET=utf8;
 
 
 