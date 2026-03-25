import { useEffect, useState } from 'react';
import Card from '../components/ui/Card';
import { getWallet } from '../features/wallet/walletApi';
import { listTransactions } from '../features/transaction/transactionApi';
import type { Txn } from '../features/transaction/transactionTypes';
import { useAuth } from '../context/AuthContext';
import { currency } from '../lib/utils';

export default function DashboardPage() {
  const [balance, setBalance] = useState(0);
  const [walletNumber, setWallet] = useState('');
  const [txns, setTxns] = useState<Txn[]>([]);
  const { user } = useAuth();
  useEffect(() => { getWallet().then(w => { setBalance(w.balance); setWallet(w.walletNumber); }); listTransactions().then(setTxns); }, []);
  return <div className="grid"><Card><h3>{user?.fullName}</h3><p>Wallet {walletNumber}</p><h2>{currency(balance)}</h2></Card><Card><h4>Recent Transactions</h4>{txns.slice(0,5).map(t => <p key={t.reference}>{t.reference} - {t.status}</p>)}</Card></div>;
}
