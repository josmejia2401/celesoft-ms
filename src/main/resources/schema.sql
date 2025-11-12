-- Crear esquema
CREATE SCHEMA IF NOT EXISTS celesoft;

-- Cambiar a ese esquema
SET search_path TO celesoft;

CREATE TABLE user_status (
    id BIGSERIAL PRIMARY KEY,
    code TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de usuarios
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone_number TEXT,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    security TEXT,
    status_id BIGSERIAL NOT NULL REFERENCES user_status(id),
    previous_status_id BIGSERIAL REFERENCES user_status(id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de tokens de sesión
CREATE TABLE tokens (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGSERIAL NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    access_token TEXT NOT NULL,
    app_name TEXT NOT NULL,
    audience TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPTZ
);

CREATE TABLE password_resets (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGSERIAL NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  token VARCHAR(255) NOT NULL,
  expires_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE account_activations (
  id BIGSERIAL PRIMARY KEY,
  user_id BIGSERIAL NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  token VARCHAR(255) NOT NULL UNIQUE,
  expires_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT NOW()
);

-- seed.sql

INSERT INTO user_status (id, code, description) VALUES
(1, 'ACTIVE', 'Cuenta activa'),
(2, 'INACTIVE', 'Cuenta desactivada o bloqueada'),
(3, 'LOCKED', 'Cuenta bloqueada'),
(4, 'DELETED', 'Cuenta eliminada'),
(5, 'PENDING', 'Cuenta pendiente de activación');