-- SQL script to reset the admin user in the database
-- This script ensures that the admin user can log in with a known password.

UPDATE users
SET password = 'new_secure_password'
WHERE username = 'admin';