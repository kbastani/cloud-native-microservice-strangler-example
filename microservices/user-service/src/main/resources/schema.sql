DROP TABLE IF EXISTS user;
CREATE TABLE user
(
  id            BIGINT(20) PRIMARY KEY NOT NULL,
  created_at    BIGINT(40),
  last_modified BIGINT(40),
  username      VARCHAR(255)
);