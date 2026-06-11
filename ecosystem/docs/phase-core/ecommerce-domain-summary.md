# Ecommerce Domain Summary — Phase Core

## What Was Built

Phase core establishes the four foundational commerce domains: **User**, **Store**, **Category**, and **Product**. Each follows Clean Architecture with feature-based packaging.

## Package Structure

```
org.eharu.shop
├── user/
│   ├── domain/       User.java, UserRole.java, UserRepository.java
│   ├── application/  UserService.java, UserServiceImpl.java
│   ├── dto/          CreateUserRequest, UpdateUserRequest, UserResponse
│   └── presentation/ UserController.java
├── store/
│   ├── domain/       Store.java, StoreStatus.java, StoreRepository.java
│   ├── application/  StoreService.java, StoreServiceImpl.java
│   ├── dto/          CreateStoreRequest, UpdateStoreRequest, StoreResponse
│   └── presentation/ StoreController.java
├── category/
│   ├── domain/       Category.java, CategoryRepository.java
│   ├── application/  CategoryService.java, CategoryServiceImpl.java
│   ├── dto/          CreateCategoryRequest, UpdateCategoryRequest, CategoryResponse
│   └── presentation/ CategoryController.java
└── product/
    ├── domain/       Product.java, ProductStatus.java, ProductRepository.java
    ├── application/  ProductService.java, ProductServiceImpl.java
    ├── dto/          CreateProductRequest, UpdateProductRequest, ProductResponse
    └── presentation/ ProductController.java
```

## Design Decisions

- **No Lombok**: Plain Java classes for entities (JPA compatibility), Java records for DTOs.
- **Constructor injection only**: All Spring beans use constructor injection.
- **Interface-first services**: Every service has an interface (`UserService`) and an implementation (`UserServiceImpl`), enabling mock injection in tests.
- **Manual mapping**: `from(Entity)` factory methods on response records — no MapStruct added this phase.
- **Error handling**: `ResourceNotFoundException` for 404s, `IllegalArgumentException` for business rule violations (duplicate slug/email). Both handled in `GlobalExceptionHandler` → 404/400.
- **Validation**: `@Valid` on all POST/PUT request bodies; `MethodArgumentNotValidException` handler returns 400 with field-level messages.

## Database Migrations

| File | Description |
|------|-------------|
| `V2__user_domain.sql` | `users` table with role check constraint |
| `V3__store_domain.sql` | `stores` table with merchant FK |
| `V4__category_domain.sql` | `categories` with self-join hierarchy |
| `V5__product_domain.sql` | `products` with store/category FKs, price/stock constraints |

## Test Coverage

- **Backend**: 58 unit tests — 4 service test suites + 4 controller test suites + 3 shared exception tests.
- **Jacoco**: LINE 85.6% ✓ (≥80%), BRANCH 71.4% ✓ (≥70%).
- **Frontend**: 12 Jest tests across 5 suites.

## Frontend Pages Added

| Page | Route | Description |
|------|-------|-------------|
| Dashboard | `/` | Summary cards (Stores, Categories, Products) |
| Stores | `/stores` | CRUD table with create/edit dialog |
| Categories | `/categories` | Store-scoped CRUD with parent hierarchy |
| Products | `/products` | Store-scoped list with status chips |
| Product Form | `/products/new`, `/products/:id/edit` | Full product create/edit form |
