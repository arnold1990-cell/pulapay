import api from '../../lib/axios';
import type { ApiResponse, AuthPayload, AuthUser, LoginRequest, RegisterRequest } from './authTypes';

type RawAuthUser = {
  id?: number | string;
  name?: string;
  fullName?: string;
  email: string;
  role?: AuthUser['role'];
};

type RawAuthResponse = {
  token: string;
  user: RawAuthUser;
};

const unwrapApiResponse = <T>(payload: ApiResponse<T> | T): T => {
  if (payload && typeof payload === 'object' && 'success' in payload && 'data' in payload) {
    const envelope = payload as ApiResponse<T>;
    if (!envelope.success) {
      throw new Error(envelope.message || 'Request failed');
    }
    return envelope.data;
  }

  return payload as T;
};

function normalizeUser(user: RawAuthUser): AuthUser {
  if (!user?.email) {
    throw new Error('Authentication response missing user email');
  }

  return {
    id: user.id ?? user.email,
    name: user.name ?? user.fullName ?? '',
    email: user.email,
    role: user.role ?? 'USER'
  };
}

function normalizeAuthResponse(payload: RawAuthResponse): AuthPayload {
  if (!payload?.token) {
    throw new Error('Authentication response missing access token');
  }

  if (!payload?.user) {
    throw new Error('Authentication response missing user');
  }

  return {
    token: payload.token,
    user: normalizeUser(payload.user)
  };
}

export async function login(payload: LoginRequest): Promise<AuthPayload> {
  const { data } = await api.post<ApiResponse<RawAuthResponse>>('/api/auth/login', payload);
  const authData = normalizeAuthResponse(unwrapApiResponse(data));

  if (import.meta.env.DEV) {
    console.log('LOGIN RAW RESPONSE', data);
    console.log('LOGIN PARSED AUTH DATA', authData);
  }

  return authData;
}

export async function register(payload: RegisterRequest): Promise<AuthPayload> {
  const { data } = await api.post<ApiResponse<RawAuthResponse>>('/api/auth/register', payload);
  const authData = normalizeAuthResponse(unwrapApiResponse(data));

  if (import.meta.env.DEV) {
    console.log('REGISTER RAW RESPONSE', data);
    console.log('REGISTER PARSED AUTH DATA', authData);
  }

  return authData;
}

export async function getCurrentUser(): Promise<AuthUser> {
  const { data } = await api.get<ApiResponse<RawAuthUser>>('/api/auth/me');
  return normalizeUser(unwrapApiResponse(data));
}
