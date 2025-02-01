-- создаём таблицу addresses
CREATE TABLE IF NOT EXISTS addresses (
    id UUID PRIMARY KEY,
    country VARCHAR,
    city VARCHAR,
    street VARCHAR,
    house VARCHAR,
    flat VARCHAR
);

-- создаём таблицу delivery
CREATE TABLE IF NOT EXISTS delivery (
    id UUID PRIMARY KEY,
    order_id UUID,
    state INT,
    volume FLOAT,
    weight FLOAT,
    fragile BOOLEAN,
    from_address_id UUID REFERENCES addresses(id),
    to_address_id UUID REFERENCES addresses(id)
);


