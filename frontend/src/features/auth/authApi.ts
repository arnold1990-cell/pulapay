import api from '../../lib/axios';
import type { ApiEnvelope, AuthResponse, AuthUser, LoginRequest, RegisterRequest } from './authTypes';

type RawAuthUser = {
  id?: string;
  name?: string;
  fullName?: string;
  email: string;
  role: AuthUser['role'];
};

type RawAuthResponse = {
  token: string;
  user: RawAuthUser;
};

const unwrap = <T>(payload: ApiEnvelope<T>): T => payload.data;

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
  const { data } = await api.post<ApiEnvelope<RawAuthResponse>>('/api/auth/login', payload);
  return normalizeAuthResponse(unwrap(data));
}

export async function register(payload: RegisterRequest): Promise<AuthResponse> {
  const { data } = await api.post<ApiEnvelope<RawAuthResponse>>('/api/auth/register', payload);
  return normalizeAuthResponse(unwrap(data));
}

export async function getCurrentUser(): Promise<AuthUser> {
  const { data } = await api.get<ApiEnvelope<RawAuthUser>>('/api/auth/me');
  return normalizeUser(unwrap(data));
}
