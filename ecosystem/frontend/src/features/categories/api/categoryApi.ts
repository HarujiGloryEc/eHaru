import axiosClient from '../../../shared/api/axiosClient';
import type { ApiResponse } from '../../../shared/types/api.types';
import type { CategoryResponse, CreateCategoryRequest, UpdateCategoryRequest } from '../types';

const BASE = '/api/v1/categories';

export const categoryApi = {
  getByStore: (storeId: string) =>
    axiosClient
      .get<ApiResponse<CategoryResponse[]>>(`${BASE}/store/${storeId}`)
      .then((r) => r.data.data),

  getById: (id: string) =>
    axiosClient.get<ApiResponse<CategoryResponse>>(`${BASE}/${id}`).then((r) => r.data.data),

  create: (body: CreateCategoryRequest) =>
    axiosClient.post<ApiResponse<CategoryResponse>>(BASE, body).then((r) => r.data.data),

  update: (id: string, body: UpdateCategoryRequest) =>
    axiosClient.put<ApiResponse<CategoryResponse>>(`${BASE}/${id}`, body).then((r) => r.data.data),

  remove: (id: string) => axiosClient.delete(`${BASE}/${id}`),
};
