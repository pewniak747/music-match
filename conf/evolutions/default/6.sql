# --- !Ups
CREATE TABLE recommendations (
  id SERIAL PRIMARY KEY,
  created_at timestamp NOT NULL,
  song_id integer REFERENCES songs(id) ON DELETE CASCADE NOT NULL,
  user_id integer NOT NULL
);

# --- !Downs
DROP TABLE recommendations;
