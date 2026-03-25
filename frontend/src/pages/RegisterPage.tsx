import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { register } from '../features/auth/authApi';

export default function RegisterPage() {
  const [form, setForm] = useState({ name:'', email:'', password:'' });
  const navigate = useNavigate();
  return <div className="container"><h2>Register</h2><div className="grid">
    <Input placeholder="Name" value={form.name} onChange={e => setForm({ ...form, name:e.target.value })} />
    <Input placeholder="Email" value={form.email} onChange={e => setForm({ ...form, email:e.target.value })} />
    <Input type="password" placeholder="Password" value={form.password} onChange={e => setForm({ ...form, password:e.target.value })} />
    <Button onClick={async () => { await register(form); navigate('/login'); }}>Create account</Button>
  </div></div>;
}
