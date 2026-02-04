CREATE TABLE regional
(
    id     BIGSERIAL PRIMARY KEY,
    codigo INTEGER      NOT NULL,
    nome   VARCHAR(200) NOT NULL,
    ativo  BOOLEAN      NOT NULL DEFAULT TRUE
);

CREATE INDEX idx_regional_codigo ON regional (codigo);
CREATE INDEX idx_regional_ativo ON regional (ativo);