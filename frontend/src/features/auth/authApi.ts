import api from '../../lib/axios';
import type { AuthResponse } from './authTypes';

export async function login(email: string, password: string): Promise<AuthResponse> {
  const { data } = await api.post('/api/auth/login', { email, password });
  return data.data;
}
export async function register(payload: { name: string; email: string; password: string }): Promise<AuthResponse> {
  const { data } = await api.post('/api/auth/register', payload);
  return data.data;
}
