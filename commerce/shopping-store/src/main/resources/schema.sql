-- создаём таблицу products
CREATE TABLE IF NOT EXISTS products (
    id UUID DEFAULT gen_random_uuid() PRIMARY KEY,
    name VARCHAR,
    description VARCHAR,
    src VARCHAR,
    quantity INT,
    state INT,
    rating BIGINT,
    category INT,
    price FLOAT
);