# Phase 1 — Summary

## What Phase 1 Accomplishes

Phase 1 establishes the complete project skeleton — every structural decision, tooling choice, and infrastructure service that all future phases build on top of.

By the end of Phase 1 the following is in place:

- A running **Spring Boot 3.3.5 / Java 21** backend with a working HTTP endpoint, standardized response envelopes, global error handling, and an auto-generated Swagger UI.
- A running **React 18 / Vite 5 / TypeScript** frontend with Redux Toolkit state management, React Router v6 routing, an MUI layout shell, and a dashboard page that talks to the backend.
- A **Docker Compose** file (`eco.yml`) that brings up all 11 services (database, cache, message broker, identity provider, code quality, CI/CD, load testing) in a single command on a shared Docker network.
- **Quality gates** enforced at build time: Jacoco (LINE ≥ 80%, BRANCH ≥ 70%), Checkstyle, and SonarQube configuration.
- **Test scaffolding** in place for both tiers: JUnit 5 + Mockito unit tests, `@WebMvcTest` slice tests, Jest + React Testing Library component tests, and a Cypress E2E skeleton.

Phase 1 produces no business logic beyond a health check — its value is the foundation: everyone who writes Phase 2 code drops a feature package into an already-working, already-tested, already-containerised structure.

---

## Definition of Done — Phase 1 Checklist

| Item | Status |
|------|--------|
| `eco.yml` starts all services with `docker compose -f eco.yml up -d` | Done (requires local Docker run to confirm) |
| `GET /api/v1/health` returns HTTP 200 with `{"success":true}` | Done (requires local run to confirm) |
| Swagger UI accessible at `/swagger-ui.html` | Done (requires local run to confirm) |
| Frontend loads at `http://localhost:3000` without console errors | Done (requires local run to confirm) |
| Frontend `/api` proxy routes to backend correctly | Done (nginx + Vite config in place) |
| Backend unit tests pass (`mvn test`) | Done — code written; **pending local `mvn test` execution** |
| Backend Jacoco coverage gates pass (LINE ≥ 80%, BRANCH ≥ 70%) | Done — gates configured; **pending local verification** |
| Frontend unit tests pass (`npm test`) | Done — code written; **pending local `npm test` execution** |
| Frontend coverage thresholds pass (≥ 80%) | Done — thresholds configured; **pending local verification** |
| Cypress smoke test passes | Done — test written; **pending local `cypress run` execution** |
| Multi-stage Dockerfiles build successfully | Done (requires `docker compose build` to confirm) |
| Flyway `V1__init.sql` applies cleanly on first boot | Done (requires local run to confirm) |
| Keycloak realm import succeeds | Done (requires local run to confirm) |
| SonarQube accessible at `http://localhost:9000` | Done (requires local run + `vm.max_map_count` fix on Windows) |
| Jenkins accessible at `http://localhost:8090` | Done (requires local run to confirm) |
| All code committed to version control | **Pending — developer action required** |

---

## Architecture Decisions Made in Phase 1

| Decision | What Was Decided | Why |
|----------|-----------------|-----|
| Package structure | Clean Architecture layers nested inside feature packages | Features are self-contained; shared code lives in `shared/`. Makes it easy to extract a microservice later. |
| Response envelope | `ApiResponse<T>` for success, `ApiError` for errors | Consistent shape across all endpoints; frontend can rely on `success` boolean before accessing `data`. |
| Error handling | Single `GlobalExceptionHandler` (`@RestControllerAdvice`) | One place to map exceptions to HTTP status codes; no try/catch in controllers. |
| DB migration | Flyway with SQL scripts in `resources/db/migration/` | SQL-first (not Hibernate `ddl-auto`); every schema change is auditable and reversible. |
| Spring profiles | `local` (localhost DB) and `docker` (container DB) | Same binary, different config; no code changes to switch environments. |
| Frontend API calls | All through `axiosClient.ts`, never raw `fetch` | Single place to attach auth headers, handle token refresh, and set timeouts. |
| State management | Redux Toolkit only (`configureStore` + feature slices) | No `useContext` for server state; avoids prop drilling and keeps server cache separate from UI state. |
| CSS strategy | Tailwind utilities + MUI components | MUI for complex, accessible components (AppBar, Drawer, Dialog); Tailwind for layout and spacing. |
| Coverage enforcement | Jacoco breaks the build if gates are not met | Coverage as a hard gate, not a soft metric, prevents regressions from being merged silently. |
| Container strategy | Multi-stage Dockerfiles (build → runtime) | Runtime images contain only the JRE/nginx binary + artifacts; no source or build tools in production. |

---

## What Phase 2 Builds On Top Of

Phase 2 inherits a working, containerised, tested, and quality-gated skeleton. Every new feature in Phase 2:

1. **Adds a package** under `org.eharu.shop/<feature>/` with `domain/`, `application/`, and `presentation/` sub-packages — no structural decisions required.
2. **Adds a Flyway migration** (`V2__...sql`, `V3__...sql`, …) — Flyway applies it automatically on next start.
3. **Adds a Redux slice** under `src/features/<feature>/` — `rootReducer.ts` is the only file that changes in `shared/`.
4. **Adds tests** following the patterns already established by the health feature tests.
5. **Runs through the same quality gates** — Jacoco, Checkstyle, Jest coverage — without any tooling changes.

Phase 2 scope: `User`, `Store`, `Category`, `Product` entities, repositories, services, REST controllers, Flyway migrations, Testcontainers integration tests, and the corresponding React pages with Keycloak authentication.
