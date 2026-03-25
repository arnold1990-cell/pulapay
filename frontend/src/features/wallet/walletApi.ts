import api from '../../lib/axios';
import type { Wallet } from './walletTypes';

export async function getWallet(): Promise<Wallet> {
  const { data } = await api.get<{ data: Wallet } | Wallet>('/api/v1/wallet');
  return 'data' in data ? data.data : data;
}
