create type gender_enum as ENUM ('MALE', 'FEMALE');

CREATE TABLE children (
    id BIGSERIAL PRIMARY KEY,
    parent_id BIGINT NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    age SMALLINT NOT NULL CHECK (age >= 2 AND age <= 18),
    gender gender_enum NOT NULL
);

CREATE TABLE club_registrations (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT,
    child_id BIGINT,
    club_id BIGINT NOT NULL,
    registration_date TIMESTAMP(0) DEFAULT NOW() NOT NULL,
    is_approved BOOLEAN NOT NULL DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (child_id) REFERENCES children(id),
    FOREIGN KEY (club_id) REFERENCES clubs(id)
);
