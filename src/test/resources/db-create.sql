drop table if exists quotes;
create table quotes
(
    id    int auto_increment,
    quote varchar(255)
);

drop table if exists users;
create table users
(
    marsid  int primary key not null,
    name    varchar(255) not null,
    contactid int auto_increment not null
)