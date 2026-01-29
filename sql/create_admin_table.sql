CREATE TABLE IF NOT EXISTS `admin_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL UNIQUE,
  `password` varchar(255) NOT NULL,
  `role` varchar(20) DEFAULT 'ADMIN',
  `last_login` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- Password is 'admin123' hashed with SHA-256
-- SHA-256('admin123') = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9

INSERT INTO admin_users (username, password, role) 
SELECT * FROM (SELECT 'admin' AS u, '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9' AS p, 'ADMIN' AS r) AS tmp
WHERE NOT EXISTS (
    SELECT username FROM admin_users WHERE username = 'admin'
) LIMIT 1;
