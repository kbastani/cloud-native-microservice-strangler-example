DROP TABLE IF EXISTS profile;

CREATE TABLE profile
(
  id            BIGINT(20) PRIMARY KEY NOT NULL AUTO_INCREMENT,
  created_at    BIGINT(20),
  last_modified BIGINT(20),
  email         VARCHAR(255),
  first_name    VARCHAR(255),
  last_name     VARCHAR(255),
  username      VARCHAR(255) UNIQUE    NOT NULL
);
CREATE INDEX UK_jwt2qo9oj3wd7ribjkymryp8s
  ON profile (username);