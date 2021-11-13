create TABLE if not exists post (
   id SERIAL PRIMARY KEY,
   name TEXT
);

create TABLE if not exists candidate (
   id SERIAL PRIMARY KEY,
   name TEXT,
   filename TEXT
);

DELETE FROM post;

DELETE FROM candidate;