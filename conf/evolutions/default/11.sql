# --- !Ups
ALTER TABLE users ADD created_at timestamp NOT NULL DEFAULT current_timestamp;

# --- !Downs
ALTER TABLE users DROP COLUMN created_at;
