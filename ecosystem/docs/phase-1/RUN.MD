 the tests locally (Java 21 + Maven + Node 20 required):


# Backend
cd ecosystem/backend && mvn test -pl shop-api

# Frontend
cd ecosystem/frontend && npm install && npm test

# Full stack
cd ecosystem && docker compose -f eco.yml up -d