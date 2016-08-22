DELETE FROM customer;
INSERT INTO customer VALUES (0, unix_timestamp(now()), unix_timestamp(now()), 'john.doe@example.com', 'John', 'Doe', 'user');