create type dblink_pkey_results as
(
    position integer,
    colname text
);

alter type dblink_pkey_results owner to postgres;

create type truck_status as enum ('OK', 'BROKEN');

alter type truck_status owner to chpok;

create type driver_status as enum ('RESTING', 'SHIFTING', 'DRIVING', 'CARGO_WORKING');

alter type driver_status owner to chpok;

create type cargo_status as enum ('PREPARED', 'SHIPPED', 'DELIVERED');

alter type cargo_status owner to chpok;

create type waypoint_type as enum ('LOADING', 'SHIPPING');

alter type waypoint_type owner to chpok;

create type user_role as enum ('ROLE_EMPLOYEE', 'ROLE_DRIVER');

alter type user_role owner to chpok;

create type order_status as enum ('COMPLETED', 'CLOSED', 'IN_PROGRESS');

alter type order_status owner to postgres;

create table cargo
(
    id bigserial
        constraint cargo_pkey
            primary key,
    name varchar(50) not null,
    weight integer not null,
    status cargo_status not null
);

alter table cargo owner to chpok;

create table customer_order
(
    id bigserial
        constraint customer_order_pkey
            primary key,
    current_truck_id bigint,
    creation_date date default now(),
    start_date date,
    end_date date,
    status order_status
);

alter table customer_order owner to chpok;

create table location
(
    id bigserial
        constraint location_pkey
            primary key,
    name varchar(255)
);

alter table location owner to chpok;

create table waypoint
(
    id bigserial
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
            on update cascade on delete cascade,
    location_id bigint
        constraint waypoint_location__fk
            references location
            on update cascade,
    is_done boolean default false
);

alter table waypoint owner to chpok;

create table truck
(
    id bigserial
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

alter table customer_order
    add constraint customer_order_truck__fk
        foreign key (current_truck_id) references truck;

create table driver
(
    id bigserial
        constraint driver_pkey
            primary key,
    first_name varchar(30) not null,
    last_name varchar(30) not null,
    month_worked_hours smallint default 0,
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
            on update cascade,
    status driver_status default 'RESTING'::driver_status
);

alter table driver owner to chpok;

create table employee
(
    id bigserial
        constraint employee_pkey
            primary key,
    first_name varchar(30) not null,
    last_name varchar(30) not null,
    current_position varchar(100) not null
);

alter table employee owner to chpok;

create table users
(
    id bigserial
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
    id bigserial
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

create table order_report
(
    id bigserial
        constraint order_report_pkey
            primary key,
    order_creation_date date,
    order_start_date date,
    order_end_date date,
    route text,
    distance smallint,
    truck varchar(7),
    drivers text,
    order_id bigint,
    report_creation_date date
);

alter table order_report owner to postgres;


