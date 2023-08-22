
CREATE TABLE challenge_registrations
(
    id                BIGSERIAL PRIMARY KEY,
    user_id           BIGINT,
    child_id          BIGINT,
    challenge_id      BIGINT  NOT NULL,
    registration_date TIMESTAMP(0)     DEFAULT NOW() NOT NULL,
    is_approved       BOOLEAN NOT NULL DEFAULT FALSE,
    is_active         BOOLEAN NOT NULL DEFAULT TRUE,
    comment           VARCHAR(255),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (child_id) REFERENCES children (id),
    FOREIGN KEY (challenge_id) REFERENCES challenges (id)
);
