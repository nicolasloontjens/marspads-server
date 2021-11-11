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
    name    varchar(255) not null
);

drop table if exists marsidcontactid;
create table marsidcontactid
(
    marsid int,
    contactid int auto_increment not null
);


drop table if exists usercontacts;
create table usercontacts
(
    marsid int not null,
    contactid int not null,
    unique(marsid, contactid)
);

drop table if exists chatmessages;
create table chatmessages
(
    chatid int not null,
    marsid int not null,
    content varchar(10000) not null,
    time_sent timestamp not null
);

drop table if exists chats;
create table chats
(
    chatid int not null primary key auto_increment,
    marsid1 int not null,
    marsid2 int not null
);