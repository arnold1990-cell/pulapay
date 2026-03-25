import { NavLink } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

type NavItem = {
  label: string;
  to: string;
  adminOnly?: boolean;
};

const navItems: NavItem[] = [
  { label: 'Dashboard', to: '/dashboard' },
  { label: 'Wallet', to: '/wallet' },
  { label: 'Transfer', to: '/transfer' },
  { label: 'Transactions', to: '/transactions' },
  { label: 'Payments', to: '/payments' },
  { label: 'Profile', to: '/profile' },
  { label: 'Admin Dashboard', to: '/admin', adminOnly: true }
];

export default function Sidebar() {
  const { user } = useAuth();

  return (
    <aside className="sidebar">
      <div className="sidebar-brand">
        <h1>PulaPay</h1>
        <p>Wallet & Payments</p>
      </div>

      <nav className="sidebar-nav" aria-label="Main navigation">
        {navItems
          .filter((item) => !item.adminOnly || user?.role === 'ADMIN')
          .map((item) => (
            <NavLink
              key={item.to}
              to={item.to}
              className={({ isActive }) => `nav-link ${isActive ? 'active' : ''}`.trim()}
            >
              {item.label}
            </NavLink>
          ))}
      </nav>
    </aside>
  );
}
