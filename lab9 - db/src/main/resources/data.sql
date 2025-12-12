-- Products
INSERT INTO products (name, price) VALUES ('iPhone 17 Pro Max 256GB', 92990.99);
INSERT INTO products (name, price) VALUES ('Nintendo Switch 2', 26554.50);
INSERT INTO products (name, price) VALUES ('iPad Pro M5', 72990.00);

-- Customers
INSERT INTO customers (name, email, phone) VALUES ('Sean Ivan Fabia', 'sean.ivan.fabia@email.com', '09123456789');
INSERT INTO customers (name, email, phone) VALUES ('Mariel Kimberly Novio', 'mariel.kimberly.novio@email.com', '09123456788');
INSERT INTO customers (name, email, phone) VALUES ('Holly Patches', 'holly.patches@email.com', '09123456787');

-- Invoices
INSERT INTO invoices (invoice_date, total_amount, customer_id) VALUES ('2025-01-15 10:30:00', 92990.99, 1);
INSERT INTO invoices (invoice_date, total_amount, customer_id) VALUES ('2025-02-20 14:45:00', 99544.50, 2);
INSERT INTO invoices (invoice_date, total_amount, customer_id) VALUES ('2025-03-10 09:15:00', 26554.50, 3);

-- Invoice-Product relationships
INSERT INTO invoice_products (invoice_id, product_id) VALUES (1, 1);
INSERT INTO invoice_products (invoice_id, product_id) VALUES (2, 2);
INSERT INTO invoice_products (invoice_id, product_id) VALUES (2, 3);
INSERT INTO invoice_products (invoice_id, product_id) VALUES (3, 2);
