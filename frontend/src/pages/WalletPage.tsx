import { useEffect, useState } from 'react';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';
import { fundWallet, getWallet } from '../features/wallet/walletApi';
import type { Wallet } from '../features/wallet/walletTypes';

export default function WalletPage() {
  const [wallet, setWallet] = useState<Wallet | null>(null);
  const [amount, setAmount] = useState('');
  useEffect(() => { getWallet().then(setWallet); }, []);
  return <div><h2>Wallet</h2><p>{wallet?.walletNumber} | {wallet?.currency}</p><h3>{wallet?.balance}</h3><Input value={amount} onChange={e => setAmount(e.target.value)} placeholder="Fund amount" /><Button onClick={async()=>{ await fundWallet(Number(amount)); setWallet(await getWallet()); }}>Fund</Button></div>;
}
