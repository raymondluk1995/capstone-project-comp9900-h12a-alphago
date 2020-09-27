create database property_sale;

use property_sale;

create table user(
	`uid` int(6) NOT NULL auto_increment,
	`username` varchar(10) NOT NULL,
	`password` varchar(32) NOT NULL,
	`salt` varchar(6) DEFAULT NULL,
	`phone` varchar(10) NOT NULL,
	`email` varchar(50) NOT NULL,
	
	primary key(uid),
	KEY `name_index` (`username`) USING BTREE
)engine = innodb default charset = utf8;

create table payment(
	`pyid` int(6) NOT NULL AUTO_INCREMENT,
	`uid` int(6) NOT NULL,
	`card_number` varchar(25) NOT NULL,
	`expiry_date` varchar(4) NOT NULL,
	`cvv` varchar(3) NOT NULL,
	`name` varchar(50) NOT NULL,

	primary key(pyid),
	foreign key (uid) references user(uid),
	KEY `uid_index` (`uid`) USING BTREE
)engine = innodb default charset = utf8;

create table property(
	`pid` int(6) NOT NULL AUTO_INCREMENT,
	`owner` int(6) NOT NULL,
	`n_bedroom` int(3) NOT NULL,
	`n_bathroom` int(3) NOT NULL,
	`n_garage` int(3) NOT NULL,
	`auction` boolean NOT NULL,
	`public_access` boolean NOT NULL,
	`area` int(6) NOT NULL,
	
	primary key(pid),
	foreign key (owner) references user(uid),
	KEY `uid_index` (`owner`) USING BTREE
)engine = innodb default charset = utf8;

create table address(
	`pid` int(6) NOT NULL,
	`country` varchar(20) NOT NULL,
	`address1` varchar(255) NOT NULL,
	`address2` varchar(255) DEFAULT NULL,
	`postcode` varchar(10) NOT NULL,
	`city` varchar(20) NOT NULL,
	`state` varchar(20) NOT NULL,
	
	primary key(pid),
	foreign key (pid) references property(pid),
	KEY `pid_index` (`pid`) USING BTREE
)engine = innodb default charset = utf8;

create table auction(
	`aid` int(6) NOT NULL AUTO_INCREMENT,
	`pid` int(6) NOT NULL,
	`start_date` DATE NOT NULL,
	`end_date` DATE NOT NULL,
	`reserve_pirce` int(10) NOT NULL,
	`minimum_price` int(10) NOT NULL,
	`latest_bid` int(10) NOT NULL,
	`is_success` boolean NOT NULL,
	
	primary key(aid),
	foreign key (pid) references property(pid),
	KEY `aid_index` (`aid`) USING BTREE,
	KEY `pid_index` (`pid`) USING BTREE
)engine = innodb default charset = utf8;

create table rab(
	`rab_id` int(6) NOT NULL AUTO_INCREMENT,
	`aid` int(6) NOT NULL,
	`uid` int(6) NOT NULL,
	
	primary key(rab_id),
	foreign key(aid) references auction(aid),
	foreign key(uid) references user(uid)
)engine = innodb default charset = utf8;
	
create table auction_action(
	`rab_id` int(6) NOT NULL,
	`bid_price` int(10) NOT NULL,
	`bid-time` date NOT NULL,
	
	foreign key (rab_id) references rab(rab_id)
)engine = innodb default charset = utf8;

create table history(
	`uid` int(6) NOT NULL,
	`url` varchar(255) NOT NULL,
	
	foreign key (uid) references user(uid)
)engine = innodb default charset = utf8;
