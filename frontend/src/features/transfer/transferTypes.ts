export type TransferRequest = {
  recipientIdentifier: string;
  amount: number;
  reference?: string;
};

export type TransferResponse = {
  reference: string;
  status: string;
  amount: number;
  createdAt: string;
};
