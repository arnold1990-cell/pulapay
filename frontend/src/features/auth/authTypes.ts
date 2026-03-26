export type UserRole = 'USER' | 'ADMIN';

export type LoginRequest = {
  email: string;
  password: string;
};

export type RegisterRequest = {
  name: string;
  email: string;
  password: string;
};

export type AuthUser = {
  id: number | string;
  name: string;
  email: string;
  role: UserRole | string;
  createdAt?: string;
};

export type AuthPayload = {
  token: string;
  user: AuthUser;
};

export type ApiResponse<T> = {
  success: boolean;
  message: string;
  data: T;
  timestamp?: string;
};

export type ApiError = {
  success: false;
  message: string;
  path?: string;
  timestamp?: string;
  errors?: Record<string, string>;
};
