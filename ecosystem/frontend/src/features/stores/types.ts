export type StoreStatus = 'ACTIVE' | 'INACTIVE' | 'SUSPENDED';

export interface StoreResponse {
  id: string;
  name: string;
  slug: string;
  description?: string;
  status: StoreStatus;
  merchantId: string;
  createdAt: string;
}

export interface CreateStoreRequest {
  name: string;
  slug: string;
  description?: string;
  merchantId: string;
}

export interface UpdateStoreRequest {
  name?: string;
  description?: string;
  status?: StoreStatus;
}
