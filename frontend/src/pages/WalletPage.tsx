import { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import Card from '../components/ui/Card';
import { getWallet } from '../features/wallet/walletApi';
import type { Wallet } from '../features/wallet/walletTypes';
import { currency } from '../lib/utils';

export default function WalletPage() {
  const [wallet, setWallet] = useState<Wallet | null>(null);
  const [error, setError] = useState('');
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const loadWallet = async () => {
      setIsLoading(true);
      setError('');

      try {
        const walletData = await getWallet();
        setWallet(walletData);
      } catch {
        setError('Unable to fetch wallet details.');
      } finally {
        setIsLoading(false);
      }
    };

    void loadWallet();
  }, []);

  return (
    <div className="stack-lg">
      <Card>
        <h3>Wallet Details</h3>
        {isLoading ? <p>Loading wallet...</p> : null}
        {error ? <p className="form-error">{error}</p> : null}
        {!isLoading && wallet ? (
          <div className="stack-sm">
            <p>
              <strong>Balance:</strong> {currency(wallet.balance, wallet.currency)}
            </p>
            <p>
              <strong>Wallet ID:</strong> {wallet.walletId || 'N/A'}
            </p>
            <p>
              <strong>Wallet Number:</strong> {wallet.walletNumber}
            </p>
            <p>
              <strong>Currency:</strong> {wallet.currency}
            </p>
            <p>
              <strong>Status:</strong> {wallet.status}
            </p>
          </div>
        ) : null}
      </Card>

      <Card>
        <h3>Quick Actions</h3>
        <div className="actions-row">
          <Link to="/transfer" className="quick-link">
            Send Money
          </Link>
          <Link to="/payments" className="quick-link">
            Pay Merchant
          </Link>
        </div>
      </Card>
    </div>
  );
}
