drop table if exists achievements cascade;
drop table if exists users_achievements cascade;
drop table if exists users_dice_sets cascade;
drop table if exists stats cascade;
drop table if exists users cascade;
drop table if exists chats cascade;
drop table if exists dice_sets cascade;

create table dice_sets
(
    id           bigserial          not null primary key,
    system_name  varchar(50) unique not null,
    public_name  varchar(50) unique not null,
    cost         integer      default 10000,
    date_created timestamp(6) default now()
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
values ('billiards', 'Бильярдный', 100000);
insert into dice_sets(system_name, public_name, cost)
values ('pixel', 'Пиксельный', 100000);
insert into dice_sets(system_name, public_name, cost)
values ('poker', 'Покерный', 500000);
insert into dice_sets(system_name, public_name, cost)
values ('regression', 'Регрессивный', 500000);
insert into dice_sets(system_name, public_name, cost)
values ('secret', 'Секретный', 500000);
insert into dice_sets(system_name, public_name, cost)
values ('abstract', 'Абстрактный', 1000000);
insert into dice_sets(system_name, public_name, cost)
values ('coded', 'Кодовый', 1000000);
insert into dice_sets(system_name, public_name, cost)
values ('ghostly', 'Призрачный', 2000000);

create table users
(
    id              bigserial not null primary key,
    telegram_id     numeric   not null unique,
    telegram_handle varchar(255),
    is_bot          boolean   not null default false,
    is_alpha_tester boolean   not null default false,
    is_beta_tester  boolean   not null,
    date_created    timestamp(6)       default now()
);
create unique index users_id_uindex on users (id);

create table chats
(
    id            bigserial not null primary key,
    telegram_id   numeric   not null unique,
    is_group_chat boolean   not null,
    is_user_chat  boolean   not null,
    date_created  timestamp(6) default now()
);
create table users_dice_sets
(
    id               bigserial not null primary key,
    user_id          bigint    not null,
    available_set_id bigint    not null,
    enabled          boolean   not null default false,
    date_created     timestamp(6)       default now(),
    constraint fk_user_id foreign key (user_id) references users (id),
    constraint fk_available_set_id foreign key (available_set_id) references dice_sets (id)
);

create unique index chats_id_uindex on chats (id);
create unique index users_dice_sets_id_uindex on users_dice_sets (id);

create table stats
(
    -- keys
    id                 bigserial   not null primary key,
    user_id            bigint      not null,
    -- user profile stats
    last_combo         varchar(50) not null default '',
    current_money      bigint      not null default 0,
    max_money          bigint      not null default 0,
    points             bigint      not null default 0,
    tokens             integer     not null default 0,
    new_game_plus      integer     not null default 0,
    -- quest stats
    quests_completed   integer     not null default 0,
    common_quests      integer     not null default 0,
    rare_quests        integer     not null default 0,
    epic_quests        integer     not null default 0,
    legendary_quests   integer     not null default 0,
    -- combo stats
    high_card          integer     not null default 0,
    pair               integer     not null default 0,
    two_pair           integer     not null default 0,
    triple             integer     not null default 0,
    full_house         integer     not null default 0,
    small_straight     integer     not null default 0,
    big_straight       integer     not null default 0,
    quad               integer     not null default 0,
    poker              integer     not null default 0,
    -- dice stats
    one_played         bigint      not null default 0,
    two_played         bigint      not null default 0,
    three_played       bigint      not null default 0,
    four_played        bigint      not null default 0,
    five_played        bigint      not null default 0,
    six_played         bigint      not null default 0,
    -- other stats
    hands_played       integer     not null default 0,
    white_set_played   integer     not null default 0,
    pairs_in_a_row     integer     not null default 0,
    max_pairs_in_a_row integer     not null default 0,
    lucky_in_a_row     integer     not null default 0,
    unlucky_in_a_row   integer     not null default 0,
    max_lucky_in_a_row integer     not null default 0,
    highest_score      integer     not null default 0,
    constraint fk_user_id foreign key (user_id) references users (id)
);
create unique index stats_id_uindex on stats (id);

create table achievements
(
    id           bigserial   not null primary key,
    system_name  varchar(50) not null,
    public_name  varchar(50) not null,
    requirement  integer     not null,
    date_created timestamp(6) default now()
);
create unique index achievements_id_uindex on achievements (id);

insert into achievements(system_name, public_name, requirement)
values ('snakeEyes', 'Змеиный глаз', 111);

insert into achievements(system_name, public_name, requirement)
values ('pear', 'Груша', 56);

insert into achievements(system_name, public_name, requirement)
values ('luckyNumber', 'Счастливое число', 333);

insert into achievements(system_name, public_name, requirement)
values ('clover', 'Четырехлистный клевер', 3);

insert into achievements(system_name, public_name, requirement)
values ('avenue', 'Авеню', 62);

insert into achievements(system_name, public_name, requirement)
values ('lowStakes', 'Низкая ставка', 200);

-- По очереди сыграть комбо [Стрит, Фулл Хаус]
insert into achievements(system_name, public_name, requirement)
values ('homeRun', 'Дорога домой', 0);

insert into achievements(system_name, public_name, requirement)
values ('whiteSet', 'Белый набор', 500);

insert into achievements(system_name, public_name, requirement)
values ('bookCover', 'Обложка', 1000);

-- Бросить кости X раз
insert into achievements(system_name, public_name, requirement)
values ('handy', 'Бросок кобры', 2500);

insert into achievements(system_name, public_name, requirement)
values ('bigLeagues', 'Высшая лига', 10000);

insert into achievements(system_name, public_name, requirement)
values ('moneyMaker', 'Копилочка', 1000000);

insert into achievements(system_name, public_name, requirement)
values ('king', 'Король', 1);

insert into achievements(system_name, public_name, requirement)
values ('dicePoker', 'Покер на костях', 0);

create table users_achievements
(
    id             bigserial not null primary key,
    user_id        bigint    not null,
    achievement_id bigint    not null,
    new_game_plus  integer   not null default 0,
    date_created   timestamp(6)       default now(),
    constraint fk_user_id foreign key (user_id) references users (id),
    constraint fk_tier_id foreign key (achievement_id) references achievements (id)
);
create unique index users_achievements_id_uindex on users_achievements (id);
