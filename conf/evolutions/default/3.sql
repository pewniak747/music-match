# --- !Ups
CREATE TABLE scrobbles (
  id SERIAL PRIMARY KEY,
  song_id integer REFERENCES songs(id) NOT NULL
);

# --- !Downs
DROP TABLE scrobbles;
