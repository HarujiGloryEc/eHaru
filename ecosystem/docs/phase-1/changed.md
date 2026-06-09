# Phase 1 — Change Log

## Created

### Root Infrastructure
- `ecosystem/` — top-level project directory
- `ecosystem/eco.yml` — Docker Compose orchestrator: 11 services, `haru-net` bridge network, named volumes, health checks, dependency ordering

### Backend — Maven Multi-Module Project
- `backend/pom.xml` — Parent POM: Spring Boot 3.3.5 BOM, Java 21, `shop-api` module declaration
- `backend/shop-api/pom.xml` — Module POM: all runtime and test dependencies, Jacoco plugin (LINE ≥ 80%, BRANCH ≥ 70%), Checkstyle plugin, Flyway, SpringDoc OpenAPI
- `backend/shop-api/Dockerfile` — Multi-stage build: `eclipse-temurin:21-jdk` compile stage → `eclipse-temurin:21-jre` runtime stage
- `backend/shop-api/checkstyle.xml` — Checkstyle ruleset based on Google Java Style

### Backend — Application Source
- `ShopApiApplication.java` — `@SpringBootApplication` entry point
- `config/OpenApiConfig.java` — SpringDoc `OpenAPI` bean with project title, version, and server description
- `shared/exception/ApiError.java` — Error response envelope: HTTP status, message, timestamp, field-level errors map
- `shared/exception/ResourceNotFoundException.java` — Semantic 404 exception, extends `RuntimeException`
- `shared/exception/GlobalExceptionHandler.java` — `@RestControllerAdvice`: handles `ResourceNotFoundException`, `MethodArgumentNotValidException`, and generic `Exception`
- `shared/response/ApiResponse.java` — Generic success envelope `ApiResponse<T>`: `success`, `data`, `message` fields with static factory methods

### Backend — Health Feature (Clean Architecture example)
- `health/domain/HealthStatus.java` — Domain record: `status`, `timestamp`, `version` (no Spring imports)
- `health/application/HealthService.java` — Builds and returns a `HealthStatus`; reads version from `application.yml`
- `health/presentation/HealthController.java` — `GET /api/v1/health` → `ApiResponse<HealthStatus>` with `@Tag` annotation for Swagger grouping

### Backend — Configuration Files
- `resources/application.yml` — Base config: server port 8080, datasource placeholders, Flyway enabled, logging levels
- `resources/application-local.yml` — Local profile: `jdbc:postgresql://localhost:5432/haru_shop`
- `resources/application-docker.yml` — Docker profile: `jdbc:postgresql://haru-postgres:5432/haru_shop`
- `resources/db/migration/V1__init.sql` — Flyway baseline: `uuid-ossp` extension, `schema_version` audit table

### Backend — Tests
- `ShopApiApplicationTests.java` — Spring context loads (`@SpringBootTest` smoke test)
- `health/application/HealthServiceTest.java` — Unit test: `HealthService.getHealth()` returns `HealthStatus` with `"UP"` status
- `health/presentation/HealthControllerTest.java` — `@WebMvcTest`: `GET /api/v1/health` returns HTTP 200, `success: true`
- `shared/exception/GlobalExceptionHandlerTest.java` — `@WebMvcTest`: `ResourceNotFoundException` maps to 404; `MethodArgumentNotValidException` maps to 400 with error details

### Frontend — Project Scaffolding
- `frontend/package.json` — Dependencies: React 18, Vite 5, TypeScript, MUI v5, Redux Toolkit, React Router v6, Axios, Tailwind CSS
- `frontend/vite.config.ts` — Vite: port 3000, `/api` proxy to `http://localhost:8080`, path aliases
- `frontend/tsconfig.json` — TypeScript strict mode, `baseUrl`, `paths` aliases
- `frontend/jest.config.ts` — Jest with `jsdom` environment, coverage thresholds: statements/lines/branches ≥ 80%
- `frontend/babel.config.cjs` — Babel preset-env + preset-react + preset-typescript for Jest transforms
- `frontend/cypress.config.ts` — Cypress E2E config: `baseUrl: http://localhost:3000`
- `frontend/tailwind.config.ts` — Content glob, theme extension (matches MUI breakpoints)
- `frontend/postcss.config.js` — `tailwindcss` + `autoprefixer`
- `frontend/.eslintrc.cjs` — `react-hooks` + `@typescript-eslint/recommended` rules
- `frontend/.prettierrc` — 2-space indent, single quotes, trailing commas: `"all"`
- `frontend/Dockerfile` — Multi-stage: `node:20-alpine` build → `nginx:alpine` serve
- `frontend/nginx.conf` — SPA `try_files` fallback, `/api` location proxy to `backend:8080`
- `frontend/.env` — `VITE_API_BASE_URL=http://localhost:8080`
- `frontend/.env.local` — Local override (git-ignored)
- `frontend/.env.production` — `VITE_API_BASE_URL=''` (nginx handles proxying)

