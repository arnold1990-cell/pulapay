CREATE TABLE wallets (
  id BIGSERIAL PRIMARY KEY,
  wallet_number VARCHAR(20) NOT NULL UNIQUE,
  user_id BIGINT NOT NULL UNIQUE REFERENCES users(id),
  balance NUMERIC(19,2) NOT NULL,
  currency VARCHAR(3) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL,
  updated_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_wallets_wallet_number ON wallets(wallet_number);
