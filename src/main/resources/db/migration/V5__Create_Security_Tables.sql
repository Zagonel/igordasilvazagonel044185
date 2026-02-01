CREATE TABLE usuarios
(
    id      BIGSERIAL PRIMARY KEY,
    user_id UUID         NOT NULL UNIQUE,
    nome    VARCHAR(255) NOT NULL,
    email   VARCHAR(255) NOT NULL UNIQUE,
    senha   VARCHAR(255) NOT NULL,
    role    VARCHAR(50)  NOT NULL
);

CREATE TABLE refresh_tokens
(
    id          BIGSERIAL PRIMARY KEY,
    token       VARCHAR(255) NOT NULL UNIQUE,
    expiry_date TIMESTAMP    NOT NULL,
    user_id     BIGINT       NOT NULL,

    CONSTRAINT fk_refresh_token_usuario
        FOREIGN KEY (user_id)
            REFERENCES usuarios (id)
            ON DELETE CASCADE
);

CREATE INDEX idx_refresh_token_user ON refresh_tokens (user_id);