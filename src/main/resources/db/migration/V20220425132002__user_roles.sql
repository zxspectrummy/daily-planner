CREATE TABLE IF NOT EXISTS roles (
        id SERIAL PRIMARY KEY,
        name TEXT);

CREATE TABLE IF NOT EXISTS user_roles (
        user_id BIGINT REFERENCES users(id),
        role_id INTEGER REFERENCES roles(id),
        PRIMARY KEY (user_id, role_id));


INSERT INTO roles (name) VALUES ('ROLE_USER'),('ROLE_ADMIN');
INSERT INTO user_roles(user_id, role_id)
SELECT id,1 from users