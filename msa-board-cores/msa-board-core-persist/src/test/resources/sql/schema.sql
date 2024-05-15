create table `tb_test` (
	`id` BINARY(16) not null,
    `test_gender` char(1) not null,
    `test_group` integer not null,
    `state` integer not null,
    `created_date_time` datetime(6) not null,
    `updated_date_time` datetime(6) not null,
    primary key (`id`)
);