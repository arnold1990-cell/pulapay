-- Seed local admin with known credentials for local/dev auth verification.
-- email: admin@pulapay.com
-- password: admin123
INSERT INTO users (full_name, phone_number, email, password, role, active, created_at, updated_at)
VALUES (
  'PulaPay Admin',
  '26770000000',
  'admin@pulapay.com',
  '$2b$12$1TulO3H/AYH7wo26/vcTqutmM7UAk9AyqorZL0ezd2ocaI1dheKWy',
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
