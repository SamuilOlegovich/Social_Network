create TABLE spring_session (
  primary_id            CHAR(36) NOT NULL
    CONSTRAINT spring_session_pk
    PRIMARY KEY,
  session_id            CHAR(36) NOT NULL,
  creation_time         BIGINT   NOT NULL,
  last_access_time      BIGINT   NOT NULL,
  max_inactive_interval INTEGER  NOT NULL,
  expiry_time           BIGINT   NOT NULL,
--  имя пользователя генерируемое гуглом не влазит в 100 символом,
-- потому ставим больше хотя по умолчанию стоит 100
  principal_name        VARCHAR(300)
);

create UNIQUE INDEX spring_session_ix1
  ON spring_session (session_id);

create INDEX spring_session_ix2
  ON spring_session (expiry_time);

create INDEX spring_session_ix3
  ON spring_session (principal_name);


create TABLE spring_session_attributes (
  session_primary_id CHAR(36)     NOT NULL
    CONSTRAINT spring_session_attributes_fk
    REFERENCES spring_session
    ON delete CASCADE,
  attribute_name     VARCHAR(200) NOT NULL,
  attribute_bytes    BYTEA        NOT NULL,
  CONSTRAINT spring_session_attributes_pk
  PRIMARY KEY (session_primary_id, attribute_name)
);