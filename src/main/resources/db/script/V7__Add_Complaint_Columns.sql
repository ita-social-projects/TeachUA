ALTER TABLE complaints
    ADD COLUMN recipient_id BIGINT NOT NULL;

ALTER TABLE complaints
    ADD COLUMN is_active BOOLEAN DEFAULT true NOT NULL;

ALTER TABLE complaints
    ADD COLUMN has_answer BOOLEAN DEFAULT false NOT NULL;

ALTER TABLE complaints
    ADD COLUMN answer_text TEXT;
