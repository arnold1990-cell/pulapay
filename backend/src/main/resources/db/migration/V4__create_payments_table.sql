CREATE TABLE payments (
  id BIGSERIAL PRIMARY KEY,
  reference VARCHAR(50) NOT NULL UNIQUE,
  payer_wallet_id BIGINT NOT NULL REFERENCES wallets(id),
  amount NUMERIC(19,2) NOT NULL,
  merchant_name VARCHAR(255) NOT NULL,
  merchant_reference VARCHAR(255) NOT NULL,
  status VARCHAR(20) NOT NULL,
  created_at TIMESTAMP WITH TIME ZONE NOT NULL
);
CREATE INDEX idx_payments_wallet ON payments(payer_wallet_id);
