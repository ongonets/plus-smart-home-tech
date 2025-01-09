-- создаём таблицу products
CREATE TABLE IF NOT EXISTS products (
    id VARCHAR PRIMARY KEY,
    weight FLOAT,
    width FLOAT,
    height FLOAT,
    depth FLOAT,
    fragile BOOLEAN,
    quantity INT
);