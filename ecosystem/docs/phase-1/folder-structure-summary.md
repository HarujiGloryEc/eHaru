# Phase 1 — Folder Structure Summary

Everything created in Phase 1 lives under `h:\work\haru-shop\work\ecosystem\`.

```
ecosystem/
├── eco.yml                                          ← Docker Compose orchestrator (11 services, haru-net bridge)
│
├── backend/
│   ├── pom.xml                                      ← Parent POM: Spring Boot 3.3.5, Java 21, module declarations
│   └── shop-api/
│       ├── pom.xml                                  ← Module POM: all runtime/test deps, Jacoco, Checkstyle, Flyway
│       ├── Dockerfile                               ← Multi-stage build: JDK 21 compile → JRE 21 runtime image
│       ├── checkstyle.xml                           ← Checkstyle ruleset (Google style base)
│       └── src/
│           ├── main/
│           │   ├── java/org/eharu/shop/
│           │   │   ├── ShopApiApplication.java      ← Spring Boot entry point (@SpringBootApplication)
│           │   │   ├── config/
│           │   │   │   └── OpenApiConfig.java       ← SpringDoc OpenAPI 3 metadata (title, version, servers)
│           │   │   ├── shared/
│           │   │   │   ├── exception/
│           │   │   │   │   ├── ApiError.java        ← Error envelope (status, message, timestamp, errors map)
│           │   │   │   │   ├── ResourceNotFoundException.java  ← 404 semantic exception
│           │   │   │   │   └── GlobalExceptionHandler.java    ← @RestControllerAdvice, maps exceptions → ApiError
│           │   │   │   └── response/
│           │   │   │       └── ApiResponse.java     ← Generic success envelope ApiResponse<T> (success, data, message)
│           │   │   └── health/
│           │   │       ├── domain/
│           │   │       │   └── HealthStatus.java    ← Domain record: status string, timestamp, version
│           │   │       ├── application/
│           │   │       │   └── HealthService.java   ← Builds HealthStatus, no framework deps
│           │   │       └── presentation/
│           │   │           └── HealthController.java ← GET /api/v1/health → ApiResponse<HealthStatus>
│           │   └── resources/
│           │       ├── application.yml              ← Base config: server port, datasource, flyway, logging
│           │       ├── application-local.yml        ← Local profile: postgres on localhost:5432
│           │       ├── application-docker.yml       ← Docker profile: postgres on haru-postgres:5432
│           │       └── db/migration/
│           │           └── V1__init.sql             ← Flyway baseline: extensions, audit columns, schema_version table
│           └── test/
│               └── java/org/eharu/shop/
│                   ├── ShopApiApplicationTests.java         ← Spring context loads smoke test
│                   ├── health/
│                   │   ├── application/
│                   │   │   └── HealthServiceTest.java       ← Unit test: HealthService builds correct HealthStatus
│                   │   └── presentation/
│                   │       └── HealthControllerTest.java    ← @WebMvcTest: GET /api/v1/health returns 200
│                   └── shared/
│                       └── exception/
│                           └── GlobalExceptionHandlerTest.java  ← @WebMvcTest: 404 and 400 error shapes
│
├── frontend/
│   ├── package.json                                 ← NPM manifest: React 18, Vite 5, MUI, Redux Toolkit, Axios
│   ├── vite.config.ts                               ← Vite config: /api proxy to backend, port 3000
│   ├── tsconfig.json                                ← TypeScript strict mode, path aliases
│   ├── jest.config.ts                               ← Jest + jsdom + coverage (statements/lines/branches ≥ 80%)
│   ├── babel.config.cjs                             ← Babel preset for Jest (tsx transform)
│   ├── cypress.config.ts                            ← Cypress E2E config: baseUrl http://localhost:3000
│   ├── tailwind.config.ts                           ← Tailwind: content glob, MUI theme integration
│   ├── postcss.config.js                            ← PostCSS: tailwindcss + autoprefixer
│   ├── .eslintrc.cjs                                ← ESLint: react-hooks + typescript-eslint rules
│   ├── .prettierrc                                  ← Prettier: 2-space indent, single quotes, trailing commas
│   ├── Dockerfile                                   ← Multi-stage: Node 20 build → nginx:alpine serve
│   ├── nginx.conf                                   ← nginx: SPA fallback, /api proxy to backend:8080
│   ├── .env                                         ← Default env: VITE_API_BASE_URL=http://localhost:8080
│   ├── .env.local                                   ← Local override (git-ignored)
│   ├── .env.production                              ← Production: VITE_API_BASE_URL='' (nginx proxies)
│   └── src/
│       ├── main.tsx                                 ← React root: Provider (Redux), BrowserRouter, <App />
│       ├── App.tsx                                  ← Top-level component, renders <AppRouter />
│       ├── index.css                                ← Tailwind @base/@components/@utilities directives
│       ├── router/
│       │   └── AppRouter.tsx                        ← React Router v6: route table, lazy-load placeholders
│       ├── shared/
│       │   ├── api/
│       │   │   └── axiosClient.ts                   ← Axios instance: baseURL, 10s timeout, auth interceptor hook
│       │   ├── store/
│       │   │   ├── store.ts                         ← Redux configureStore + TypeScript RootState/AppDispatch types
│       │   │   └── rootReducer.ts                   ← combineReducers (empty in Phase 1, grows with features)
│       │   ├── types/
│       │   │   └── api.types.ts                     ← TypeScript: ApiResponse<T>, ApiError, PaginatedResponse<T>
│       │   └── components/
│       │       └── layout/
│       │           ├── AppLayout.tsx                ← MUI AppBar + responsive Drawer shell, Outlet for routes
│       │           └── AppLayout.test.tsx           ← RTL: renders header, nav links visible
│       └── features/
│           └── dashboard/
│               ├── DashboardPage.tsx                ← Placeholder dashboard, calls GET /api/v1/health on mount
│               └── DashboardPage.test.tsx           ← RTL: page renders without crash
│
├── postgres/
│   └── init/
│       └── 01_create_db.sql                         ← Creates haru_shop database and haru_user role on first start
│
├── akhq/
│   └── application.yml                              ← AKHQ connection config pointing to haru-kafka:9092
│
├── keycloak/
│   └── realm-export.json                            ← haru-shop realm: client, roles, default scopes
│
├── sonarqube/
│   └── sonar.properties                             ← SonarQube project key, sources, test paths
│
├── jenkins/
│   └── Dockerfile                                   ← Jenkins LTS + JDK 21 + Maven 3 + Node 20 + Docker CLI
│
└── jmeter/
    └── Dockerfile                                   ← Apache JMeter 5.6.3 on Alpine, ENTRYPOINT for test plans
```
