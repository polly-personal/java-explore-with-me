drop table if exists users, categories, events, requests, compilations, compilations_events CASCADE;

create TABLE IF NOT EXISTS users (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    email VARCHAR(254) NOT NULL,
    name VARCHAR(250) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uq_user_email UNIQUE (email)
);

create TABLE IF NOT EXISTS categories (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT pk_categories PRIMARY KEY (id),
    CONSTRAINT uq_categories_name UNIQUE (name)
);

create TABLE IF NOT EXISTS events (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    annotation VARCHAR(2000) NOT NULL,
    category_id BIGINT NOT NULL,
    description VARCHAR(7000) NOT NULL,
    event_date TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    lat REAL NOT NULL,
    lon REAL NOT NULL,
    is_paid BOOLEAN,
    participant_limit INTEGER,
    is_request_moderation BOOLEAN,
    title VARCHAR(120) NOT NULL,
    created_on TIMESTAMP WITHOUT TIME ZONE,
    initiator_id BIGINT NOT NULL,
    published_on TIMESTAMP WITHOUT TIME ZONE,
    state VARCHAR(30),
    CONSTRAINT pk_events PRIMARY KEY (id),
    CONSTRAINT fk_category_id FOREIGN KEY (category_id) REFERENCES categories(id),
    CONSTRAINT fk_initiator_id FOREIGN KEY (initiator_id) REFERENCES users(id) ON delete CASCADE
);

create TABLE IF NOT EXISTS requests (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    created TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(30) NOT NULL,
    CONSTRAINT pk_requests PRIMARY KEY (id),
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events(id),
    CONSTRAINT fk_requester_id FOREIGN KEY (requester_id) REFERENCES users(id),
    CONSTRAINT uk_event_requester UNIQUE (event_id, requester_id)
);

create TABLE IF NOT EXISTS compilations (
    id BIGINT GENERATED BY DEFAULT AS IDENTITY NOT NULL,
    is_pinned BOOLEAN NOT NULL,
    title VARCHAR(50) NOT NULL,
    CONSTRAINT pk_compilations PRIMARY KEY (id),
    CONSTRAINT uq_title UNIQUE (title)
);

create TABLE IF NOT EXISTS compilations_events (
    compilation_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    CONSTRAINT fk_compilation_id FOREIGN KEY (compilation_id) REFERENCES compilations (id),
    CONSTRAINT fk_event_id FOREIGN KEY (event_id) REFERENCES events (id),
    PRIMARY KEY (compilation_id, event_id)
);