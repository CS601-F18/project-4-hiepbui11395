USE user24;

drop table if exists `ticket`;
drop table if exists `user`;
drop table if exists `event`;

create table `user`(
	id int not null primary key auto_increment,
    username varchar(30) not null,
    `password` char(128) not null,
    email varchar(320) not null,
    phoneNumber varchar(15)
);

create table `event`(
	id int not null primary key auto_increment,
    `name` varchar(50) not null,
    description text not null,
    location text not null,
    `date` datetime not null
);

create table `ticket`(
	id int not null primary key auto_increment,
    eventId int not null,
    price decimal(10,2) not null,
    description text not null,
    amount int not null,
    foreign key (eventId) references `event`(id)
);