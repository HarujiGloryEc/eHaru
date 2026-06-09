export interface ApiResponse<T> {
  data: T;
  message?: string;
  timestamp?: string;
}

export interface ApiError {
  code: string;
  message: string;
  timestamp: string;
}
