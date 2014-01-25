# --- !Ups
ALTER TABLE taggings ADD artist_id INTEGER REFERENCES artists(id) NOT NULL;
ALTER TABLE taggings DROP COLUMN song_id;

# --- !Downs
ALTER TABLE taggings DROP COLUMN artist_id;
ALTER TABLE taggings ADD song_id INTEGER REFERENCES songs(id) NOT NULL;
