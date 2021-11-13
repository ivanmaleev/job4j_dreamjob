create TABLE if not exists post (
   id SERIAL PRIMARY KEY,
   name TEXT
);

create TABLE if not exists candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   filename TEXT
);

create TABLE if not exists users (
   id SERIAL PRIMARY KEY,
   name TEXT,
   email TEXT,
   password TEXT
);