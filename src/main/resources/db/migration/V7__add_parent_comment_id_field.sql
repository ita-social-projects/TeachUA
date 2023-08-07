ALTER TABLE feedbacks
    ADD COLUMN parent_comment_id BIGINT,
    ADD CONSTRAINT fk_parent_comment
        FOREIGN KEY (parent_comment_id)
            REFERENCES feedbacks (id);

ALTER TABLE feedbacks ALTER COLUMN rate DROP NOT NULL;
