-- создаём таблицу products
CREATE TABLE IF NOT EXISTS products (
    id UUID PRIMARY KEY,
    weight FLOAT,
    width FLOAT,
    height FLOAT,
    depth FLOAT,
    fragile BOOLEAN,
    quantity INT
);


-- создаём таблицу orderBooking
CREATE TABLE IF NOT EXISTS order_booking (
    order_id UUID PRIMARY KEY,
    delivery_id UUID
);


-- создаём таблицу productBooking
CREATE TABLE IF NOT EXISTS product_booking (
    order_id UUID,
    product_id UUID,
    quantity int,
    PRIMARY KEY (order_id, product_id)
);