import { useEffect, useMemo, useState } from 'react';
import Card from '../components/ui/Card';
import Table from '../components/ui/Table';
import { listPayments } from '../features/payment/paymentApi';
import { listTransactions } from '../features/transaction/transactionApi';
import type { Transaction } from '../features/transaction/transactionTypes';
import { getWallet } from '../features/wallet/walletApi';
import { currency } from '../lib/utils';

export default function DashboardPage() {
  const [walletBalance, setWalletBalance] = useState(0);
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [paymentsCount, setPaymentsCount] = useState(0);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const load = async () => {
      setIsLoading(true);
      setError('');

      try {
        const [wallet, transactionList, paymentList] = await Promise.all([
          getWallet(),
          listTransactions(),
          listPayments()
        ]);

        setWalletBalance(wallet.balance);
        setTransactions(transactionList);
        setPaymentsCount(paymentList.length);
      } catch {
        setError('Unable to load dashboard insights right now.');
      } finally {
        setIsLoading(false);
      }
    };

    void load();
  }, []);

  const totalTransfers = useMemo(
    () => transactions.filter((transaction) => transaction.type.toUpperCase().includes('TRANSFER')).length,
    [transactions]
  );

  return (
    <div className="stack-lg">
      <div className="stats-grid">
        <Card>
          <p className="muted">Current Balance</p>
          <h3>{currency(walletBalance)}</h3>
        </Card>
        <Card>
          <p className="muted">Total Transfers</p>
          <h3>{totalTransfers}</h3>
        </Card>
        <Card>
          <p className="muted">Total Transactions</p>
          <h3>{transactions.length}</h3>
        </Card>
        <Card>
          <p className="muted">Recent Payments</p>
          <h3>{paymentsCount}</h3>
        </Card>
      </div>

      <Card>
        <h3>Recent Transactions</h3>
        {isLoading ? <p>Loading data...</p> : null}
        {error ? <p className="form-error">{error}</p> : null}
        {!isLoading && !error ? (
          <Table
            data={transactions.slice(0, 5)}
            columns={[
              { header: 'Date', cell: (row) => new Date(row.createdAt).toLocaleString() },
              { header: 'Type', cell: (row) => row.type },
              { header: 'Amount', cell: (row) => currency(row.amount) },
              { header: 'Status', cell: (row) => row.status },
              { header: 'Reference', cell: (row) => row.reference }
            ]}
            emptyMessage="No recent transactions found."
          />
        ) : null}
      </Card>
    </div>
  );
}
