CREATE TABLE stores (
    id          UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    merchant_id UUID         NOT NULL,
    name        VARCHAR(255) NOT NULL,
    slug        VARCHAR(255) NOT NULL,
    description TEXT,
    status      VARCHAR(20)  NOT NULL DEFAULT 'ACTIVE',
    settings    TEXT,
    created_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at  TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_stores_slug    UNIQUE (slug),
    CONSTRAINT fk_stores_merchant FOREIGN KEY (merchant_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT chk_stores_status  CHECK (status IN ('ACTIVE', 'INACTIVE', 'SUSPENDED'))
);
