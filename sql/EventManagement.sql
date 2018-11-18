USE user24;

drop table if exists `user`;
drop table if exists `event`;
drop table if exists `ticket`;
drop table if exists `order`;
drop table if exists `order_ticket`;

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
    description text,
    location text,
    `date` datetime
);

create table `ticket`(
	id int not null primary key auto_increment,
    eventId int,
    price decimal(10,2),
    description text,
    amount int,
    foreign key (eventId) references `event`(id)
);

create table `order`(
	id int not null primary key auto_increment,
    userId int,
    eventId int,
    total float(10,2),
    `date` datetime,
    paymentType int,
    foreign key (userId) references `user`(id),
    foreign key (eventId) references `event`(id)
);

create table `order_ticket`(
	id int not null primary key auto_increment,
    ticketId int,
    orderId int,
    ownerId int,
    foreign key (ticketId) references `ticket`(id),
    foreign key(orderId) references `order`(id),
    foreign key(ownerId) references `user`(id)
)