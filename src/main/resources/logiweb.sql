create table if not exists cargo
(
	id bigserial not null
		constraint cargo_pkey
			primary key,
	name varchar(50) not null,
	weight integer not null,
	status cargo_status not null
);

alter table cargo owner to chpok;

create table if not exists customer_order
(
	id bigserial not null
		constraint customer_order_pkey
			primary key,
	is_completed boolean default false,
	truck_id bigint
);

alter table customer_order owner to chpok;

create table if not exists location
(
	id bigserial not null
		constraint location_pkey
			primary key,
	name varchar(255)
);

alter table location owner to chpok;

create table if not exists waypoint
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

create table if not exists truck
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
				on update cascade
);

alter table truck owner to chpok;

create table if not exists driver
(
	id bigserial not null
		constraint driver_pkey
			primary key,
	first_name varchar(30) not null,
	last_name varchar(30) not null,
	month_worked_hours smallint default 0,
	status driver_status default 'RESTING'::driver_status,
	truck_id bigint
		constraint driver_truck__fk
			references truck
				on update cascade,
	location_id bigint
		constraint driver_location__fk
			references location
				on update cascade,
	order_id bigint
		constraint driver_customer_order__fk
			references customer_order
				on update cascade
);

alter table driver owner to chpok;

create table if not exists users
(
	id bigserial not null
		constraint users_pkey
			primary key,
	username varchar(30) not null,
	password varchar(60) not null,
	role user_role not null,
	driver_id bigint
		constraint users_driver_id_fkey
			references driver
);

alter table users owner to chpok;

