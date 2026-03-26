-- Seed local admin with known credentials for local/dev auth verification.
-- email: admin@pulapay.com
-- password: admin123
INSERT INTO users (full_name, phone_number, email, password, role, active, created_at, updated_at)
VALUES (
  'PulaPay Admin',
  '26770000000',
  'admin@pulapay.com',
  '$2y$12$jgYWOGu0PhY3MDi9dkwM4uFI3m2ibTqwMBDup9tJZMZp87d4mTew2',
  'ADMIN',
  true,
  NOW(),
  NOW()
)
ON CONFLICT (email) DO UPDATE
SET full_name = EXCLUDED.full_name,
    phone_number = EXCLUDED.phone_number,
    password = EXCLUDED.password,
    role = EXCLUDED.role,
    active = EXCLUDED.active,
    updated_at = NOW();

INSERT INTO wallets (wallet_number, user_id, balance, currency, status, created_at, updated_at)
SELECT '2600000001', u.id, 1000000.00, 'BWP', 'ACTIVE', NOW(), NOW()
FROM users u
WHERE u.email = 'admin@pulapay.com'
ON CONFLICT (wallet_number) DO NOTHING;
