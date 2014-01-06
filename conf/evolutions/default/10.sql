# --- !Ups
CREATE UNIQUE INDEX users_email_idx ON users(email);

# --- !Downs

DROP INDEX users_email_idx;
