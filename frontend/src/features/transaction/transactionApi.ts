import api from '../../lib/axios';
import type { Transaction } from './transactionTypes';

export async function listTransactions(): Promise<Transaction[]> {
  const { data } = await api.get<{ data: Transaction[] } | Transaction[]>('/api/v1/transactions');
  return 'data' in data ? data.data : data;
}
