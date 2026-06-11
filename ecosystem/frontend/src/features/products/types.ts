export type ProductStatus = 'DRAFT' | 'ACTIVE' | 'INACTIVE' | 'ARCHIVED';

export interface ProductResponse {
  id: string;
  name: string;
  slug: string;
  description?: string;
  sku?: string;
  price: number;
  compareAtPrice?: number;
  costPrice?: number;
  stockQuantity: number;
  lowStockThreshold: number;
  trackInventory: boolean;
  status: ProductStatus;
  primaryImageUrl?: string;
  storeId: string;
  categoryId?: string;
  createdAt: string;
}

export interface CreateProductRequest {
  storeId: string;
  categoryId?: string;
  name: string;
  slug: string;
  description?: string;
  sku?: string;
  price: number;
  compareAtPrice?: number;
  costPrice?: number;
  stockQuantity: number;
  lowStockThreshold: number;
  trackInventory: boolean;
  status?: ProductStatus;
  primaryImageUrl?: string;
}

export interface UpdateProductRequest {
  name?: string;
  slug?: string;
  description?: string;
  sku?: string;
  price?: number;
  compareAtPrice?: number;
  costPrice?: number;
  stockQuantity?: number;
  lowStockThreshold?: number;
  trackInventory?: boolean;
  status?: ProductStatus;
  primaryImageUrl?: string;
  categoryId?: string;
}
