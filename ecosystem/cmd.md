# Ecosystem Commands

## Build Images

```bash
# Backend (Spring Boot)
docker build -t haru-shop/backend:latest ./backend

# Frontend (React + nginx)
docker build -t haru-shop/frontend:latest ./frontend

# Jenkins
docker build -t haru-shop/jenkins:latest ./jenkins

# JMeter
docker build -t haru-shop/jmeter:latest ./jmeter
```

## Run Stack

```bash
# Start all services (except JMeter)
docker compose -f eco.yml up -d

# Start with JMeter (performance profile)
docker compose -f eco.yml --profile perf up -d

# Stop all
docker compose -f eco.yml down

# Stop and remove volumes
docker compose -f eco.yml down -v
```

## Service URLs

| Service    | URL                              | Credentials   |
|------------|----------------------------------|---------------|
| Backend    | http://localhost:8080/api/v1/health | —          |
| Swagger    | http://localhost:8080/swagger-ui.html | —        |
| Frontend   | http://localhost:3000            | —             |
| Keycloak   | http://localhost:8280            | admin / admin |
| SonarQube  | http://localhost:9000            | admin / admin |
| AKHQ       | http://localhost:8180            | —             |
| Jenkins    | http://localhost:8090            | (see logs)    |

## Logs

```bash
# All services
docker compose -f eco.yml logs -f

# Single service
docker compose -f eco.yml logs -f backend
docker compose -f eco.yml logs -f frontend
docker compose -f eco.yml logs -f postgres
```

## Backend Tests (local)

```bash
cd backend
mvn test -Djavax.net.ssl.trustStoreType=Windows-ROOT
mvn verify -Djavax.net.ssl.trustStoreType=Windows-ROOT
```

## Frontend (local)

```bash
cd frontend
npm install
npm run dev       # dev server at http://localhost:5173
npm test          # Jest
npm run build     # production build
```
