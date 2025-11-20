-- Crear esquema
CREATE SCHEMA IF NOT EXISTS jac;

-- Cambiar a ese esquema
SET search_path TO jac;

CREATE TABLE IF NOT EXISTS user_status (
    id BIGINT PRIMARY KEY,
    code TEXT NOT NULL,
    description TEXT,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    email TEXT NOT NULL,
    phone_number TEXT,
    username TEXT NOT NULL,
    password TEXT NOT NULL,
    security TEXT,
    status_id BIGINT NOT NULL REFERENCES user_status(id),
    previous_status_id BIGINT REFERENCES user_status(id),
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Crear tabla de tokens de sesión
CREATE TABLE IF NOT EXISTS tokens (
    id BIGINT PRIMARY KEY,
    user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    access_token TEXT NOT NULL,
    app_name TEXT NOT NULL,
    audience TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMPTZ
);

CREATE TABLE IF NOT EXISTS password_resets (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  token VARCHAR(255) NOT NULL,
  expires_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS account_activations (
  id BIGINT PRIMARY KEY,
  user_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  token VARCHAR(255) NOT NULL UNIQUE,
  expires_at TIMESTAMP NOT NULL,
  created_at TIMESTAMP DEFAULT NOW()
);

-- Commons
CREATE TABLE IF NOT EXISTS status (
  id          BIGINT PRIMARY KEY,
  category    VARCHAR(30) NOT NULL, -- p.ej. 'notification', 'api_key', 'user', 'system'
  code        VARCHAR(20) NOT NULL,
  name        VARCHAR(50) NOT NULL,
  description TEXT,
  is_active   BOOLEAN NOT NULL DEFAULT true,
  created_at  TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- Api keys
CREATE TABLE IF NOT EXISTS api_keys (
  id BIGINT PRIMARY KEY,
  name          TEXT,
  user_id       BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
  secret_hash   TEXT NOT NULL, --Base64: user_id|id|random hash
  category      VARCHAR(30) NOT NULL, -- p.ej. 'notification', 'api_key', 'user', 'system'
  status_id     BIGINT NOT NULL REFERENCES status(id),
  daily_limit   INTEGER,    -- null = ilimitado
  monthly_limit BIGINT,     -- null = ilimitado
  rate_per_min  INTEGER,    -- requests per minute (null = no limit)
  metadata      JSONB,      -- campos libres (allowed_channels, ip_restrictions, quotas por canal...)
  created_at    TIMESTAMPTZ NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS idx_api_keys_user_id ON api_keys(user_id);
CREATE INDEX IF NOT EXISTS idx_api_keys_status_id ON api_keys(status_id);

CREATE TABLE IF NOT EXISTS api_key_usage_daily (
  id          BIGINT PRIMARY KEY,
  api_key_id  BIGINT NOT NULL REFERENCES api_keys(id) ON DELETE CASCADE,
  day         DATE NOT NULL,
  channel     VARCHAR(30) NOT NULL, -- p.ej. 'email', 'sms', 'push', 'websocket'
  count       BIGINT NOT NULL DEFAULT 0,
  last_updated TIMESTAMPTZ NOT NULL DEFAULT now(),
  UNIQUE(api_key_id, day, channel)
);

CREATE INDEX IF NOT EXISTS idx_api_key_usage_daily_api_key_day ON api_key_usage_daily(api_key_id, day);

-- Notifications
CREATE TABLE IF NOT EXISTS notifications (
  id             BIGINT PRIMARY KEY,
  api_key_id     BIGINT NOT NULL REFERENCES api_keys(id) ON DELETE SET NULL,
  channel        VARCHAR(30) NOT NULL, -- p.ej. 'email', 'sms', 'push', 'websocket'
  payload        JSONB,                    -- contenido, variables usadas, metadata
  status_id      BIGINT NOT NULL REFERENCES status(id),
  attempts       SMALLINT NOT NULL DEFAULT 0,
  priority       SMALLINT NOT NULL DEFAULT 50, -- 0..100 (más bajo = más prioridad) o al revés según preferencia
  provider_id    TEXT,                      -- id devuelto por proveedor (twilio, ses, fcm, apns)
  error          JSONB,                     -- último error / respuesta del proveedor
  created_at     TIMESTAMPTZ NOT NULL DEFAULT now()
);

-- índices
CREATE INDEX IF NOT EXISTS idx_notifications_api_key_id ON notifications(api_key_id);
CREATE INDEX IF NOT EXISTS idx_notifications_status_id ON notifications(status_id);



-- seed.sql

INSERT INTO user_status (id, code, description)
SELECT 1, 'ACTIVE',  'Cuenta activa'
WHERE NOT EXISTS (SELECT 1 FROM user_status WHERE id = 1 OR code = 'ACTIVE');

INSERT INTO user_status (id, code, description)
SELECT 2, 'INACTIVE', 'Cuenta desactivada o bloqueada'
WHERE NOT EXISTS (SELECT 1 FROM user_status WHERE id = 2 OR code = 'INACTIVE');

INSERT INTO user_status (id, code, description)
SELECT 3, 'LOCKED', 'Cuenta bloqueada'
WHERE NOT EXISTS (SELECT 1 FROM user_status WHERE id = 3 OR code = 'LOCKED');

INSERT INTO user_status (id, code, description)
SELECT 4, 'DELETED', 'Cuenta eliminada'
WHERE NOT EXISTS (SELECT 1 FROM user_status WHERE id = 4 OR code = 'DELETED');

INSERT INTO user_status (id, code, description)
SELECT 5, 'PENDING', 'Cuenta pendiente de activación'
WHERE NOT EXISTS (SELECT 1 FROM user_status WHERE id = 5 OR code = 'PENDING');

-- NOTIFICATIONS

INSERT INTO status (id, category, code, name, description, is_active)
SELECT 1, 'notification', 'queued',    'En cola',    'Notificación en cola, pendiente de envío', true
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='notification' AND code='queued')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 1);

INSERT INTO status (id, category, code, name, description, is_active)
SELECT 2, 'notification', 'sending',   'Enviando',   'En proceso de envío', true
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='notification' AND code='sending')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 2);

