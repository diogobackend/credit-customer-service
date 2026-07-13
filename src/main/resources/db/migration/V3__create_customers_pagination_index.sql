CREATE INDEX idx_customers_created_at_customer_id
    ON customers (created_at DESC, customer_id DESC);