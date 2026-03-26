import axios, { AxiosError } from 'axios';
import type { ApiError } from '../features/auth/authTypes';

const api = axios.create({
  baseURL: 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
    Accept: 'application/json'
  }
});

api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');

  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }

  return config;
});

api.interceptors.response.use(
  (response) => response,
  (error: AxiosError<ApiError>) => {
    const backendMessage = error.response?.data?.message;
    if (backendMessage) {
      error.message = backendMessage;
    }

    if (error.response?.status === 401) {
      localStorage.removeItem('accessToken');
      localStorage.removeItem('authUser');

      const isAuthEndpoint = error.config?.url?.includes('/api/auth/login') || error.config?.url?.includes('/api/auth/register');
      if (!isAuthEndpoint && !window.location.pathname.startsWith('/login')) {
        window.location.href = '/login';
      }
    }

    return Promise.reject(error);
  }
);

export default api;
