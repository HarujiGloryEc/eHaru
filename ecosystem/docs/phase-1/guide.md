# Phase 1 — Operational Guide

## Quick Start: Local Development (No Docker)

Use this approach when you want fast iteration on backend or frontend without running the full stack.

### Prerequisites

| Tool | Required Version | Check |
|------|-----------------|-------|
| JDK | 21+ | `java -version` |
| Maven | 3.9+ | `mvn -version` |
| Node.js | 20+ | `node -version` |
| npm | 10+ | `npm -version` |
| PostgreSQL | 16 (local install or Docker) | `psql --version` |

### Start PostgreSQL (Docker, quickest option)

```bash
docker run -d \
  --name haru-postgres-local \
  -e POSTGRES_USER=haru_user \
  -e POSTGRES_PASSWORD=haru_pass \
  -e POSTGRES_DB=haru_shop \
  -p 5432:5432 \
  postgres:16
```

### Start the Backend

```bash
cd h:\work\haru-shop\work\ecosystem\backend

# Build without running tests (faster first start)
mvn clean package -DskipTests

# Run with the local Spring profile
mvn spring-boot:run -pl shop-api -Dspring-boot.run.profiles=local
```

The API starts on http://localhost:8080. Verify with:
```bash
curl http://localhost:8080/api/v1/health
```

### Start the Frontend

Open a second terminal:

```bash
cd h:\work\haru-shop\work\ecosystem\frontend

# Install dependencies (first time only)
npm install

# Start Vite dev server
npm run dev
```

The UI starts on http://localhost:3000. Vite proxies `/api` requests to `http://localhost:8080`.

### Run Tests

```bash
# Backend unit tests + coverage report
cd h:\work\haru-shop\work\ecosystem\backend
mvn test -pl shop-api

# Coverage report location:
# backend/shop-api/target/site/jacoco/index.html

# Frontend unit tests
cd h:\work\haru-shop\work\ecosystem\frontend
npm test

# Frontend coverage
npm run test:coverage

# Cypress E2E (requires both backend and frontend running)
npm run cypress:open
```

---

## Quick Start: Full Docker Stack

### Prerequisites

- Docker Desktop 4.x+ (or Docker Engine + Compose v2)
- At least 8 GB RAM allocated to Docker
- Ports 3000, 5432, 6379, 8080, 8090, 8180, 8280, 9000, 9092 free

### Steps

```bash
# 1. Navigate to the ecosystem root
cd h:\work\haru-shop\work\ecosystem

# 2. Build and start all services
docker compose -f eco.yml up -d --build

# 3. Watch startup logs (Ctrl+C to stop watching, containers keep running)
docker compose -f eco.yml logs -f

# 4. Check all services are healthy
docker compose -f eco.yml ps
```

Wait approximately 60–90 seconds for all health checks to pass. Services start in dependency order: postgres and redis first, then kafka, then backend, then frontend.

### Verify the Stack

```bash
# Backend health
curl http://localhost:8080/api/v1/health

# Expected response:
# {"success":true,"data":{"status":"UP","version":"1.0.0"},"message":"OK"}

# Frontend — open in browser
# http://localhost:3000
```

---

## Troubleshooting

### PostgreSQL Not Starting

**Symptom:** `haru-postgres` container exits immediately or stays unhealthy.

```bash
docker logs haru-postgres
```

Common causes:
- Port 5432 already in use by a local PostgreSQL installation. See Port Conflict Resolution below.
- Volume data corruption. Reset with: `docker compose -f eco.yml down -v` (destroys data).
- Insufficient disk space. Check with `docker system df`.

---

### SonarQube Fails to Start (vm.max_map_count)

**Symptom:** `haru-sonarqube` exits with `max virtual memory areas vm.max_map_count [65530] is too low`.

**Fix on Windows (Docker Desktop / WSL2):**

```bash
wsl -d docker-desktop sysctl -w vm.max_map_count=262144
```

This setting resets on Docker Desktop restart. To make it permanent, add to `%USERPROFILE%\.wslconfig`:

```ini
[wsl2]
kernelCommandLine = sysctl.vm.max_map_count=262144
```

Then restart Docker Desktop.

---

### Kafka / Zookeeper Timing Issues

