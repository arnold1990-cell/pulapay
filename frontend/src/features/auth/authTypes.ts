export type UserRole = 'USER' | 'ADMIN';

export type LoginRequest = {
  email: string;
  password: string;
};

export type RegisterRequest = {
  fullName: string;
  email: string;
  password: string;
};

export type AuthUser = {
  id?: string;
  fullName: string;
  email: string;
  role: UserRole;
  createdAt?: string;
};

export type AuthResponse = {
  token: string;
  user: AuthUser;
};

export type ApiEnvelope<T> = {
  data: T;
  message?: string;
};
