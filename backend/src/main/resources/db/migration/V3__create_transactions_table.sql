CREATE TABLE transactions (
  id BIGSERIAL PRIMARY KEY,
  reference VARCHAR(50) NOT NULL UNIQUE,
  sender_wallet_id BIGINT REFERENCES wallets(id),
  receiver_wallet_id BIGINT REFERENCES wallets(id),
  amount NUMERIC(19,2) NOT NULL,
  fee NUMERIC(19,2) NOT NULL,
  net_amount NUMERIC(19,2) NOT NULL,
  type VARCHAR(20) NOT NULL,
  status VARCHAR(20) NOT NULL,
  narration VARCHAR(500),
  created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_transactions_reference ON transactions(reference);
