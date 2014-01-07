# --- !Ups
CREATE TABLE tags (
  id SERIAL PRIMARY KEY,
  name VARCHAR(255) NOT NULL
);

# --- !Downs

DROP TABLE tags;
