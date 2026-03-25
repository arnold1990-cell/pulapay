import Button from '../ui/Button';
import { useAuth } from '../../context/AuthContext';

export default function Topbar() {
  const { user, logout } = useAuth();
  return <header style={{ display:'flex', justifyContent:'space-between', alignItems:'center', marginBottom:16 }}>
    <strong>Welcome, {user?.name}</strong>
    <Button onClick={logout}>Logout</Button>
  </header>;
}
