# --- !Ups
ALTER TABLE scrobbles ADD user_id integer NOT NULL;

# --- !Downs
ALTER TABLE scrobbles DROP COLUMN user_id;
