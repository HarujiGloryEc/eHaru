# Entity Relationship Summary — Phase Core

## Domain Entities

### User
- Central identity entity. Roles: `MERCHANT`, `CUSTOMER`, `ADMIN`.
- `tenant_id` (UUID) reserved for future multi-tenant scoping (Phase Auth).
- A merchant user owns one or more Stores.

### Store
- Belongs to a single `User` (merchant) via `merchant_id FK → users.id`.
- Status: `ACTIVE`, `INACTIVE`, `SUSPENDED`.
- `slug` is globally unique across all stores.
- Categories and Products are scoped to a Store.

### Category
- Belongs to a `Store` via `store_id FK → stores.id`.
- Self-referential hierarchy: `parent_id FK → categories.id` (nullable — root categories have no parent).
- `(store_id, slug)` unique — same slug can exist in different stores.
- Depth is unbounded (tree traversal handled at application layer).

### Product
- Belongs to a `Store` via `store_id FK → stores.id`.
- Optionally assigned to a `Category` via `category_id FK → categories.id` (nullable, `ON DELETE SET NULL`).
- Status: `DRAFT`, `ACTIVE`, `INACTIVE`, `ARCHIVED`.
- `(store_id, slug)` unique.
- Inventory fields: `stock_quantity`, `low_stock_threshold`, `track_inventory`.
- Pricing fields: `price` (required), `compare_at_price`, `cost_price`.

## Relationship Diagram

```
User (MERCHANT)
  │  1
  │
  ▼ N
Store ──── slug (globally unique)
  │  1
  │
  ▼ N
Category ──── parent_id (self-join, nullable)
  │  1
  │
  ▼ N
Product
```

## Cascade Rules

| Relationship       | On Delete Parent |
|--------------------|-----------------|
| User → Store       | CASCADE          |
| Store → Category   | CASCADE          |
| Store → Product    | CASCADE          |
| Category → Product | SET NULL         |
| Category → Category (parent) | SET NULL |

## API Endpoints

| Domain   | Base Path              |
|----------|------------------------|
| User     | `GET/POST/PUT/DELETE /api/v1/users` |
| Store    | `GET/POST/PUT/DELETE /api/v1/stores` |
| Category | `GET/POST/PUT/DELETE /api/v1/categories`, `GET /api/v1/categories/store/{storeId}` |
| Product  | `GET/POST/PUT/DELETE /api/v1/products`, `GET /api/v1/products/store/{storeId}` |
