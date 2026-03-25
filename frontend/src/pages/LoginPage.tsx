import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { login } from '../features/auth/authApi';
import { useAuth } from '../context/AuthContext';

export default function LoginPage() {
  const [phoneNumber, setPhone] = useState('');
  const [password, setPass] = useState('');
  const [error, setError] = useState('');
  const { login: setAuth } = useAuth();
  const navigate = useNavigate();
  return <div className="container"><h2>Login</h2>{error && <p>{error}</p>}<div className="grid"><Input placeholder="Phone" value={phoneNumber} onChange={e => setPhone(e.target.value)} /><Input type="password" placeholder="Password" value={password} onChange={e => setPass(e.target.value)} /><Button onClick={async () => { try { const res = await login(phoneNumber, password); setAuth(res.accessToken, res); navigate('/dashboard'); } catch { setError('Invalid credentials'); } }}>Login</Button><Link to="/register">Create account</Link></div></div>;
}
