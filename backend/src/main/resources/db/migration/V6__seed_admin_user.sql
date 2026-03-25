INSERT INTO users (full_name, phone_number, email, password, role, active, created_at, updated_at)
VALUES ('PulaPay Admin', '26770000000', 'admin@pulapay.com', '$2a$10$17f3rdR30nSEBooA4L6wYec9yx9F4v4jVvBlt3F4iAfDdcQa1JiXq', 'ADMIN', true, NOW(), NOW())
ON CONFLICT (phone_number) DO NOTHING;

INSERT INTO wallets (wallet_number, user_id, balance, currency, status, created_at, updated_at)
SELECT '2600000001', u.id, 1000000.00, 'BWP', 'ACTIVE', NOW(), NOW() FROM users u WHERE u.phone_number='26770000000'
ON CONFLICT (wallet_number) DO NOTHING;
