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
  return axiosError.response?.data?.message ?? axiosError.message ?? 'Registration failed. Please try again.';
}

export default function RegisterPage() {
  const navigate = useNavigate();
  const { register, isAuthenticated } = useAuth();

  const [form, setForm] = useState({ name: '', email: '', password: '', confirmPassword: '' });
  const [error, setError] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  const onSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError('');

    if (form.password !== form.confirmPassword) {
      setError('Passwords do not match.');
      return;
    }

    setIsSubmitting(true);

    try {
      await register({ name: form.name, email: form.email, password: form.password });
      navigate('/login', { replace: true });
    } catch (submitError) {
      if (import.meta.env.DEV) {
        console.error('Registration request failed', submitError);
      }
      setError(resolveErrorMessage(submitError));
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="auth-page">
      <Card className="auth-card">
        <h1>Create your PulaPay account</h1>
        <p>Start sending money and paying merchants in seconds.</p>

        <form onSubmit={onSubmit} className="auth-form">
          <Input
            label="Full name"
            name="name"
            value={form.name}
            onChange={(event) => setForm((prev) => ({ ...prev, name: event.target.value }))}
            required
          />
          <Input
            label="Email"
            name="email"
            type="email"
            value={form.email}
            onChange={(event) => setForm((prev) => ({ ...prev, email: event.target.value }))}
            required
          />
          <Input
            label="Password"
            name="password"
            type="password"
            value={form.password}
            onChange={(event) => setForm((prev) => ({ ...prev, password: event.target.value }))}
            required
            minLength={8}
          />
          <Input
            label="Confirm password"
            name="confirmPassword"
            type="password"
            value={form.confirmPassword}
            onChange={(event) => setForm((prev) => ({ ...prev, confirmPassword: event.target.value }))}
            required
            minLength={8}
          />

          {error ? <p className="form-error">{error}</p> : null}

          <Button type="submit" isLoading={isSubmitting}>
            Register
          </Button>
        </form>

        <p className="auth-switch">
          Already have an account? <Link to="/login">Sign in</Link>
        </p>
      </Card>
    </div>
  );
}
