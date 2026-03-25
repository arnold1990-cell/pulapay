import api from '../../lib/axios';
import type { AuthResponse } from './authTypes';

export async function login(phoneNumber: string, password: string): Promise<AuthResponse> {
  const { data } = await api.post('/api/v1/auth/login', { phoneNumber, password });
  return data.data;
}
export async function register(payload: { fullName: string; phoneNumber: string; email: string; password: string }): Promise<AuthResponse> {
  const { data } = await api.post('/api/v1/auth/register', payload);
  return data.data;
}
