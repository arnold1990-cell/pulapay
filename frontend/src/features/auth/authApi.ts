import api from '../../lib/axios';
import type { ApiEnvelope, AuthResponse, AuthUser, LoginRequest, RegisterRequest } from './authTypes';

type RawAuthResponse = {
  token?: string;
  accessToken?: string;
  user?: AuthUser;
  fullName?: string;
  email?: string;
  role?: AuthUser['role'];
};

const unwrap = <T>(payload: T | ApiEnvelope<T>): T => {
  if (payload && typeof payload === 'object' && 'data' in (payload as ApiEnvelope<T>)) {
    return (payload as ApiEnvelope<T>).data;
  }

  return payload as T;
};

function normalizeAuthResponse(payload: RawAuthResponse): AuthResponse {
  const token = payload.token ?? payload.accessToken;

  if (!token) {
    throw new Error('Authentication response missing access token');
  }

  const user: AuthUser = payload.user ?? {
    fullName: payload.fullName ?? 'PulaPay User',
    email: payload.email ?? '',
    role: payload.role ?? 'USER'
  };

  return { token, user };
}

export async function login(payload: LoginRequest): Promise<AuthResponse> {
  const { data } = await api.post<RawAuthResponse | ApiEnvelope<RawAuthResponse>>('/api/auth/login', payload);
  return normalizeAuthResponse(unwrap(data));
}

export async function register(payload: RegisterRequest): Promise<AuthResponse> {
  const { data } = await api.post<RawAuthResponse | ApiEnvelope<RawAuthResponse>>('/api/auth/register', payload);
  return normalizeAuthResponse(unwrap(data));
}

export async function getCurrentUser(): Promise<AuthUser> {
  try {
    const { data } = await api.get<AuthUser | ApiEnvelope<AuthUser>>('/api/auth/me');
    return unwrap(data);
  } catch {
    const { data } = await api.get<AuthUser | ApiEnvelope<AuthUser>>('/api/v1/users/me');
    return unwrap(data);
  }
}
