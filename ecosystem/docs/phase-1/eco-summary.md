# Phase 1 — eco.yml Service Runbook

## Service Registry

| Service | Image / Build | Host Port | Container Port | Purpose |
|---------|--------------|-----------|----------------|---------|
| postgres | `postgres:16` | 5432 | 5432 | Primary relational database |
| redis | `redis:7-alpine` | 6379 | 6379 | Cache and session store |
| zookeeper | `confluentinc/cp-zookeeper:7.6.1` | — | 2181 | Kafka coordination (internal only) |
| kafka | `confluentinc/cp-kafka:7.6.1` | 9092 | 9092 | Async message broker |
| akhq | `tchiotludo/akhq` | 8180 | 8080 | Kafka web UI |
| keycloak | `quay.io/keycloak/keycloak:24.0` | 8280 | 8080 | OAuth2 / OIDC identity provider |
| sonarqube | `sonarqube:10-community` | 9000 | 9000 | Static analysis and code quality |
| jenkins | custom build (`jenkins/Dockerfile`) | 8090 | 8080 | CI/CD automation server |
| jmeter | custom build (`jmeter/Dockerfile`) | — | — | Load testing (profile: `perf` only) |
| backend | custom build (`backend/shop-api/Dockerfile`) | 8080 | 8080 | Spring Boot REST API |
| frontend | custom build (`frontend/Dockerfile`) | 3000 | 80 | React UI served by nginx |

### Health Check Commands

| Service | Health Check |
|---------|-------------|
| postgres | `pg_isready -U haru_user -d haru_shop` |
| redis | `redis-cli ping` |
| kafka | `kafka-broker-api-versions --bootstrap-server localhost:9092` |
| backend | `curl -f http://localhost:8080/api/v1/health` |

---

## Common Commands

```bash
# Start everything
docker compose -f eco.yml up -d

# Start only core services (backend + frontend + postgres + redis)
docker compose -f eco.yml up -d postgres redis backend frontend

# Start with performance testing profile (adds jmeter)
docker compose -f eco.yml --profile perf up -d

# Stop everything (keeps volumes)
docker compose -f eco.yml down

# Stop and remove volumes — DESTRUCTIVE, wipes all persisted data
docker compose -f eco.yml down -v

# Rebuild a single service image and restart it
docker compose -f eco.yml up -d --build backend

# View streaming logs for a service
docker compose -f eco.yml logs -f backend
docker compose -f eco.yml logs -f frontend

# View last 100 lines from all services
docker compose -f eco.yml logs --tail=100

# Check running status and health of all services
docker compose -f eco.yml ps

# Execute a command inside a running container
docker compose -f eco.yml exec backend sh
docker compose -f eco.yml exec postgres psql -U haru_user -d haru_shop

# Restart a single service
docker compose -f eco.yml restart backend
```

---

## Service URLs

| Service | URL | Default Credentials |
|---------|-----|-------------------|
| Backend API (health) | http://localhost:8080/api/v1/health | — |
| Swagger UI | http://localhost:8080/swagger-ui.html | — |
| Frontend | http://localhost:3000 | — |
| Keycloak Admin | http://localhost:8280 | `admin` / `admin` |
| SonarQube | http://localhost:9000 | `admin` / `admin` |
| AKHQ (Kafka UI) | http://localhost:8180 | — |
| Jenkins | http://localhost:8090 | see initial password below |

**Jenkins initial admin password:**
```bash
docker exec haru-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

---

## Named Volumes

| Volume | Used By | Contents |
|--------|---------|----------|
| `haru-postgres-data` | postgres | All database files |
| `haru-redis-data` | redis | Redis persistence (AOF) |
| `haru-keycloak-data` | keycloak | Realm / user data |
| `haru-sonar-data` | sonarqube | SonarQube project data |
| `haru-sonar-extensions` | sonarqube | Plugins |
| `haru-sonar-logs` | sonarqube | Log files |
| `haru-jenkins-data` | jenkins | Jobs, plugins, credentials |

---

## Network

All services connect to the `haru-net` Docker bridge network. Services reference each other by service name (e.g., the backend connects to `haru-postgres:5432`, the frontend nginx proxies to `backend:8080`).

No service exposes ports to `0.0.0.0` in production except those in the port table above. Zookeeper and AKHQ's Kafka connection stay entirely internal.
