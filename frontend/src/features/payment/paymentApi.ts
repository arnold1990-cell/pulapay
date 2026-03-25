import api from '../../lib/axios';
import type { CreatePaymentRequest, Payment } from './paymentTypes';

export async function createPayment(payload: CreatePaymentRequest): Promise<Payment> {
  const { data } = await api.post<{ data: Payment } | Payment>('/api/v1/payments', payload);
  return 'data' in data ? data.data : data;
}

export async function listPayments(): Promise<Payment[]> {
  const { data } = await api.get<{ data: Payment[] } | Payment[]>('/api/v1/payments');
  return 'data' in data ? data.data : data;
}
