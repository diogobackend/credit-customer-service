ALTER TABLE customers
    ADD CONSTRAINT uk_customers_email UNIQUE (email);

ALTER TABLE customers
    ADD CONSTRAINT uk_customers_phone UNIQUE (phone);