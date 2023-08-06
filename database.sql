CREATE database IF NOT EXISTS paymybuddy;

create table transaction_entity_seq
(
    next_val bigint null
);

create table user_entity
(
    balance    decimal(38, 2) null,
    email      varchar(255)   not null
        primary key,
    first_name varchar(255)   null,
    last_name  varchar(255)   null,
    password   varchar(255)   null
);

create table transaction_entity
(
    amount         decimal(38, 2) null,
    fees           decimal(38, 2) null,
    id             bigint         not null
        primary key,
    timestamp      bigint         null,
    description    varchar(255)   null,
    receiver_email varchar(255)   null,
    sender_email   varchar(255)   null,
    constraint FK4ipmtyjwl90xgg54um4e4pjof
        foreign key (receiver_email) references user_entity (email),
    constraint FKhvku0du350n8pqktyymamakus
        foreign key (sender_email) references user_entity (email)
);

create table user_entity_contacts
(
    contacts          varchar(255) null,
    user_entity_email varchar(255) not null,
    constraint FK4opc7rjfaikm1x7y557dedf23
        foreign key (user_entity_email) references user_entity (email)
);
