create table if not exists `tb_member` (
	`id` BINARY(16) not null,
    `member_username` varchar(30) not null,
    `member_password` varchar(60) not null,
    `member_name` varchar(30) not null,
    `member_gender` char(1) not null,
    `member_email` varchar(60) not null,
    `member_group` integer not null,
    `created_date_time` datetime(6) not null,
    `updated_date_time` datetime(6) not null,
    `state` integer not null,
    primary key (`id`),
    constraint `UK_MEMBER_USERNAME` unique (`member_username`)
);