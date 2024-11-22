CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
    );

INSERT INTO users (username, password) VALUES ('testuser', 'test123') ON CONFLICT DO NOTHING;
INSERT INTO users (username, password) VALUES ('admin', '1234') ON CONFLICT DO NOTHING;
