import api from '../../lib/axios';
import type { ApiEnvelope, AuthResponse, AuthUser, LoginRequest, RegisterRequest } from './authTypes';

const unwrap = <T>(payload: T | ApiEnvelope<T>): T => {
  if (payload && typeof payload === 'object' && 'data' in (payload as ApiEnvelope<T>)) {
    return (payload as ApiEnvelope<T>).data;
  }

  return payload as T;
};

export async function login(payload: LoginRequest): Promise<AuthResponse> {
  const { data } = await api.post<AuthResponse | ApiEnvelope<AuthResponse>>('/api/auth/login', payload);
  return unwrap(data);
}

export async function register(payload: RegisterRequest): Promise<AuthResponse> {
  const { data } = await api.post<AuthResponse | ApiEnvelope<AuthResponse>>('/api/auth/register', payload);
  return unwrap(data);
}

export async function getCurrentUser(): Promise<AuthUser> {
  const { data } = await api.get<AuthUser | ApiEnvelope<AuthUser>>('/api/v1/users/me');
  return unwrap(data);
}
