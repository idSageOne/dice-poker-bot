drop table if exists users_available_dice_sets;
drop table if exists achievements;
drop table if exists stats;
drop table if exists users;
drop table if exists dice_sets;

create table dice_sets
(
    id           bigserial    not null primary key,
    system_name  varchar(255) not null default 'white',
    public_name  varchar(255) not null default 'Белый',
    cost         integer                   default 10000,
    date_created timestamp(6)          default now()
);

insert into dice_sets(system_name, public_name, cost)
values ('white', 'Белый', 10000);
insert into dice_sets(system_name, public_name, cost)
values ('black', 'Черный', 50000);
insert into dice_sets(system_name, public_name, cost)
values ('roman', 'Римский', 50000);
insert into dice_sets(system_name, public_name, cost)
values ('calendar', 'Календарный', 100000);
insert into dice_sets(system_name, public_name, cost)
values ('brailles', 'Тактильный', 100000);
insert into dice_sets(system_name, public_name, cost)
values ('pixel', 'Пиксельный', 100000);
insert into dice_sets(system_name, public_name, cost)
values ('poker', 'Покерный', 500000);
insert into dice_sets(system_name, public_name, cost)
values ('billiards', 'Бильярдный', 500000);
insert into dice_sets(system_name, public_name, cost)
values ('regression', 'Регрессивный', 500000);
insert into dice_sets(system_name, public_name, cost)
values ('abstract', 'Абстрактный', 1000000);
insert into dice_sets(system_name, public_name, cost)
values ('coded', 'Кодовый', 5000000);

create table users
(
    id                  bigserial not null primary key,
    telegram_id         numeric   not null unique,
    telegram_handle     varchar(255),
    is_bot              boolean   not null,
    date_created        timestamp(6) default now(),
    enabled_dice_set_id bigint    not null,
    constraint fk_enabled_dice_set_id foreign key (enabled_dice_set_id) references dice_sets (id)
);
create unique index users_id_uindex on users (id);

create table users_available_dice_sets
(
    id               bigserial not null primary key,
    user_id          bigint    not null,
    available_set_id bigint    not null,
    date_created     timestamp(6) default now(),
    constraint fk_user_id foreign key (user_id) references users (id),
    constraint fk_available_set_id foreign key (available_set_id) references dice_sets (id)
);
create unique index users_available_dice_sets_id_uindex on users_available_dice_sets (id);

create table stats
(
    id                     bigserial not null primary key,
    user_id                bigint    not null,
    high_card              integer   not null default 0,
    pair                   integer   not null default 0,
    two_pair               integer   not null default 0,
    triple                 integer   not null default 0,
    full_house             integer   not null default 0,
    small_straight         integer   not null default 0,
    big_straight           integer   not null default 0,
    quad                   integer   not null default 0,
    poker                  integer   not null default 0,
    hands_played           integer   not null default 0,
    hands_played_white_set integer   not null default 0,
    hands_played_black_set integer   not null default 0,
    pairs_in_a_row         integer   not null default 0,
    points_earned          bigint    not null default 0,
    money_earned           bigint    not null default 0,
    highest_score          integer   not null default 0,
    constraint fk_user_id foreign key (user_id) references users (id)
);
create unique index stats_id_uindex on stats (id);

create table achievements
(
    id           bigserial not null primary key,
    user_id      bigint    not null,
    snake_eyes   boolean   not null default false,
    pear         boolean   not null default false,
    avenue       boolean   not null default false,
    lucky_number boolean   not null default false,
    low_stakes   boolean   not null default false,
    white_set    boolean   not null default false,
    black_set    boolean   not null default false,
    big_money    boolean   not null default false,
    bigger_money boolean   not null default false,
    millionaire  boolean   not null default false,
    king         boolean   not null default false,
    alpha        boolean   not null default false,
    dice_poker   boolean   not null default false,
    constraint fk_user_id foreign key (user_id) references users (id)
);
create unique index achievements_id_uindex on achievements (id);

