CREATE TABLE categories (
    id            UUID         PRIMARY KEY DEFAULT gen_random_uuid(),
    store_id      UUID         NOT NULL,
    parent_id     UUID,
    name          VARCHAR(255) NOT NULL,
    slug          VARCHAR(255) NOT NULL,
    description   TEXT,
    display_order INT          NOT NULL DEFAULT 0,
    is_active     BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    updated_at    TIMESTAMP    NOT NULL DEFAULT NOW(),
    CONSTRAINT uq_categories_store_slug UNIQUE (store_id, slug),
    CONSTRAINT fk_categories_store      FOREIGN KEY (store_id)  REFERENCES stores (id)     ON DELETE CASCADE,
    CONSTRAINT fk_categories_parent     FOREIGN KEY (parent_id) REFERENCES categories (id) ON DELETE SET NULL
);
