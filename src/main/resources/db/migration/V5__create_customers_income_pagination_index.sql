CREATE INDEX idx_customers_income_created_at_customer_id
    ON customers (income, created_at DESC, customer_id DESC);