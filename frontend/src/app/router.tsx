import { Link, Navigate, Outlet, createBrowserRouter } from 'react-router-dom';
import AppLayout from '../components/layout/AppLayout';
import { useAuth } from '../context/AuthContext';
import AdminDashboardPage from '../pages/AdminDashboardPage';
import DashboardPage from '../pages/DashboardPage';
import LoginPage from '../pages/LoginPage';
import PaymentsPage from '../pages/PaymentsPage';
import ProfilePage from '../pages/ProfilePage';
import RegisterPage from '../pages/RegisterPage';
import TransactionsPage from '../pages/TransactionsPage';
import TransferPage from '../pages/TransferPage';
import WalletPage from '../pages/WalletPage';

function FullPageLoader() {
  return <div className="page-loader">Loading your account...</div>;
}

function RootRedirect() {
  const { isAuthenticated, authReady } = useAuth();

  if (!authReady) {
    return <FullPageLoader />;
  }

  return <Navigate to={isAuthenticated ? '/dashboard' : '/login'} replace />;
}

function PublicOnlyRoute() {
  const { isAuthenticated, authReady } = useAuth();

  if (!authReady) {
    return <FullPageLoader />;
  }

  if (isAuthenticated) {
    return <Navigate to="/dashboard" replace />;
  }

  return <Outlet />;
}

function ProtectedRoute() {
  const { isAuthenticated, authReady } = useAuth();

  if (!authReady) {
    return <FullPageLoader />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  return <AppLayout />;
}

function AdminRoute() {
  const { user, authReady, isAuthenticated } = useAuth();

  if (!authReady) {
    return <FullPageLoader />;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (user?.role !== 'ADMIN') {
    return <Navigate to="/dashboard" replace />;
  }

  return <AdminDashboardPage />;
}

function NotFoundPage() {
  return (
    <div className="auth-page">
      <div className="card auth-card stack-md">
        <h1>Page not found</h1>
        <p className="muted">The page you requested does not exist.</p>
        <div className="actions-row">
          <Link className="quick-link" to="/">
            Go to home
          </Link>
          <Link className="quick-link" to="/dashboard">
            Open dashboard
          </Link>
        </div>
      </div>
    </div>
  );
}

export const router = createBrowserRouter([
  { path: '/', element: <RootRedirect /> },
  {
    element: <PublicOnlyRoute />,
    children: [
      { path: '/login', element: <LoginPage /> },
      { path: '/register', element: <RegisterPage /> }
    ]
  },
  {
    path: '/',
    element: <ProtectedRoute />,
    children: [
      { path: 'dashboard', element: <DashboardPage /> },
      { path: 'wallet', element: <WalletPage /> },
      { path: 'transfer', element: <TransferPage /> },
      { path: 'transactions', element: <TransactionsPage /> },
      { path: 'payments', element: <PaymentsPage /> },
      { path: 'profile', element: <ProfilePage /> },
      { path: 'admin', element: <AdminRoute /> }
    ]
  },
  { path: '*', element: <NotFoundPage /> }
]);
