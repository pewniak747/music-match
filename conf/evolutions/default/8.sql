# --- !Ups
CREATE TABLE oauth2_access_tokens (
  token VARCHAR(255) NOT NULL,
  refresh_token VARCHAR(255),
  scope VARCHAR(255),
  expires_in INTEGER NOT NULL,
  created_at TIMESTAMP NOT NULL
);

# --- !Downs

DROP TABLE oauth2_access_tokens;
