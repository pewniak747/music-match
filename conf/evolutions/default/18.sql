# --- !Ups
CREATE INDEX CONCURRENTLY artists_name_idx ON artists(lower(name) text_pattern_ops);

# --- !Downs

DROP INDEX artists_name_idx;
