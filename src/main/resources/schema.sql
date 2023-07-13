create table metric_data
(
    "metric_id" bigint identity,
    "metric_system" varchar(256),
    "metric_name" varchar(256),
    "metric_date" bigint,
    "metric_value" bigint,
    CONSTRAINT UC_metric UNIQUE ("metric_system", "metric_name", "metric_date")
);

insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric1', 1, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric2', 2, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric3', 3, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric4', 4, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric5', 5, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric6', 6, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric7', 7, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric8', 8, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric9', 9, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric1', 10, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric2', 11, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric3', 12, 1);
insert into metric_data( "metric_system", "metric_name", "metric_date", "metric_value") values('system001', 'metric4', 13, 1);


