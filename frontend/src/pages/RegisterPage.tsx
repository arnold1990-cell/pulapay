import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { register } from '../features/auth/authApi';
import { useAuth } from '../context/AuthContext';

export default function RegisterPage() {
  const [form, setForm] = useState({ fullName:'', phoneNumber:'', email:'', password:'' });
  const { login } = useAuth();
  const navigate = useNavigate();
  return <div className="container"><h2>Register</h2><div className="grid">
    <Input placeholder="Full name" value={form.fullName} onChange={e => setForm({ ...form, fullName:e.target.value })} />
    <Input placeholder="Phone" value={form.phoneNumber} onChange={e => setForm({ ...form, phoneNumber:e.target.value })} />
    <Input placeholder="Email" value={form.email} onChange={e => setForm({ ...form, email:e.target.value })} />
    <Input type="password" placeholder="Password" value={form.password} onChange={e => setForm({ ...form, password:e.target.value })} />
    <Button onClick={async () => { const res = await register(form); login(res.accessToken, res); navigate('/dashboard'); }}>Create account</Button>
  </div></div>;
}
