# --- !Ups
ALTER TABLE scrobbles ADD created_at timestamp;

# --- !Downs
ALTER TABLE scrobbles DROP COLUMN created_at;
