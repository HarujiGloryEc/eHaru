-- Baseline migration: Haru Shop schema initialization
-- This migration establishes the schema baseline.
-- Domain tables will be added in subsequent migrations.

CREATE TABLE IF NOT EXISTS schema_info (
    id          SERIAL PRIMARY KEY,
    version     VARCHAR(50)  NOT NULL,
    description VARCHAR(255) NOT NULL,
    applied_at  TIMESTAMP    NOT NULL DEFAULT NOW()
);

INSERT INTO schema_info (version, description)
VALUES ('1.0.0', 'Phase 1 baseline');
