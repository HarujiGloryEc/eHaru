-- Ensure keycloak schema exists for Keycloak's DB
CREATE SCHEMA IF NOT EXISTS keycloak;

-- Ensure the haru user has full access
GRANT ALL PRIVILEGES ON DATABASE haru_shop TO haru;
GRANT ALL PRIVILEGES ON SCHEMA public TO haru;
GRANT ALL PRIVILEGES ON SCHEMA keycloak TO haru;
