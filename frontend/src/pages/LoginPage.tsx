import { AxiosError } from 'axios';
import { FormEvent, useState } from 'react';
import { Link, Navigate, useNavigate } from 'react-router-dom';
import Button from '../components/ui/Button';
import Card from '../components/ui/Card';
import Input from '../components/ui/Input';
import type { ApiError } from '../features/auth/authTypes';
import { useAuth } from '../context/AuthContext';

function resolveErrorMessage(error: unknown): string {
  const axiosError = error as AxiosError<ApiError>;
  const errorFields = axiosError.response?.data?.errors;
  if (errorFields && Object.keys(errorFields).length > 0) {
    return Object.values(errorFields).join(', ');
  }
  return axiosError.response?.data?.message ?? axiosError.message ?? 'Login failed. Please try again.';
}

export default function LoginPage() {
  const navigate = useNavigate();
  const { login, isAuthenticated } = useAuth();

  const [form, setForm] = useState({ email: '', password: '' });
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  const onSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError('');
    setIsSubmitting(true);

    try {
      await login(form);
      navigate('/dashboard', { replace: true });
    } catch (submitError) {
      if (import.meta.env.DEV) {
        console.error('Login request failed', { error: submitError });
      }
      setError(resolveErrorMessage(submitError));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="auth-page">
      <Card className="auth-card">
        <h1>PulaPay</h1>
        <p>Securely access your wallet and payments dashboard.</p>

        <form onSubmit={onSubmit} className="auth-form">
          <Input
            label="Email"
            name="email"
            type="email"
            autoComplete="email"
            value={form.email}
            onChange={(event) => setForm((prev) => ({ ...prev, email: event.target.value }))}
            required
          />
          <Input
            label="Password"
            name="password"
            type="password"
            autoComplete="current-password"
            value={form.password}
            onChange={(event) => setForm((prev) => ({ ...prev, password: event.target.value }))}
            required
          />

          {error ? <p className="form-error">{error}</p> : null}

          <Button type="submit" isLoading={isSubmitting}>
            Sign In
          </Button>
        </form>

        <p className="auth-switch">
          New to PulaPay? <Link to="/register">Create an account</Link>
        </p>
      </Card>
    </div>
  );
}
