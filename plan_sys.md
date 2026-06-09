# Shopify Store Platform - plan.md

# Rules

* Use subagent-driven-development
* One subagent per checkbox task
* Follow RED → GREEN → REFACTOR TDD cycle
* Parent agent coordinates all subagents
* Parent agent reviews and merges all changes
* Run tests after every completed task
* Keep subagents isolated to related files only
* Prefer clean architecture principles
* Prefer SOLID principles
* Use feature-based package structure
* Use DTO pattern
* No business logic inside controllers
* Use centralized exception handling
* Prefer interface-first development
* Use constructor injection only
* All APIs must include OpenAPI documentation
* Every feature requires unit + integration tests
* Every phase must contain summary.md
* SonarQube quality gate must pass before merge
* Every service must contain Dockerfile
* Every frontend app must contain Dockerfile
* Entire ecosystem must boot using eco.yml
* All services must be independently runnable
* All services must support local development mode
* The backend must use Maven, and the base package name must be `org.eharu.shop`
---

# Architecture

## Frontend

* React
* Vite
* Redux Toolkit
* React Router
* Axios
* TailwindCSS
* Material UI
* Jest
* React Testing Library
* Cypress

## Backend

* Spring Boot
* Spring Security
* Keycloak
* PostgreSQL
* Redis
* Kafka
* JUnit
* Mockito
* Jacoco

## Infrastructure

* Docker
* eco.yml
* Kong
* HAProxy
* Prometheus
* Grafana
* Loki
* Tempo
* Jenkins
* SonarQube
* JMeter

---

# Ecosystem Structure

```text
ecosystem/
├── eco.yml
├── frontend/
├── backend/
├── keycloak/
├── kong/
├── haproxy/
├── postgres/
├── redis/
├── kafka/
├── akhq/
├── prometheus/
├── grafana/
├── loki/
├── tempo/
├── sonarqube/
├── jenkins/
└── jmeter/
```

---

# Domain Model

## Identity

User

* merchant
* customer
* admin

## Commerce

Store
Category
Product
Inventory
Cart
Order
Invoice
Payment

---

# Phase 1 - Backend & Frontend Foundation

## Backend Foundation

* [ ] Initialize Spring Boot project
* [ ] Configure Maven multi-module structure
* [ ] Configure shared dependency management
* [ ] Configure PostgreSQL connection
* [ ] Configure Flyway migrations
* [ ] Configure Jacoco
* [ ] Configure JUnit
* [ ] Configure Mockito
* [ ] Configure SonarQube scanner
* [ ] Configure test coverage thresholds
* [ ] Configure static analysis rules
* [ ] Implement health check endpoint
* [ ] Implement global exception handling
* [ ] Configure Swagger/OpenAPI
* [ ] Configure base clean architecture packages
* [ ] Create backend Dockerfile

## Frontend Foundation

* [ ] Initialize React + Vite project
* [ ] Configure React Router
* [ ] Configure Redux Toolkit
* [ ] Configure Axios API client
* [ ] Configure frontend environment handling
* [ ] Configure TailwindCSS
* [ ] Configure Material UI
* [ ] Configure Jest
* [ ] Configure React Testing Library
* [ ] Configure Cypress
* [ ] Configure frontend coverage reporting
* [ ] Configure ESLint
* [ ] Configure Prettier
* [ ] Create application layout
* [ ] Create dashboard shell
* [ ] Configure frontend package structure
* [ ] Create frontend Dockerfile

## Infrastructure Foundation

* [ ] Create eco.yml
* [ ] Configure PostgreSQL container
* [ ] Configure Redis container
* [ ] Configure Kafka container
* [ ] Configure AKHQ container
* [ ] Configure Keycloak container
* [ ] Configure SonarQube container
* [ ] Configure Jenkins container
* [ ] Configure JMeter container
* [ ] Verify backend startup
* [ ] Verify frontend startup
* [ ] Verify PostgreSQL connectivity
* [ ] Verify frontend/backend communication
* [ ] Verify Swagger accessibility

## Tests

* [ ] Verify backend smoke tests
* [ ] Verify frontend smoke tests
* [ ] Verify Jest execution
* [ ] Verify Cypress execution
* [ ] Verify Jacoco reports
* [ ] Verify SonarQube scan

## Documentation

* [ ] Create architecture-summary.md
* [ ] Create folder-structure-summary.md
* [ ] Create eco-summary.md
* [ ] Create phase-1-summary.md

---

# Phase 2 - Core Ecommerce Domain

## User Domain

* [ ] Create User entity
* [ ] Add merchant/customer/admin roles
* [ ] Add tenant ownership model
* [ ] Add validation annotations
* [ ] Create UserRepository
* [ ] Create UserService

## Store Domain

* [ ] Create Store entity
* [ ] Add merchant ownership relationship
* [ ] Add store settings model
* [ ] Add store status handling
* [ ] Create StoreRepository
* [ ] Create StoreService

## Category Domain

