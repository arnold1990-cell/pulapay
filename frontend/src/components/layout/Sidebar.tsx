import { Link } from 'react-router-dom';
import { useAuth } from '../../context/AuthContext';

export default function Sidebar() {
  const { user } = useAuth();
  return <aside style={{ width:220, background:'#0b2545', color:'#fff', padding:'1rem', minHeight:'100vh' }}>
    <h3>PulaPay</h3>
    <nav style={{ display:'grid', gap:8 }}>
      <Link to="/dashboard">Dashboard</Link><Link to="/wallet">Wallet</Link><Link to="/transfer">Transfer</Link>
      <Link to="/transactions">Transactions</Link><Link to="/payments">Payments</Link><Link to="/profile">Profile</Link>
      {user?.role === 'ADMIN' && <Link to="/admin">Admin</Link>}
    </nav>
  </aside>;
}
