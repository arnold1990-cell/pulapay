import { createBrowserRouter, Navigate } from 'react-router-dom';
import AppLayout from '../components/layout/AppLayout';
import LoginPage from '../pages/LoginPage';
import RegisterPage from '../pages/RegisterPage';
import HomePage from '../pages/HomePage';
import DashboardPage from '../pages/DashboardPage';
import WalletPage from '../pages/WalletPage';
import TransferPage from '../pages/TransferPage';
import TransactionsPage from '../pages/TransactionsPage';
import PaymentsPage from '../pages/PaymentsPage';
import ProfilePage from '../pages/ProfilePage';
import AdminDashboardPage from '../pages/AdminDashboardPage';

export const router = createBrowserRouter([
  { path: '/', element: <HomePage /> },
  { path: '/login', element: <LoginPage /> },
  { path: '/register', element: <RegisterPage /> },
  {
    path: '/app', element: <AppLayout />, children: [
      { index: true, element: <Navigate to="/dashboard" replace /> },
      { path: 'dashboard', element: <DashboardPage /> },
      { path: 'wallet', element: <WalletPage /> },
      { path: 'transfer', element: <TransferPage /> },
      { path: 'transactions', element: <TransactionsPage /> },
      { path: 'payments', element: <PaymentsPage /> },
      { path: 'profile', element: <ProfilePage /> },
      { path: 'admin', element: <AdminDashboardPage /> }
    ]
  },
  { path: '/dashboard', element: <Navigate to="/app/dashboard" replace /> },
  { path: '/wallet', element: <Navigate to="/app/wallet" replace /> },
  { path: '/transfer', element: <Navigate to="/app/transfer" replace /> },
  { path: '/transactions', element: <Navigate to="/app/transactions" replace /> },
  { path: '/payments', element: <Navigate to="/app/payments" replace /> },
  { path: '/profile', element: <Navigate to="/app/profile" replace /> },
  { path: '/admin', element: <Navigate to="/app/admin" replace /> }
]);
