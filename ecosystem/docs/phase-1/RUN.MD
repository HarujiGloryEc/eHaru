 the tests locally (Java 21 + Maven + Node 20 required):


# Backend
cd ecosystem/backend && mvn test -pl shop-api

# Frontend
cd ecosystem/frontend && npm install && npm test

# Full stack
cd ecosystem && docker compose -f eco.yml up -d


What	URL
Backend health	http://localhost:8080/api/v1/health
Swagger UI	http://localhost:8080/swagger-ui.html
Frontend	http://localhost:3000
Keycloak admin	http://localhost:8280 (admin/admin)
SonarQube	http://localhost:9000 (admin/admin)
AKHQ (Kafka UI)	http://localhost:8180
Jenkins	http://localhost:8090