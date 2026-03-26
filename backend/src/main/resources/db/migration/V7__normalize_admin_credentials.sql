-- Ensure admin account always has known working credentials in existing environments.
-- email: admin@pulapay.com
-- password: admin123
UPDATE users
SET password = '$2b$12$1TulO3H/AYH7wo26/vcTqutmM7UAk9AyqorZL0ezd2ocaI1dheKWy',
    active = true,
    updated_at = NOW()
WHERE lower(email) = 'admin@pulapay.com';
