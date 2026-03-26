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
  id?: string;
  name: string;
  email: string;
  role: UserRole;
  createdAt?: string;
};

export type AuthResponse = {
  token: string;
  user: AuthUser;
};

export type ApiEnvelope<T> = {
  success: boolean;
  message: string;
  data: T;
  timestamp?: string;
};

export type ApiError = {
  success: false;
  message: string;
  errors?: Record<string, string>;
};
