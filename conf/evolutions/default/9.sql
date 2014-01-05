# --- !Ups
ALTER TABLE oauth2_access_tokens ADD user_id INTEGER REFERENCES users(id) NOT NULL;

# --- !Downs

ALTER TABLE oauth2_access_tokens DROP COLUMN user_id;
