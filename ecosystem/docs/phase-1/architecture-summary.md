# Phase 1 — Architecture Summary

## System Overview

Haru Shop is a full-stack e-commerce platform composed of three tiers:

| Tier | Technology | Entry Point |
|------|-----------|-------------|
| Backend | Spring Boot 3.3.5 (Java 21) | `http://localhost:8080` |
| Frontend | React 18 + Vite 5 + TypeScript | `http://localhost:3000` |
| Infrastructure | Docker Compose (`eco.yml`) | 11 services on `haru-net` |

The backend exposes a REST API under `/api/v1`. The frontend proxies all `/api` requests through Vite (local dev) or nginx (Docker) to the backend. Every service runs in a shared `haru-net` bridge network so hostnames resolve by service name.

---

## Backend: Clean Architecture + Feature-Based Packages

The backend follows **Clean Architecture** with layers (domain → application → infrastructure → presentation) nested inside **feature packages**. Each feature is self-contained; cross-cutting concerns live in `shared/`.

```
org.eharu.shop/
├── shared/
│   ├── exception/          ← ApiError, ResourceNotFoundException, GlobalExceptionHandler
│   └── response/           ← ApiResponse<T>  (envelope for all responses)
├── config/
│   └── OpenApiConfig.java  ← Swagger / SpringDoc configuration
└── health/                 ← example feature
    ├── domain/
    │   └── HealthStatus.java        ← pure domain record, no framework deps
    ├── application/
    │   └── HealthService.java       ← business logic, depends on domain only
    └── presentation/
        └── HealthController.java    ← Spring MVC, depends on application layer
```

**Rules enforced by this structure:**
- `domain/` has zero Spring dependencies.
- `application/` depends on `domain/` and interfaces only.
- `presentation/` depends on `application/` and Spring MVC only.
- No layer reaches down into a sibling feature's internal layers.

New features (User, Product, Order, …) follow the same pattern: create a package, add `domain/ → application/ → presentation/` sub-packages.

---

## Frontend Structure

```
src/
├── main.tsx                  ← React root, Redux Provider, BrowserRouter
├── App.tsx                   ← top-level component, mounts AppRouter
├── index.css                 ← Tailwind base styles
├── router/
│   └── AppRouter.tsx         ← React Router v6 route definitions
├── shared/
│   ├── api/
│   │   └── axiosClient.ts    ← Axios instance, base URL from VITE_API_BASE_URL
│   ├── store/
│   │   ├── store.ts          ← Redux Toolkit configureStore
│   │   └── rootReducer.ts    ← combineReducers
│   ├── types/
│   │   └── api.types.ts      ← ApiResponse<T> TypeScript mirror of backend envelope
│   └── components/
│       └── layout/
│           └── AppLayout.tsx ← MUI AppBar + responsive Drawer shell
└── features/
    └── dashboard/
        └── DashboardPage.tsx ← first route, placeholder for Phase 2 widgets
```

**Conventions:**
- All HTTP calls go through `axiosClient.ts` — never raw `fetch`.
- Feature slices (Redux Toolkit) live inside their feature folder.
- Shared components are generic and import nothing from features.

---

## Infrastructure Topology

All services run in the `haru-net` Docker bridge network.

```
haru-net (bridge)
│
├── postgres     :5432   ← primary database (volume: haru-postgres-data)
├── redis        :6379   ← cache / session store (volume: haru-redis-data)
├── zookeeper    :2181   ← Kafka coordination
├── kafka        :9092   ← message broker
├── akhq         :8180   ← Kafka UI
├── keycloak     :8280   ← identity provider (volume: haru-keycloak-data)
├── sonarqube    :9000   ← code quality (volume: haru-sonar-*)
├── jenkins      :8090   ← CI/CD (volume: haru-jenkins-data)
├── jmeter       [perf]  ← load testing, only started under --profile perf
├── backend      :8080   ← Spring Boot API (depends on postgres, redis, kafka)
└── frontend     :3000   ← React UI via nginx (depends on backend)
```

Health checks are configured on `postgres`, `redis`, `kafka`, and `backend` so dependent services wait for readiness before starting.

---

## Technology Decisions and Rationale

| Decision | Choice | Rationale |
|----------|--------|-----------|
| JVM language | Java 21 (LTS) | Latest LTS, virtual threads (Loom) available for future use |
| Framework | Spring Boot 3.3.5 | Industry standard, excellent ecosystem, native Kubernetes support |
| Build tool | Maven (multi-module) | Predictable, widely supported in CI/CD pipelines |
| DB migration | Flyway | SQL-first migrations, easy rollback auditing |
| API docs | SpringDoc OpenAPI 3 | Auto-generated from annotations, zero boilerplate |
| Frontend framework | React 18 | Concurrent features, large ecosystem |
| Build tool | Vite 5 | Sub-second HMR, first-class TypeScript support |
| State management | Redux Toolkit | Structured, good DevTools, scales to complex state |
| HTTP client | Axios | Interceptors for auth tokens, consistent error handling |
| UI component lib | MUI v5 | Comprehensive, accessible, themeable |
| CSS utility | Tailwind CSS | Rapid prototyping, minimal custom CSS |
| Testing (backend) | JUnit 5 + Mockito | Standard, Mockito for unit isolation |
| Testing (frontend) | Jest + RTL + Cypress | Unit/integration (RTL) + E2E smoke (Cypress) |
| Coverage gate | Jacoco (LINE ≥ 80%, BRANCH ≥ 70%) | Enforced at build time, not just reported |
| Code quality | SonarQube + Checkstyle | SonarQube for runtime analysis, Checkstyle for style |
| Identity | Keycloak 24 | OAuth2 / OIDC, self-hosted, Spring Security integration |
| Messaging | Kafka 7.6.1 | Decoupled async events (order placed, inventory update) |
| Container runtime | Docker Compose | Simple local orchestration; same images deploy to K8s later |
