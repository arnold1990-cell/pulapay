import api from '../../lib/axios';
import type { TransferPayload } from './transferTypes';

export async function submitTransfer(payload: TransferPayload) { const { data } = await api.post('/api/v1/transfers', payload); return data.data; }
