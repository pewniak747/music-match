# --- !Ups
ALTER TABLE recommendation_requests ADD enqueued_at timestamp;

# --- !Downs
ALTER TABLE recommendation_requests DROP COLUMN enqueued_at;
