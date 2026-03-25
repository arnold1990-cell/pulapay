import { FormEvent, useState } from 'react';
import Button from '../components/ui/Button';
import Card from '../components/ui/Card';
import Input from '../components/ui/Input';
import { submitTransfer } from '../features/transfer/transferApi';

export default function TransferPage() {
  const [form, setForm] = useState({ recipientIdentifier: '', amount: '', reference: '' });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  const onSubmit = async (event: FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setError('');
    setSuccess('');

    const amount = Number(form.amount);

    if (!form.recipientIdentifier.trim()) {
      setError('Recipient identifier is required.');
      return;
    }

    if (!Number.isFinite(amount) || amount <= 0) {
      setError('Enter a valid transfer amount.');
      return;
    }

    setIsSubmitting(true);

    try {
      const transfer = await submitTransfer({
        recipientIdentifier: form.recipientIdentifier,
        amount,
        reference: form.reference || undefined
      });

      setSuccess(`Transfer submitted successfully. Ref: ${transfer.reference}`);
      setForm({ recipientIdentifier: '', amount: '', reference: '' });
    } catch {
      setError('Transfer failed. Please try again.');
    } finally {
      setIsSubmitting(false);
    }
  };

  return (
    <Card>
      <h3>Transfer Funds</h3>
      <form className="stack-md" onSubmit={onSubmit}>
        <Input
          label="Recipient identifier"
          value={form.recipientIdentifier}
          onChange={(event) => setForm((prev) => ({ ...prev, recipientIdentifier: event.target.value }))}
          placeholder="Email, phone, or wallet number"
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
        <Input
          label="Reference (optional)"
          value={form.reference}
          onChange={(event) => setForm((prev) => ({ ...prev, reference: event.target.value }))}
        />

        {error ? <p className="form-error">{error}</p> : null}
        {success ? <p className="form-success">{success}</p> : null}

        <Button type="submit" isLoading={isSubmitting}>
          Send Transfer
        </Button>
      </form>
    </Card>
  );
}
