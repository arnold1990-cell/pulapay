import { useEffect, useState } from 'react';
import Table from '../components/ui/Table';
import { listTransactions } from '../features/transaction/transactionApi';
import type { Txn } from '../features/transaction/transactionTypes';

export default function TransactionsPage() {
  const [data, setData] = useState<Txn[]>([]);
  useEffect(() => { listTransactions().then(setData); }, []);
  return <div><h2>Transactions</h2><Table headers={['Reference','Type','Amount','Status','Date']} rows={data.map(t => [t.reference, t.type, t.amount, t.status, new Date(t.createdAt).toLocaleString()])} /></div>;
}
