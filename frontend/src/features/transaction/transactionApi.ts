import api from '../../lib/axios';
import type { Txn } from './transactionTypes';

export async function listTransactions(): Promise<Txn[]> { const { data } = await api.get('/api/v1/transactions'); return data.data; }
