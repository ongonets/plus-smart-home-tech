-- создаём таблицу carts
CREATE TABLE IF NOT EXISTS carts (
    id UUID PRIMARY KEY,
    username VARCHAR,
    state INT
);

-- создаём таблицу carts
CREATE TABLE IF NOT EXISTS products (
    cart_id UUID REFERENCES carts(id),
    product_id UUID,
    quantity INT,
    PRIMARY KEY (cart_id, product_id)
);