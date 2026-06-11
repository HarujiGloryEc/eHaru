export interface CategoryResponse {
  id: string;
  name: string;
  slug: string;
  description?: string;
  displayOrder: number;
  isActive: boolean;
  storeId: string;
  parentId?: string;
  createdAt: string;
}

export interface CreateCategoryRequest {
  storeId: string;
  parentId?: string;
  name: string;
  slug: string;
  description?: string;
  displayOrder: number;
}

export interface UpdateCategoryRequest {
  name?: string;
  parentId?: string;
  description?: string;
  displayOrder?: number;
  isActive?: boolean;
}
