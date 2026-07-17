CREATE INDEX idx_customers_status_created_at_customer_id
    ON customers (status, created_at DESC, customer_id DESC);