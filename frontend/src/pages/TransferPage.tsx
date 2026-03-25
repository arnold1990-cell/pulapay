import { useState } from 'react';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { submitTransfer } from '../features/transfer/transferApi';

export default function TransferPage() {
  const [recipientPhoneNumber, setRecipient] = useState('');
  const [amount, setAmount] = useState('');
  const [narration, setNarration] = useState('');
  const [message, setMessage] = useState('');
  return <div className="grid"><h2>Transfer</h2><Input placeholder="Recipient phone" value={recipientPhoneNumber} onChange={e=>setRecipient(e.target.value)} /><Input placeholder="Amount" value={amount} onChange={e=>setAmount(e.target.value)} /><Input placeholder="Narration" value={narration} onChange={e=>setNarration(e.target.value)} /><Button onClick={async()=>{ try { await submitTransfer({ recipientPhoneNumber, amount:Number(amount), narration }); setMessage('Transfer successful'); } catch { setMessage('Transfer failed'); } }}>Send</Button><p>{message}</p></div>;
}
