export type Transaction = {
  id?: string;
  reference: string;
  type: 'DEBIT' | 'CREDIT' | string;
  amount: number;
  status: 'PENDING' | 'SUCCESS' | 'FAILED' | string;
  description?: string;
  createdAt: string;
};
