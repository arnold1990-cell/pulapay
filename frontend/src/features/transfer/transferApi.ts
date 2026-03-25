import api from '../../lib/axios';
import type { TransferRequest, TransferResponse } from './transferTypes';

export async function submitTransfer(payload: TransferRequest): Promise<TransferResponse> {
  const { data } = await api.post<{ data: TransferResponse } | TransferResponse>('/api/v1/transfers', payload);
  return 'data' in data ? data.data : data;
}
