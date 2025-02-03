-- создаём таблицу addresses
CREATE TABLE IF NOT EXISTS addresses (
    id UUID PRIMARY KEY,
    country VARCHAR,
    city VARCHAR,
    street VARCHAR,
    house VARCHAR,
    flat VARCHAR
);

-- создаём таблицу orders
CREATE TABLE IF NOT EXISTS orders (
    id UUID PRIMARY KEY,
    shopping_cart_id UUID,
    payment_id UUID,
    delivery_id UUID,
    username VARCHAR,
    state INT,
    delivery_volume FLOAT,
    delivery_weight FLOAT,
    fragile BOOLEAN,
    total_price FLOAT,
    delivery_price FLOAT,
    product_price FLOAT,
    address_id UUID REFERENCES addresses(id)
);

-- создаём таблицу products
CREATE TABLE IF NOT EXISTS products (
    order_id UUID REFERENCES orders(id),
    product_id UUID,
    quantity INT,
    PRIMARY KEY (order_id, product_id)
);
