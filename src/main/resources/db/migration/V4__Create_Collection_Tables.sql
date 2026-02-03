CREATE TABLE album_capa(
    album_id    BIGINT  NOT NULL,
    path        VARCHAR(255),
    descricao   VARCHAR(255),
    data_upload TIMESTAMP,
    CONSTRAINT fk_album_capa_on_album FOREIGN KEY (album_id) REFERENCES album (id)
);