* [ ] Create Category entity
* [ ] Add store-category relationship
* [ ] Implement category hierarchy support
* [ ] Create CategoryRepository
* [ ] Create CategoryService

## Product Domain

* [ ] Create Product entity
* [ ] Add product-category relationship
* [ ] Add product-store relationship
* [ ] Add inventory fields
* [ ] Add pricing fields
* [ ] Add product status handling
* [ ] Add product image handling
* [ ] Create ProductRepository
* [ ] Create ProductService

## APIs

* [ ] Implement UserController
* [ ] Implement StoreController
* [ ] Implement CategoryController
* [ ] Implement ProductController

## Frontend

* [ ] Create merchant dashboard
* [ ] Create store management UI
* [ ] Create category management UI
* [ ] Create product management UI
* [ ] Create product creation form
* [ ] Create product editing form

## Tests

* [ ] Add entity relationship tests
* [ ] Add repository tests
* [ ] Add service tests
* [ ] Add controller integration tests
* [ ] Add Jest frontend tests
* [ ] Add Cypress merchant flow tests

## Documentation

* [ ] Create ecommerce-domain-summary.md
* [ ] Create entity-relationship-summary.md
* [ ] Create phase-2-summary.md

---

# Phase 3 - Authentication & Multi-Tenant Security

## Spring Security

* [ ] Configure Spring Security
* [ ] Configure JWT validation
* [ ] Configure RBAC permissions
* [ ] Configure authentication exception handling
* [ ] Configure tenant-aware security context

## Keycloak

* [ ] Configure Keycloak realm
* [ ] Configure Keycloak clients
* [ ] Configure merchant/customer/admin roles
* [ ] Configure Keycloak themes
* [ ] Create custom Keycloak login page

## Keycloak SPI

* [ ] Create Keycloak SPI project
* [ ] Create custom authenticator SPI
* [ ] Implement username/password validation API
* [ ] Implement authz synchronization
* [ ] Implement SPI error handling
* [ ] Add SPI integration tests
* [ ] Create SPI Dockerfile
* [ ] Add SPI Docker packaging

## Frontend Authentication

* [ ] Create login page
* [ ] Create session management
* [ ] Create token refresh handling
* [ ] Create protected routes
* [ ] Create logout flow
* [ ] Create RBAC frontend rendering

## Tests

* [ ] Add backend authentication tests
* [ ] Add Keycloak integration tests
* [ ] Add frontend authentication Jest tests
* [ ] Add Cypress authentication flows

## Documentation

* [ ] Create authentication-flow.md
* [ ] Create keycloak-spi-summary.md
* [ ] Create multi-tenant-security-summary.md
* [ ] Create phase-3-summary.md

---

# Phase 4 - Commerce Flow

## Cart Domain

* [ ] Create Cart entity
* [ ] Create CartItem entity
* [ ] Implement cart calculation logic
* [ ] Implement cart persistence
* [ ] Create CartRepository
* [ ] Create CartService

## Order Domain

* [ ] Create Order entity
* [ ] Create OrderItem entity
* [ ] Implement transactional checkout flow
* [ ] Implement order lifecycle states
* [ ] Implement order total calculation
* [ ] Create OrderRepository
* [ ] Create OrderService

## Invoice Domain

* [ ] Create Invoice entity
* [ ] Implement invoice number generation
* [ ] Implement invoice status handling
* [ ] Create InvoiceRepository
* [ ] Create InvoiceService

## Payment Domain

* [ ] Create Payment entity
* [ ] Implement payment lifecycle
* [ ] Implement payment status handling
* [ ] Create PaymentRepository
* [ ] Create PaymentService

## APIs

* [ ] Implement CartController
* [ ] Implement OrderController
* [ ] Implement InvoiceController
* [ ] Implement PaymentController

## Frontend

* [ ] Create shopping cart page
* [ ] Create checkout page
* [ ] Create invoice page
* [ ] Create payment page
* [ ] Create customer order history page

## Tests

* [ ] Add transactional tests
* [ ] Add service tests
* [ ] Add checkout integration tests
* [ ] Add Jest commerce tests
* [ ] Add Cypress checkout flow tests

## Documentation

* [ ] Create commerce-flow-summary.md
* [ ] Create payment-flow-summary.md
* [ ] Create phase-4-summary.md

---

# Phase 5 - Inventory & Real-Time

## Inventory

* [ ] Implement inventory reservation
* [ ] Implement inventory deduction
* [ ] Implement low stock alerts

## Kafka

* [ ] Configure Kafka producers
* [ ] Configure Kafka consumers
* [ ] Implement commerce events
* [ ] Implement dead-letter handling

## WebSocket

* [ ] Configure WebSocket server
* [ ] Implement live order updates
* [ ] Implement live inventory updates
* [ ] Implement frontend WebSocket client

## Tests

* [ ] Add inventory tests
* [ ] Add Kafka integration tests
* [ ] Add WebSocket integration tests
* [ ] Add Cypress realtime flow tests

## Documentation

