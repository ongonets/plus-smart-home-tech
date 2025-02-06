-- создаём таблицу payments
CREATE TABLE IF NOT EXISTS payments (
    id UUID PRIMARY KEY,
    order_id UUID,
    total_payment FLOAT,
    delivery_total FLOAT,
    product_total FLOAT,
    state INT
);