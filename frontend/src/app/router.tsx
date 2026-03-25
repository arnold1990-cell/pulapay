import { Navigate, Outlet, createBrowserRouter } from 'react-router-dom';
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
  const { user } = useAuth();

  if (user?.role !== 'ADMIN') {
    return <Navigate to="/dashboard" replace />;
  }

  return <AdminDashboardPage />;
}

export const router = createBrowserRouter([
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
      { index: true, element: <Navigate to="/dashboard" replace /> },
      { path: 'dashboard', element: <DashboardPage /> },
      { path: 'wallet', element: <WalletPage /> },
      { path: 'transfer', element: <TransferPage /> },
      { path: 'transactions', element: <TransactionsPage /> },
      { path: 'payments', element: <PaymentsPage /> },
      { path: 'profile', element: <ProfilePage /> },
      { path: 'admin', element: <AdminRoute /> }
    ]
  },
  { path: '*', element: <Navigate to="/dashboard" replace /> }
]);