**Symptom:** `haru-kafka` exits or backend fails with `org.apache.kafka.common.errors.TimeoutException`.

```bash
# Restart Kafka (Zookeeper stays running)
docker compose -f eco.yml restart kafka

# If that doesn't help, restart both
docker compose -f eco.yml restart zookeeper kafka

# View Kafka logs
docker compose -f eco.yml logs --tail=50 kafka
```

---

### Backend Fails to Connect to PostgreSQL

**Symptom:** `haru-backend` crashes with `Connection refused` or `FATAL: password authentication failed`.

Checklist:
1. Confirm postgres container is healthy: `docker compose -f eco.yml ps`
2. Verify environment variables are set in `eco.yml` (`DB_URL`, `DB_USERNAME`, `DB_PASSWORD`).
3. The backend `depends_on` postgres with `condition: service_healthy` — if postgres is slow, the backend may have started before the health check passed. Restart: `docker compose -f eco.yml restart backend`.

```bash
# Manually test DB connectivity from inside the backend container
docker compose -f eco.yml exec backend sh -c "nc -zv haru-postgres 5432"
```

---

### Frontend Shows a Blank Page

**Symptom:** http://localhost:3000 loads but the page is white or shows a JS error.

1. Open browser DevTools → Console. Look for network errors.
2. Check that `VITE_API_BASE_URL` is correct:
   - Local dev: should be `http://localhost:8080` (set in `.env`).
   - Docker: should be empty string `''` so nginx proxies (set in `.env.production`).
3. Confirm the backend is running and healthy.
4. Check nginx proxy config: `docker compose -f eco.yml exec frontend cat /etc/nginx/conf.d/default.conf`.

---

### Jenkins Initial Admin Password

```bash
docker exec haru-jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

Open http://localhost:8090 and paste the password when prompted.

---

## Recovery Procedures

### Reset a Single Service

```bash
# Stop, remove, and recreate one service (keeps volume data)
docker compose -f eco.yml stop backend
docker compose -f eco.yml rm -f backend
docker compose -f eco.yml up -d --build backend
```

### Wipe and Restart the Whole Stack

**WARNING: this deletes all persisted data including the database.**

```bash
cd h:\work\haru-shop\work\ecosystem

# Stop everything and remove containers + volumes
docker compose -f eco.yml down -v

# Remove built images (optional, forces full rebuild)
docker compose -f eco.yml down --rmi local -v

# Start fresh
docker compose -f eco.yml up -d --build
```

### Back Up PostgreSQL Data

```bash
# Dump the database to a file on your host
docker exec haru-postgres pg_dump -U haru_user haru_shop > backup_$(Get-Date -Format "yyyyMMdd_HHmmss").sql

# Restore from a dump
docker exec -i haru-postgres psql -U haru_user haru_shop < backup_20260609_120000.sql
```

For a binary backup (faster restore on large databases):

```bash
docker exec haru-postgres pg_dump -U haru_user -Fc haru_shop > haru_shop.dump
docker exec -i haru-postgres pg_restore -U haru_user -d haru_shop < haru_shop.dump
```

---

## Port Conflict Resolution

If a port is already in use on your host you will see an error like:
`Bind for 0.0.0.0:5432 failed: port is already allocated.`

### Find What Is Using the Port (Windows)

```powershell
# Replace 5432 with the conflicting port
netstat -ano | findstr :5432
# Note the PID in the last column, then:
tasklist /fi "PID eq <PID>"
```

### Option A: Stop the Conflicting Process

Stop your local PostgreSQL service (if installed):
```powershell
Stop-Service postgresql*
```

### Option B: Change the Host Port in eco.yml

Edit `eco.yml` and change the left side of the port mapping:

```yaml
# Example: move postgres to host port 5433
ports:
  - "5433:5432"
```

Then update any local connection strings to use the new port. No other Docker services are affected because they connect on the internal Docker network using container ports.

### Default Port Assignments

| Service | Host Port |
|---------|-----------|
| postgres | 5432 |
| redis | 6379 |
| kafka | 9092 |
| backend | 8080 |
| jenkins | 8090 |
| akhq | 8180 |
| keycloak | 8280 |
| sonarqube | 9000 |
| frontend | 3000 |
