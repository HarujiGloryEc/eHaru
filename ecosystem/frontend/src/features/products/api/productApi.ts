import axiosClient from '../../../shared/api/axiosClient';
import type { ApiResponse } from '../../../shared/types/api.types';
import type { CreateProductRequest, ProductResponse, UpdateProductRequest } from '../types';

const BASE = '/api/v1/products';

export const productApi = {
  getByStore: (storeId: string) =>
    axiosClient
      .get<ApiResponse<ProductResponse[]>>(`${BASE}/store/${storeId}`)
      .then((r) => r.data.data),

  getById: (id: string) =>
    axiosClient.get<ApiResponse<ProductResponse>>(`${BASE}/${id}`).then((r) => r.data.data),

  create: (body: CreateProductRequest) =>
    axiosClient.post<ApiResponse<ProductResponse>>(BASE, body).then((r) => r.data.data),

  update: (id: string, body: UpdateProductRequest) =>
    axiosClient.put<ApiResponse<ProductResponse>>(`${BASE}/${id}`, body).then((r) => r.data.data),

  remove: (id: string) => axiosClient.delete(`${BASE}/${id}`),
};
