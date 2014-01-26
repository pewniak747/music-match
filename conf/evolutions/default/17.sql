# --- !Ups
CREATE INDEX CONCURRENTLY songs_title_idx ON songs(lower(title) text_pattern_ops);

# --- !Downs

DROP INDEX songs_title_idx;