INSERT INTO status (id, category, code, name, description, is_active)
SELECT 3, 'notification', 'sent',      'Enviado',    'Enviado al proveedor', true
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='notification' AND code='sent')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 3);

INSERT INTO status (id, category, code, name, description, is_active)
SELECT 4, 'notification', 'failed',    'Fallido',    'Entrega fallida', true
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='notification' AND code='failed')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 4);

-- API KEYS
INSERT INTO status (id, category, code, name, description, is_active)
SELECT 5, 'api_key', 'active',   'Activo',     'Clave API habilitada', true
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='api_key' AND code='active')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 5);

INSERT INTO status (id, category, code, name, description, is_active)
SELECT 6, 'api_key', 'disabled', 'Deshabilado','Clave temporalmente deshabilitada', true
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='api_key' AND code='disabled')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 6);

INSERT INTO status (id, category, code, name, description, is_active)
SELECT 7, 'api_key', 'revoked',  'Revocado',   'Clave revocada permanentemente', false
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='api_key' AND code='revoked')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 7);

-- SYSTEM (salud / mantenimiento)
INSERT INTO status (id, category, code, name, description, is_active)
SELECT 8,  'system', 'healthy',     'Saludable',    'Sistema operando normalmente', true
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='system' AND code='healthy')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 8);

INSERT INTO status (id, category, code, name, description, is_active)
SELECT 9,  'system', 'degraded',    'Degradado',    'Rendimiento degradado / parcial', true
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='system' AND code='degraded')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 9);

INSERT INTO status (id, category, code, name, description, is_active)
SELECT 10, 'system', 'maintenance', 'Mantenimiento','Mantenimiento programado', true
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='system' AND code='maintenance')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 10);

INSERT INTO status (id, category, code, name, description, is_active)
SELECT 11, 'system', 'offline',     'Offline',      'Sistema fuera de servicio', false
WHERE NOT EXISTS (SELECT 1 FROM status WHERE category='system' AND code='offline')
  AND NOT EXISTS (SELECT 1 FROM status WHERE id = 11);
