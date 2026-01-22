CREATE TABLE artista
(
    id         BIGINT       NOT NULL,
    artista_id UUID         NOT NULL,
    nome       VARCHAR(200) NOT NULL,
    tipo       VARCHAR(20)  NOT NULL,
    CONSTRAINT pk_artista PRIMARY KEY (id),
    CONSTRAINT uc_artista_artista_id UNIQUE (artista_id)
);

CREATE TABLE album(
    id              BIGINT       NOT NULL,
    album_id        UUID         NOT NULL,
    titulo          VARCHAR(200) NOT NULL,
    data_lancamento DATE,
    CONSTRAINT pk_album PRIMARY KEY (id),
    CONSTRAINT uc_album_album_id UNIQUE (album_id)
);