* [ ] Create inventory-summary.md
* [ ] Create realtime-summary.md
* [ ] Create phase-5-summary.md

---

# Phase 6 - Observability

## Metrics

* [ ] Configure Prometheus metrics
* [ ] Configure Micrometer
* [ ] Add JVM metrics
* [ ] Add business metrics

## Logging

* [ ] Configure structured logging
* [ ] Configure Loki integration
* [ ] Configure correlation IDs

## Tracing

* [ ] Configure Tempo tracing
* [ ] Configure distributed tracing propagation

## Dashboards

* [ ] Configure Grafana
* [ ] Create JVM dashboard
* [ ] Create API metrics dashboard
* [ ] Create business metrics dashboard

## Tests

* [ ] Add observability smoke tests

## Documentation

* [ ] Create observability-summary.md
* [ ] Create phase-6-summary.md

---

# Phase 7 - Gateway & Infrastructure

## Kong

* [ ] Configure Kong routes
* [ ] Configure JWT validation
* [ ] Configure rate limiting
* [ ] Create Kong Dockerfile

## HAProxy

* [ ] Configure load balancing
* [ ] Configure failover routing
* [ ] Configure health checks
* [ ] Create HAProxy Dockerfile

## Redis

* [ ] Configure Redis caching
* [ ] Configure session caching
* [ ] Configure product caching

## Docker

* [ ] Optimize backend Docker images
* [ ] Optimize frontend Docker images
* [ ] Create production eco.yml

## Tests

* [ ] Add infrastructure smoke tests

## Documentation

* [ ] Create infrastructure-summary.md
* [ ] Create gateway-summary.md
* [ ] Create phase-7-summary.md

---

# Phase 8 - CI/CD Pipeline

## Jenkins Pipeline

* [ ] Create Jenkins pipeline
* [ ] Implement backend build stage
* [ ] Implement frontend build stage
* [ ] Implement backend test stage
* [ ] Implement frontend Jest stage
* [ ] Implement Cypress E2E stage
* [ ] Implement Jacoco coverage publishing
* [ ] Implement SonarQube analysis stage
* [ ] Implement SonarQube quality gate enforcement
* [ ] Implement Docker image build stage
* [ ] Implement Docker image publishing
* [ ] Implement deployment pipeline
* [ ] Implement rollback strategy

## Quality Gates

* [ ] Fail pipeline on low Jacoco coverage
* [ ] Fail pipeline on failed Jest tests
* [ ] Fail pipeline on failed Cypress tests
* [ ] Fail pipeline on Sonar critical issues
* [ ] Fail pipeline on duplicated code threshold
* [ ] Fail pipeline on security hotspot violations

## Performance Testing

### JMeter

* [ ] Configure JMeter test suite
* [ ] Create authentication load tests
* [ ] Create product browsing load tests
* [ ] Create checkout load tests
* [ ] Create payment flow load tests
* [ ] Create inventory concurrency tests
* [ ] Create WebSocket load tests
* [ ] Create Kafka throughput tests
* [ ] Create API Gateway stress tests
* [ ] Create database connection pool stress tests

### Performance Reports

* [ ] Generate JMeter HTML reports
* [ ] Publish performance reports
* [ ] Track response time thresholds
* [ ] Track throughput thresholds
* [ ] Track failure rate thresholds

### CI/CD Performance Gates

* [ ] Run JMeter smoke performance tests
* [ ] Publish JMeter reports
* [ ] Fail pipeline on response time threshold
* [ ] Fail pipeline on error rate threshold
* [ ] Fail pipeline on throughput degradation

## Deployment

* [ ] Create staging deployment flow
* [ ] Create production deployment flow
* [ ] Create branch strategy
* [ ] Create release workflow

## Tests

* [ ] Add deployment validation tests

## Documentation

* [ ] Create cicd-summary.md
* [ ] Create deployment-guide.md
* [ ] Create release-process-summary.md
* [ ] Create performance-testing-summary.md
* [ ] Create phase-8-summary.md

---

# Phase 9 - Final Hardening

* [ ] Run full integration testing
* [ ] Run security testing
* [ ] Run load testing
* [ ] Run spike testing
* [ ] Run stress testing
* [ ] Run endurance testing
* [ ] Run frontend E2E testing
* [ ] Validate observability under load
* [ ] Review architecture consistency
* [ ] Review code duplication
* [ ] Review performance bottlenecks
* [ ] Finalize production documentation

## Documentation

* [ ] Create production-readiness-report.md
* [ ] Create final-system-summary.md

---

# Definition of Done

A task is complete only if:

* RED → GREEN → REFACTOR completed
* Backend tests pass
* Frontend tests pass
* Cypress E2E passes
* Jacoco coverage threshold passes
* SonarQube quality gate passes
* JMeter smoke tests pass
* No critical vulnerabilities
* No blocker code smells
* OpenAPI documentation updated
* No unrelated files modified
* Parent agent approved merge
* Docker environment validated
* Phase summary updated
