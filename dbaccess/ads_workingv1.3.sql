

CREATE DATABASE userhistory;
\c userhistory;

CREATE SCHEMA myapp;

CREATE TABLE myapp.user_master (
    user_id SERIAL PRIMARY KEY,
    user_unique_id character varying(1000) NOT NULL,
    status integer NOT NULL,
    modified_by integer,
    modified_at text 
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
    location_searched_at text,
	FOREIGN KEY(user_id) REFERENCES myapp.user_master(user_id),
	FOREIGN KEY(place_id) REFERENCES myapp.place_master(place_id)
);

-- Indianapolis-KIND
INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'KIND', 39.791, -86.148,1 );
--Chicago-KLOT
INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'KLOT', 41.88, -87.623,1 );
--Los Angels-KVTX
INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'KVTX', 34.052, -118.24,1 );

-- Washington DC-KLWX
INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'KLWX', 38.889, -77.03,1 );
--Dallas  - KFWS
INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'KFWS', 32.779, -96.80,1 );
--Boston - KBOX
INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'KBOX', 42.361, -71.057,1 );

-- San Francisco  - KMUX
INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'KMUX', 37.733, -122.446,1 );
--New York  - KOKX
INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'KOKX', 40.730, -73.935,1 );
--Miami  -  KAMX
INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'KAMX', 25.76, -80.191,1 );

-- Raleigh  - KRAX
INSERT INTO myapp.place_master ( place_name, lat,logi, status ) values ( 'KRAX', 35.78, -78.644,1 );

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
    location_searched_at text -- timestamp without time zone
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
	location_searched_at text -- timestamp without time zone
	) as
$$
   
   SELECT 
		p.place_name,
		h.data_link , 
		h.searched_time,
		h.location_searched_at 
	from myapp.history_master h 
	JOIN myapp.user_master u 
	on h.user_id = u.user_id 
	JOIN myapp.place_master p 
	on p.place_id = h.place_id
	where u.user_unique_id = unique_id;
   
$$ language sql;

CREATE OR REPLACE FUNCTION myapp.InsertUser ( userUniqueId text, userCreatedAt text)
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
			userCreatedAt
		);
		return true;
	END;
$$ language plpgsql;



CREATE OR REPLACE FUNCTION myapp.InsertUserSearchRecord ( userUniqueId text, placeName text, dataLink text, searchedTime text, locationSearchedAt text)
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
			created_by , searched_time, location_searched_at
		)
		VALUES
		(
		(SELECT  U1.user_id from myapp.user_master U1 where U1.user_unique_id = userUniqueId),
		( SELECT P.place_id from myapp.place_master P where P.place_name = placeName ),
		dataLink, 1,
		100, searchedTime, 
		locationSearchedAt
		);
		return true;
	END;
$$ language plpgsql;

select * from myapp.GetUserSearchHistory('Snehal');


