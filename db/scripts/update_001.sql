create TABLE if not exists post (
   id SERIAL PRIMARY KEY,
   name TEXT,
   description TEXT,
   created timestamp
);

create TABLE if not exists candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   filename TEXT,
   city_id int references city(id),
   created timestamp
);

create TABLE if not exists users (
   id SERIAL PRIMARY KEY,
   name TEXT,
   email TEXT,
   password TEXT
);

create TABLE if not exists city (
   id SERIAL PRIMARY KEY,
   name TEXT
);