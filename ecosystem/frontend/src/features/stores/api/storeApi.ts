import axiosClient from '../../../shared/api/axiosClient';
import type { ApiResponse } from '../../../shared/types/api.types';
import type { CreateStoreRequest, StoreResponse, UpdateStoreRequest } from '../types';

const BASE = '/api/v1/stores';

export const storeApi = {
  getAll: () =>
    axiosClient.get<ApiResponse<StoreResponse[]>>(BASE).then((r) => r.data.data),

  getById: (id: string) =>
    axiosClient.get<ApiResponse<StoreResponse>>(`${BASE}/${id}`).then((r) => r.data.data),

  create: (body: CreateStoreRequest) =>
    axiosClient.post<ApiResponse<StoreResponse>>(BASE, body).then((r) => r.data.data),

  update: (id: string, body: UpdateStoreRequest) =>
    axiosClient.put<ApiResponse<StoreResponse>>(`${BASE}/${id}`, body).then((r) => r.data.data),

  remove: (id: string) => axiosClient.delete(`${BASE}/${id}`),
};
