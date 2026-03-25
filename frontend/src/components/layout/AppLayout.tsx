import { Outlet, Navigate } from 'react-router-dom';
import Sidebar from './Sidebar';
import Topbar from './Topbar';
import { useAuth } from '../../context/AuthContext';

export default function AppLayout() {
  const { token } = useAuth();
  if (!token) return <Navigate to="/login" replace />;
  return <div style={{ display:'flex' }}><Sidebar /><main className="container" style={{ flex:1 }}><Topbar /><Outlet /></main></div>;
}
