import { useEffect, useState } from 'react';
import Card from '../components/ui/Card';
import Table from '../components/ui/Table';
import { listTransactions } from '../features/transaction/transactionApi';
import type { Transaction } from '../features/transaction/transactionTypes';
import { currency } from '../lib/utils';

export default function TransactionsPage() {
  const [transactions, setTransactions] = useState<Transaction[]>([]);
  const [isLoading, setIsLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    const loadTransactions = async () => {
      setIsLoading(true);
      setError('');

      try {
        const data = await listTransactions();
        setTransactions(data);
      } catch {
        setError('Unable to load transactions.');
      } finally {
        setIsLoading(false);
      }
    };

    void loadTransactions();
  }, []);

  return (
    <Card>
      <h3>Transaction History</h3>
      {isLoading ? <p>Loading transactions...</p> : null}
      {error ? <p className="form-error">{error}</p> : null}
      {!isLoading && !error ? (
        <Table
          data={transactions}
          columns={[
            { header: 'Date', cell: (row) => new Date(row.createdAt).toLocaleString() },
            { header: 'Type', cell: (row) => row.type },
            { header: 'Amount', cell: (row) => currency(row.amount) },
            { header: 'Status', cell: (row) => row.status },
            { header: 'Reference', cell: (row) => row.reference }
          ]}
          emptyMessage="No transactions available yet."
        />
      ) : null}
    </Card>
  );
}