### Frontend — Application Source
- `src/main.tsx` — React root: `<Provider store>` + `<BrowserRouter>` + `<App />`
- `src/App.tsx` — Top-level component: renders `<AppRouter />`
- `src/index.css` — Tailwind `@base`, `@components`, `@utilities` directives
- `src/router/AppRouter.tsx` — React Router v6 route table: `/` → `AppLayout` → `DashboardPage`
- `src/shared/api/axiosClient.ts` — Axios instance: `baseURL` from `VITE_API_BASE_URL`, 10-second timeout, request interceptor for Authorization header
- `src/shared/store/store.ts` — Redux `configureStore`, exports `RootState` and `AppDispatch` types
- `src/shared/store/rootReducer.ts` — `combineReducers` (empty in Phase 1, extended per feature in Phase 2+)
- `src/shared/types/api.types.ts` — TypeScript interfaces: `ApiResponse<T>`, `ApiError`, `PaginatedResponse<T>` (mirrors backend envelopes)
- `src/shared/components/layout/AppLayout.tsx` — MUI `AppBar` + responsive `Drawer` navigation shell, `<Outlet />` for nested routes
- `src/shared/components/layout/AppLayout.test.tsx` — RTL: AppBar renders, nav links present
- `src/features/dashboard/DashboardPage.tsx` — Dashboard placeholder: calls `GET /api/v1/health` on mount, renders status card
- `src/features/dashboard/DashboardPage.test.tsx` — RTL: page renders heading without crash

### Infrastructure Service Configs
- `postgres/init/01_create_db.sql` — Creates `haru_shop` database and `haru_user` role if they don't exist
- `akhq/application.yml` — AKHQ `kafka.clusters` pointing to `haru-kafka:9092`
- `keycloak/realm-export.json` — `haru-shop` realm: confidential client `haru-api`, roles (`USER`, `ADMIN`), default scopes
- `sonarqube/sonar.properties` — `sonar.projectKey=haru-shop`, sources, tests, coverage report paths
- `jenkins/Dockerfile` — Jenkins LTS base + JDK 21 + Maven 3.9 + Node 20 + Docker CLI (for running pipeline stages)
- `jmeter/Dockerfile` — Apache JMeter 5.6.3 on `alpine:3.19`, `ENTRYPOINT` configured for running `.jmx` test plans

---

## Deviations from Plan

- **Waves batched:** Waves B2–B6 (backend) and F2–F9 (frontend) were completed within the B1/F1 implementation passes respectively. The final outcome matches the plan; only the wave granularity differs.
- **Test execution deferred:** Java (Maven) and Node.js are not available in the current shell environment. All test commands (`mvn test`, `npm test`, `npm run cypress:open`) must be executed by the developer locally. Coverage gates are configured and will be enforced at that time.
- **No deviations in scope or architecture** — all planned components were created as specified.

---

## Next Phase

**Phase 2: Core E-commerce Domain**

Builds on the clean architecture foundation to introduce:
- `User` entity, repository, service, and REST controller (registration, profile)
- `Store` entity, repository, service, and REST controller
- `Category` entity (hierarchical), repository, service, and REST controller
- `Product` entity (with variants), repository, service, and REST controller
- Flyway migrations `V2__users.sql` through `V5__products.sql`
- Integration tests using Testcontainers (PostgreSQL)
- Frontend: User auth flow (Keycloak), Store and Product listing pages, Redux feature slices
