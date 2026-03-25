import { useEffect, useState } from 'react';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { createPayment, listPayments } from '../features/payment/paymentApi';
import type { Payment } from '../features/payment/paymentTypes';

export default function PaymentsPage() {
  const [form, setForm] = useState({ amount:'', merchantName:'', merchantReference:'' });
  const [payments, setPayments] = useState<Payment[]>([]);
  const load = async() => setPayments(await listPayments());
  useEffect(() => { load(); }, []);
  return <div><h2>Payments</h2><div className="grid"><Input placeholder="Merchant" value={form.merchantName} onChange={e=>setForm({ ...form, merchantName:e.target.value })}/><Input placeholder="Merchant ref" value={form.merchantReference} onChange={e=>setForm({ ...form, merchantReference:e.target.value })}/><Input placeholder="Amount" value={form.amount} onChange={e=>setForm({ ...form, amount:e.target.value })}/><Button onClick={async()=>{ await createPayment({ merchantName: form.merchantName, merchantReference: form.merchantReference, amount:Number(form.amount) }); await load(); }}>Pay</Button></div>{payments.map(p=><p key={p.reference}>{p.reference} - {p.status}</p>)}</div>;
}
