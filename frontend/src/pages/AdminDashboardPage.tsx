import { useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { Navigate } from 'react-router-dom';
import api from '../lib/axios';
import Input from '../components/ui/Input';
import Button from '../components/ui/Button';

export default function AdminDashboardPage() {
  const { user } = useAuth();
  const [walletNumber, setWallet] = useState('');
  const [amount, setAmount] = useState('');
  if (user?.role !== 'ADMIN') return <Navigate to="/dashboard" replace />;
  return <div className="grid"><h2>Admin Dashboard</h2><Input placeholder="Wallet number" value={walletNumber} onChange={e=>setWallet(e.target.value)} /><Input placeholder="Amount" value={amount} onChange={e=>setAmount(e.target.value)} /><Button onClick={async()=>{ await api.post('/api/v1/admin/fund-wallet', { walletNumber, amount:Number(amount) }); }}>Fund Wallet</Button></div>;
}
