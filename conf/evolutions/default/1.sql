# --- !Ups
CREATE TABLE artists (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL,
  image_url VARCHAR(255)
);

# --- !Downs

DROP TABLE artists;
