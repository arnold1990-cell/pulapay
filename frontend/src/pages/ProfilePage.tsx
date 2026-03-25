import { useEffect, useState } from 'react';
import api from '../lib/axios';
import Input from '../components/ui/Input';
import Button from '../components/ui/Button';

export default function ProfilePage() {
  const [profile, setProfile] = useState({ fullName:'', email:'', phoneNumber:'' });
  useEffect(() => { api.get('/api/v1/users/me').then(r => setProfile(r.data.data)); }, []);
  return <div className="grid"><h2>Profile</h2><Input value={profile.fullName} onChange={e=>setProfile({ ...profile, fullName:e.target.value })} /><Input value={profile.email} onChange={e=>setProfile({ ...profile, email:e.target.value })} /><Input value={profile.phoneNumber} disabled /><Button onClick={async()=>{ await api.put('/api/v1/users/me', { fullName: profile.fullName, email: profile.email }); }}>Save</Button></div>;
}
