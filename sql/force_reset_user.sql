-- Force delete and recreate the admin user to GUARANTEE it exists
DELETE FROM admin_users WHERE username = 'admin';

INSERT INTO admin_users (username, password, role) 
VALUES ('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'ADMIN');

SELECT * FROM admin_users;
