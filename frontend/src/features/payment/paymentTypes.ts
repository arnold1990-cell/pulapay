export type Payment = {
  id?: string;
  reference: string;
  amount: number;
  merchantName: string;
  merchantReference: string;
  status: 'PENDING' | 'SUCCESS' | 'FAILED' | string;
  createdAt: string;
};

export type CreatePaymentRequest = {
  amount: number;
  merchantName: string;
  merchantReference: string;
};
