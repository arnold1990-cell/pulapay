import { useLocation } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';
import Button from '../ui/Button';

const pageTitles: Record<string, string> = {
  '/dashboard': 'Dashboard Overview',
  '/wallet': 'Wallet',
  '/transfer': 'Money Transfer',
  '/transactions': 'Transactions',
  '/payments': 'Payments',
  '/profile': 'Profile',
  '/admin': 'Admin Dashboard'
};

export default function Topbar() {
  const location = useLocation();
  const { user, logout } = useAuth();

  return (
    <header className="topbar">
      <div>
        <h2>{pageTitles[location.pathname] ?? 'PulaPay'}</h2>
        <p>Manage your finances securely.</p>
      </div>

      <div className="topbar-user">
        <div>
          <strong>{user?.fullName ?? 'User'}</strong>
          <span>{user?.email ?? ''}</span>
        </div>
        <Button variant="ghost" onClick={logout}>
          Logout
        </Button>
      </div>
    </header>
  );
}
