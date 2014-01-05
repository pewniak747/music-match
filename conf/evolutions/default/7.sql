# --- !Ups
CREATE TABLE users (
  id SERIAL PRIMARY KEY,
  email VARCHAR(255) NOT NULL,
  encrypted_password VARCHAR(255) NOT NULL
);

# --- !Downs

DROP TABLE users;
