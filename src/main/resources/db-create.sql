drop table if exists quotes;
create table quotes
(
    id    int auto_increment,
    quote varchar(255)
);

drop table if exists user;
create table user
(
    marsid  int primary key not null,
    name    varchar(255) not null,
    contactid int auto_increment not null
);

drop table if exists marsidcontactid;
create table marsidcontactid
(
    marsid int,
    contactid int auto_increment not null
);

drop table if exists usercontacts
create table usercontacts
(
    marsid int,
    contactid int
);