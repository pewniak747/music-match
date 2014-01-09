# --- !Ups
CREATE TABLE recommendation_requests (
  id SERIAL PRIMARY KEY,
  user_id INTEGER REFERENCES users(id) ON DELETE CASCADE NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT current_timestamp
);

# --- !Downs

DROP TABLE recommendation_requests;
