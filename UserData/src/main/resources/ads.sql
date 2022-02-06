
--CREATE DATABASE userhistory;
--\c userhistory;

CREATE SCHEMA myapp;

CREATE TABLE userhistory.myapp.user_master (
    user_id SERIAL PRIMARY KEY,
    user_unique_id character varying(1000) NOT NULL,
    status integer NOT NULL,
    modified_by integer,
    modified_at timestamp without time zone
);


CREATE TABLE userhistory.myapp.place_master (
    place_id SERIAL NOT NULL PRIMARY KEY,
    place_name character varying(255) NOT NULL,
    lat double precision,
    logi double precision,
    status integer NOT NULL
);

CREATE TABLE userhistory.myapp.history_master (
    history_id SERIAL NOT NULL,
    user_id integer NOT NULL,
    place_id integer NOT NULL,
    data_link character varying(2000) NOT NULL,
    status integer NOT NULL,
    created_by integer,
    created_at timestamp without time zone,
	FOREIGN KEY(user_id) REFERENCES userhistory.myapp.user_master(user_id),
	FOREIGN KEY(place_id) REFERENCES userhistory.myapp.place_master(place_id)
);

/*
INSERT INTO myapp.user_master (user_unique_id,status,modified_by, modified_at) values ( 'NILESH', 1 , 100, (SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE));

INSERT INTO myapp.user_master (user_unique_id,status,modified_by, modified_at) values ( 'Snehal', 1 , 100, (SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE));

INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'MN', 100, 200,1 );

INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'Boomington', 100, 200,1 );

INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'Shakopee', 100, 200,1 );


INSERT INTO myapp.history_master ( user_id,place_id, data_link, status, created_at )
values ( 1 , 2, 'C:temp', 1, (SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE) );

INSERT INTO myapp.history_master ( user_id,place_id, data_link, status, created_at )
values ( 1 , 1, 'C:temp', 1, (SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE) );


INSERT INTO myapp.history_master ( user_id,place_id, data_link, status, created_at )
values ( 2 , 1, 'C:temp', 1, (SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE) );



select * from myapp.user_master;
select * from myapp.place_master;
select * from myapp.history_master;


CREATE OR REPLACE PROCEDURE myapp.GetHistory ( unique_id text)
returns table(
	history_id integer, 
	user_id integer
    ,place_id integer,
    data_link character varying(2000) ,
    status integer,
    created_by integer,
    created_at timestamp without time zone
	) as
$$
   
   SELECT * from 
   myapp.history_master h
   where h.user_id = ( select user_id from myapp.user_master u where u.user_unique_id = unique_id );
   
$$ language sql;
*/
