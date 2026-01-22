CREATE TABLE musica(
    album_id         BIGINT NOT NULL,
    titulo           VARCHAR(255),
    duracao_segundos BIGINT,
    ordem            INTEGER,
    CONSTRAINT fk_musica_on_album FOREIGN KEY (album_id) REFERENCES album (id)
);

CREATE TABLE album_capa(
    album_id    BIGINT  NOT NULL,
    path        VARCHAR(255),
    descricao   VARCHAR(255),
    principal   BOOLEAN NOT NULL,
    data_upload TIMESTAMP,
    CONSTRAINT fk_album_capa_on_album FOREIGN KEY (album_id) REFERENCES album (id)
);