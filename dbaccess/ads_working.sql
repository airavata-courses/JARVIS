

CREATE DATABASE userhistory;
\c userhistory;

CREATE SCHEMA myapp;

CREATE TABLE myapp.user_master (
    user_id SERIAL PRIMARY KEY,
    user_unique_id character varying(1000) NOT NULL,
    status integer NOT NULL,
    modified_by integer,
    modified_at timestamp without time zone
);


CREATE TABLE myapp.place_master (
    place_id SERIAL NOT NULL PRIMARY KEY,
    place_name character varying(255) NOT NULL,
    lat double precision,
    logi double precision,
    status integer NOT NULL
);

CREATE TABLE myapp.history_master (
    history_id SERIAL NOT NULL,
    user_id integer NOT NULL,
    place_id integer NOT NULL,
    data_link character varying(2000) NOT NULL,
    status integer NOT NULL,
    created_by integer,
	searched_time text,
    created_at timestamp without time zone,
	FOREIGN KEY(user_id) REFERENCES myapp.user_master(user_id),
	FOREIGN KEY(place_id) REFERENCES myapp.place_master(place_id)
);

INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'MN', 100, 200,1 );

INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'Boomington', 100, 200,1 );

INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'Shakopee', 100, 200,1 );

/*

INSERT INTO myapp.user_master (user_unique_id,status,modified_by, modified_at) values ( 'NILESH', 1 , 100, (SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE));

INSERT INTO myapp.user_master (user_unique_id,status,modified_by, modified_at) values ( 'Snehal', 1 , 100, (SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE));

INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'MN', 100, 200,1 );

INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'Boomington', 100, 200,1 );

INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'Shakopee', 100, 200,1 );


--DROP TABLE myapp.history_master;


INSERT INTO myapp.history_master ( user_id,place_id, data_link, status, created_by , searched_time, created_at )
values ( 1 , 2, 'C:temp', 1,100, 'Adata', (SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE) );

INSERT INTO myapp.history_master ( user_id,place_id, data_link, status, created_by , searched_time , created_at )
values ( 1 , 1, 'C:temp', 1, 200, 'Adata', (SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE) );


INSERT INTO myapp.history_master ( user_id,place_id, data_link, status, created_by , searched_time, created_at )
values ( 2 , 1, 'C:temp', 1, 300, 'Adata', (SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE) );

*/

select * from myapp.user_master;
select * from myapp.place_master;
select * from myapp.history_master;


CREATE OR REPLACE FUNCTION myapp.GetHistory ( )
returns table(
	history_id integer, 
	user_id integer
    ,place_id integer,
    data_link character varying(2000) ,
    status integer,
    created_by integer,
	searched_time text,
    created_at timestamp without time zone
	) as
$$
   
   SELECT h.* from 
   myapp.history_master h;
   
$$ language sql;

CREATE OR REPLACE FUNCTION myapp.GetUserSearchHistory ( unique_id text)
returns table(
	place_name text,
	data_link text,
	searched_time text,
	searched_at timestamp without time zone
	) as
$$
   
   SELECT 
		p.place_name,
		h.data_link , 
		h.searched_time,
		h.created_at 
	from myapp.history_master h 
	JOIN myapp.user_master u 
	on h.user_id = u.user_id 
	JOIN myapp.place_master p 
	on p.place_id = h.place_id
	where u.user_unique_id = unique_id order by h.created_at ;
   
$$ language sql;

CREATE OR REPLACE FUNCTION myapp.InsertUser ( userUniqueId text)
returns  boolean as
$$
	DECLARE	

	BEGIN
		IF EXISTS ( SELECT 1 from myapp.user_master where user_unique_id = userUniqueId ) THEN
			return false;
		END IF;
		
		INSERT INTO myapp.user_master 
		( 
			user_unique_id,
			status,modified_by, 
			modified_at
		) 
		values 
		(
			userUniqueId, 
			1, 
			100, 
			(SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE)
		);
		return true;
	END;
$$ language plpgsql;



CREATE OR REPLACE FUNCTION myapp.InsertUserSearchRecord ( userUniqueId text, placeName text, dataLink text, searchedTime text)
returns  boolean as
$$
	DECLARE	

	BEGIN
		IF NOT EXISTS ( SELECT 1 from myapp.user_master where user_unique_id = userUniqueId ) THEN
			return false;
		END IF;
		
		IF NOT EXISTS ( SELECT 1 from myapp.place_master where place_name = placeName ) THEN
			return false;
		END IF;
		
		INSERT INTO myapp.history_master 
		( 
			user_id,place_id, data_link, status, 
			created_by , searched_time, created_at
		) VALUES 
		((SELECT U.user_id from myapp.user_master U where U.user_unique_id = userUniqueId),
		( SELECT P.place_id from myapp.place_master P where P.place_name = placeName ),
		dataLink, 1,
		100, searchedTime, 
		(SELECT CURRENT_TIMESTAMP(0)::TIMESTAMP WITHOUT TIME ZONE)); 
		
		
		return true;
	END;
$$ language plpgsql;


/*
CREATE OR REPLACE FUNCTION myapp.InsertUserHistory ( unique_id text, place text, data_link text)
returns table(
	place_name text,
	data_link text,
	searched_at timestamp without time zone
	) as
$$
   
   SELECT 
		p.place_name,
		h.data_link , 
		h.created_at as searched_at 
	from myapp.history_master h 
	JOIN myapp.user_master u 
	on h.user_id = u.user_id 
	JOIN myapp.place_master p 
	on p.place_id = h.place_id
	where u.user_unique_id = unique_id;
   
$$ language sql;

*/