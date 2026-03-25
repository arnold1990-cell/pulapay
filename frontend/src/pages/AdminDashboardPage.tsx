import { useEffect, useState } from 'react';
import Card from '../components/ui/Card';
import { listPayments } from '../features/payment/paymentApi';
import { listTransactions } from '../features/transaction/transactionApi';

export default function AdminDashboardPage() {
  const [stats, setStats] = useState({ transactions: 0, payments: 0, pendingTransactions: 0 });

  useEffect(() => {
    const loadStats = async () => {
      try {
        const [transactionList, paymentList] = await Promise.all([listTransactions(), listPayments()]);

        setStats({
          transactions: transactionList.length,
          payments: paymentList.length,
          pendingTransactions: transactionList.filter((transaction) => transaction.status === 'PENDING').length
        });
      } catch {
        setStats({ transactions: 0, payments: 0, pendingTransactions: 0 });
      }
    };

    void loadStats();
  }, []);

  return (
    <div className="stats-grid">
      <Card>
        <p className="muted">Total Transactions</p>
        <h3>{stats.transactions}</h3>
      </Card>
      <Card>
        <p className="muted">Total Payments</p>
        <h3>{stats.payments}</h3>
      </Card>
      <Card>
        <p className="muted">Pending Transactions</p>
        <h3>{stats.pendingTransactions}</h3>
      </Card>
    </div>
  );
}
