DROP TABLE IF EXISTS Member;

create table Member
(
    id integer not null,
    name varchar(255) not null,
    primary key (id)
);

DROP TABLE IF EXISTS urls;

CREATE TABLE urls
(
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  original_url VARCHAR(255) NOT NULL,
  short_url VARCHAR(255) NOT NULL,
  access_count BIGINT NOT NULL DEFAULT 0,
  created_at TIMESTAMP NOT NULL,
  updated_at TIMESTAMP NOT NULL
);