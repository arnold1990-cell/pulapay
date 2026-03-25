import { FormEvent, useEffect, useState } from 'react';
import Button from '../components/ui/Button';
import Card from '../components/ui/Card';
import Input from '../components/ui/Input';
import Table from '../components/ui/Table';
import { createPayment, listPayments } from '../features/payment/paymentApi';
import type { Payment } from '../features/payment/paymentTypes';
import { currency } from '../lib/utils';

export default function PaymentsPage() {
  const [payments, setPayments] = useState<Payment[]>([]);
  const [form, setForm] = useState({ merchantName: '', merchantReference: '', amount: '' });
  const [isLoading, setIsLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);
  const [error, setError] = useState('');

  const loadPayments = async () => {
    setIsLoading(true);

    try {
      const data = await listPayments();
      setPayments(data);
    } catch {
      setError('Unable to load payments.');
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    void loadPayments();
  }, []);

  const onSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError('');

    const amount = Number(form.amount);

    if (!form.merchantName.trim() || !form.merchantReference.trim()) {
      setError('Merchant name and reference are required.');
      return;
    }

    if (!Number.isFinite(amount) || amount <= 0) {
      setError('Enter a valid payment amount.');
      return;
    }

    setIsSubmitting(true);

    try {
      await createPayment({
        merchantName: form.merchantName,
        merchantReference: form.merchantReference,
        amount
      });
      setForm({ merchantName: '', merchantReference: '', amount: '' });
      await loadPayments();
    } catch {
      setError('Payment request failed. Please retry.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <div className="stack-lg">
      <Card>
        <h3>Initiate Payment</h3>
        <form className="stack-md" onSubmit={onSubmit}>
          <Input
            label="Merchant name"
            value={form.merchantName}
            onChange={(event) => setForm((prev) => ({ ...prev, merchantName: event.target.value }))}
            required
          />
          <Input
            label="Merchant reference"
            value={form.merchantReference}
            onChange={(event) => setForm((prev) => ({ ...prev, merchantReference: event.target.value }))}
            required
          />
          <Input
            label="Amount"
            type="number"
            min="0"
            step="0.01"
            value={form.amount}
            onChange={(event) => setForm((prev) => ({ ...prev, amount: event.target.value }))}
            required
          />
          {error ? <p className="form-error">{error}</p> : null}
          <Button type="submit" isLoading={isSubmitting}>
            Create Payment
          </Button>
        </form>
      </Card>

      <Card>
        <h3>Payments</h3>
        {isLoading ? <p>Loading payments...</p> : null}
        {!isLoading ? (
          <Table
            data={payments}
            columns={[
              { header: 'Date', cell: (row) => new Date(row.createdAt).toLocaleString() },
              { header: 'Merchant', cell: (row) => row.merchantName },
              { header: 'Amount', cell: (row) => currency(row.amount) },
              { header: 'Status', cell: (row) => row.status },
              { header: 'Reference', cell: (row) => row.reference }
            ]}
            emptyMessage="No payment records available yet."
          />
        ) : null}
      </Card>
    </div>
  );
}
