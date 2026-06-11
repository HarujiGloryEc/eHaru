# Phase Core Summary

## Phase Goal

Establish the core ecommerce domain — User, Store, Category, and Product — with full CRUD APIs, a working merchant UI, and sufficient test coverage to pass all quality gates.

## Deliverables

### Backend

| Artifact | Count |
|----------|-------|
| Flyway migrations | 4 (V2–V5) |
| JPA entities | 4 (User, Store, Category, Product) |
| Spring Data repositories | 4 |
| Service interfaces + impls | 4 pairs |
| REST controllers | 4 |
| DTO records | 12 |
| Unit tests | 58 (100% passing) |

### Frontend

| Artifact | Description |
|----------|-------------|
| Types | 3 feature type modules |
| API clients | 3 (storeApi, categoryApi, productApi) |
| Pages | 5 (Dashboard, Stores, Categories, Products, ProductForm) |
| Tests | 12 Jest tests (5 suites) |

### Infrastructure

| Artifact | Description |
|----------|-------------|
| `jest.config.cjs` | Replaced `.ts` config (no ts-node needed) |
| `babel.config.cjs` | Added `import.meta` transform for Jest |
| `@mui/icons-material@5` | Installed, matching MUI v5 |

## Quality Gate Results

| Gate | Result |
|------|--------|
| Backend tests | 58/58 ✓ |
| Jacoco LINE | 85.6% ✓ (min 80%) |
| Jacoco BRANCH | 71.4% ✓ (min 70%) |
| Frontend Jest | 12/12 ✓ |

## Deviations from Plan

- **No Cypress merchant flow tests**: Cypress requires a running backend + frontend stack. E2E tests deferred to Phase Auth when the full stack is containerised and running.
- **Placeholder merchant ID**: `StoresPage` uses a hardcoded UUID placeholder for `merchantId` — real merchant context comes from the session token in Phase Auth.
- **No Redux slices for domain data**: Domain pages use local `useState` + `useEffect` instead of Redux. Redux is reserved for auth/session state (Phase Auth).

## What's Next (Phase Auth)

- Wire Spring Security + JWT validation
- Configure Keycloak realm and clients
- Replace placeholder merchant ID with authenticated user context
- Add protected route guards to the frontend router
