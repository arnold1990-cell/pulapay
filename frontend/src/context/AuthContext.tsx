import { createContext, useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { getCurrentUser, login as loginApi, register as registerApi } from '../features/auth/authApi';
import type { AuthUser, LoginRequest, RegisterRequest } from '../features/auth/authTypes';

type AuthContextType = {
  user: AuthUser | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  authReady: boolean;
  login: (payload: LoginRequest) => Promise<void>;
  register: (payload: RegisterRequest) => Promise<void>;
  logout: () => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

const AUTH_USER_KEY = 'authUser';
const ACCESS_TOKEN_KEY = 'accessToken';

function readStoredUser(): AuthUser | null {
  const storedUser = localStorage.getItem(AUTH_USER_KEY);

  if (!storedUser) {
    return null;
  }

  try {
    return JSON.parse(storedUser) as AuthUser;
  } catch {
    localStorage.removeItem(AUTH_USER_KEY);
    return null;
  }
}

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | null>(localStorage.getItem(ACCESS_TOKEN_KEY));
  const [user, setUser] = useState<AuthUser | null>(readStoredUser());
  const [isLoading, setIsLoading] = useState(true);

  const logout = useCallback(() => {
    localStorage.removeItem(ACCESS_TOKEN_KEY);
    localStorage.removeItem(AUTH_USER_KEY);
    setToken(null);
    setUser(null);
  }, []);

  useEffect(() => {
    const bootstrapAuth = async () => {
      const storedToken = localStorage.getItem(ACCESS_TOKEN_KEY);

      if (!storedToken) {
        setIsLoading(false);
        return;
      }

      setToken(storedToken);

      try {
        const currentUser = await getCurrentUser();
        setUser(currentUser);
        localStorage.setItem(AUTH_USER_KEY, JSON.stringify(currentUser));
      } catch {
        const fallbackUser = readStoredUser();

        if (fallbackUser) {
          setUser(fallbackUser);
        } else {
          logout();
        }
      } finally {
        setIsLoading(false);
      }
    };

    void bootstrapAuth();
  }, [logout]);

  const login = useCallback(async (payload: LoginRequest) => {
    const response = await loginApi(payload);
    localStorage.setItem(ACCESS_TOKEN_KEY, response.token);
    localStorage.setItem(AUTH_USER_KEY, JSON.stringify(response.user));
    setToken(response.token);
    setUser(response.user);
  }, []);

  const register = useCallback(async (payload: RegisterRequest) => {
    await registerApi(payload);
  }, []);

  const value = useMemo<AuthContextType>(
    () => ({
      user,
      token,
      isAuthenticated: Boolean(token),
      isLoading,
      authReady: !isLoading,
      login,
      register,
      logout
    }),
    [user, token, isLoading, login, register, logout]
  );

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider');
  }

  return context;
}
