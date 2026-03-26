import api from '../../lib/axios';
import type { ApiEnvelope, AuthResponse, AuthUser, LoginRequest, RegisterRequest } from './authTypes';

type RawAuthUser = {
  id?: number;
  name?: string;
  fullName?: string;
  email: string;
  role: AuthUser['role'];
};

type RawAuthResponse = {
  token: string;
  user: RawAuthUser;
};

const unwrap = <T>(payload: ApiEnvelope<T>): T => {
  if (!payload.success) {
    throw new Error(payload.message || 'Request failed');
  }
  return payload.data;
};

function normalizeUser(user: RawAuthUser): AuthUser {
  return {
    id: user.id,
    name: user.name ?? user.fullName ?? '',
    email: user.email,
    role: user.role
  };
}

function normalizeAuthResponse(payload: RawAuthResponse): AuthResponse {
  if (!payload.token) {
    throw new Error('Authentication response missing access token');
  }

  return {
    token: payload.token,
    user: normalizeUser(payload.user)
  };
}

export async function login(payload: LoginRequest): Promise<AuthResponse> {
  if (import.meta.env.DEV) {
    console.debug('Auth API login request', { url: '/api/auth/login', email: payload.email });
  }
  const { data } = await api.post<ApiEnvelope<RawAuthResponse>>('/api/auth/login', payload);
  if (import.meta.env.DEV) {
    console.debug('Auth API login response', { success: data.success, message: data.message });
  }
  return normalizeAuthResponse(unwrap(data));
}

export async function register(payload: RegisterRequest): Promise<AuthResponse> {
  if (import.meta.env.DEV) {
    console.debug('Auth API register request', { url: '/api/auth/register', email: payload.email });
  }
  const { data } = await api.post<ApiEnvelope<RawAuthResponse>>('/api/auth/register', payload);
  if (import.meta.env.DEV) {
    console.debug('Auth API register response', { success: data.success, message: data.message });
  }
  return normalizeAuthResponse(unwrap(data));
}

export async function getCurrentUser(): Promise<AuthUser> {
  const { data } = await api.get<ApiEnvelope<RawAuthUser>>('/api/auth/me');
  return normalizeUser(unwrap(data));
}
