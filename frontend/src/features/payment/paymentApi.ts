import api from '../../lib/axios';
import type { Payment } from './paymentTypes';

export async function createPayment(payload: { amount: number; merchantName: string; merchantReference: string }): Promise<Payment> { const { data } = await api.post('/api/v1/payments', payload); return data.data; }
export async function listPayments(): Promise<Payment[]> { const { data } = await api.get('/api/v1/payments'); return data.data; }
