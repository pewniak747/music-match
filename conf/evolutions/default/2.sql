# --- !Ups
CREATE TABLE songs (
  id SERIAL PRIMARY KEY,
  title VARCHAR(255) NOT NULL,
  artist_id integer REFERENCES artists(id) ON DELETE CASCADE
);

# --- !Downs
DROP TABLE songs;
