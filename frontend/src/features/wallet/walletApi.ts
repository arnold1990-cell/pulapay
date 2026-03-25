import api from '../../lib/axios';
import type { Wallet } from './walletTypes';

export async function getWallet(): Promise<Wallet> { const { data } = await api.get('/api/v1/wallet'); return data.data; }
export async function fundWallet(amount: number): Promise<Wallet> { const { data } = await api.post('/api/v1/wallet/fund', { amount }); return data.data; }
