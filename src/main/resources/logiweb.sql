create type truck_status as enum ('OK', 'BROKEN');

alter type truck_status owner to chpok;

create type driver_status as enum ('RESTING', 'SHIFTING', 'DRIVING');

alter type driver_status owner to chpok;

create type cargo_status as enum ('PREPARED', 'SHIPPED', 'DELIVERED');

alter type cargo_status owner to chpok;

create type waypoint_type as enum ('LOADING', 'SHIPPING');

alter type waypoint_type owner to chpok;

create type user_role as enum ('ROLE_EMPLOYEE', 'ROLE_DRIVER');

alter type user_role owner to chpok;

create table cargo
(
    id bigserial not null
        constraint cargo_pkey
            primary key,
    name varchar(50) not null,
    weight integer not null,
    status cargo_status not null
);

alter table cargo owner to chpok;

create table customer_order
(
    id bigserial not null
        constraint customer_order_pkey
            primary key,
    is_completed boolean default false,
    current_truck_id bigint,
    creation_date date default now(),
    start_date date,
    end_date date
);

alter table customer_order owner to chpok;

create table location
(
    id bigserial not null
        constraint location_pkey
            primary key,
    name varchar(255)
);

alter table location owner to chpok;

create table waypoint
(
    id bigserial not null
        constraint waypoint_pkey
            primary key,
    cargo_id bigint not null
        constraint waypoint_cargo_id_fkey
            references cargo
            on update cascade on delete restrict,
    type waypoint_type not null,
    order_id bigint not null
        constraint waypoint_order_id_fkey
            references customer_order
            on update cascade on delete restrict,
    location_id bigint
        constraint waypoint_location__fk
            references location
            on update cascade,
    is_done boolean default false
);

alter table waypoint owner to chpok;

create table truck
(
    id bigserial not null
        constraint truck_pkey
            primary key,
    capacity smallint,
    drivers_shift smallint,
    reg_number varchar(255),
    status truck_status,
    location_id bigint
        constraint truck_location__fk
            references location
            on update cascade,
    current_order_id bigint
        constraint truck_customer_order__fk
            references customer_order
            on update cascade
);

alter table truck owner to chpok;

create table driver
(
    id bigserial not null
        constraint driver_pkey
            primary key,
    first_name varchar(30) not null,
    last_name varchar(30) not null,
    month_worked_hours smallint default 0,
    status driver_status default 'RESTING'::driver_status,
    current_truck_id bigint
        constraint driver_truck__fk
            references truck
            on update cascade,
    location_id bigint
        constraint driver_location__fk
            references location
            on update cascade,
    current_order_id bigint
        constraint driver_customer_order__fk
            references customer_order
            on update cascade
);

alter table driver owner to chpok;

alter table customer_order
    add constraint customer_order_truck__fk
        foreign key (current_truck_id) references truck;

create table employee
(
    id bigserial not null
        constraint employee_pkey
            primary key,
    first_name varchar(30) not null,
    last_name varchar(30) not null,
    current_position varchar(100) not null
);

alter table employee owner to chpok;

create table users
(
    id bigserial not null
        constraint users_pkey
            primary key,
    username varchar(30) not null,
    password varchar(60) not null,
    role user_role not null,
    driver_id bigint
        constraint users_driver_id_fkey
            references driver,
    employee_id bigint
        constraint users_employee_id_fkey
            references employee
);

alter table users owner to chpok;

create table location_map
(
    id bigserial not null
        constraint location_map_pkey
            primary key,
    starting_location bigint
        constraint location_map_starting_location_fkey
            references location,
    ending_location bigint
        constraint location_map_ending_location_fkey
            references location,
    distance smallint not null
);

alter table location_map owner to chpok;

