import { createContext, useContext, useMemo, useState } from 'react';

type AuthState = { fullName: string; phoneNumber: string; role: 'USER' | 'ADMIN' } | null;
type AuthContextType = { user: AuthState; token: string | null; login: (token: string, user: NonNullable<AuthState>) => void; logout: () => void };
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: React.ReactNode }) {
  const [token, setToken] = useState<string | null>(localStorage.getItem('accessToken'));
  const [user, setUser] = useState<AuthState>(localStorage.getItem('authUser') ? JSON.parse(localStorage.getItem('authUser')!) : null);
  const value = useMemo(() => ({
    user, token,
    login: (t: string, u: NonNullable<AuthState>) => { localStorage.setItem('accessToken', t); localStorage.setItem('authUser', JSON.stringify(u)); setToken(t); setUser(u); },
    logout: () => { localStorage.removeItem('accessToken'); localStorage.removeItem('authUser'); setToken(null); setUser(null); }
  }), [user, token]);
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth() { const ctx = useContext(AuthContext); if (!ctx) throw new Error('useAuth must be used inside provider'); return ctx; }
