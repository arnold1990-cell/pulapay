export type Wallet = {
  walletId: string;
  walletNumber: string;
  balance: number;
  currency: string;
  status: string;
  updatedAt?: string;
};

export type WalletSummary = {
  balance: number;
  currency: string;
};
