--drop table if exists endpoints_hits CASCADE;
CREATE TABLE IF NOT EXISTS endpoints_hits (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    app VARCHAR(50) NOT NULL,
    uri VARCHAR(512) NOT NULL,
    ip VARCHAR(50) NOT NULL,
    time_stamp TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT pk_endpoint_hit PRIMARY KEY (id)
);