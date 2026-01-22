CREATE TABLE artista_album(
    album_id   BIGINT NOT NULL,
    artista_id BIGINT NOT NULL,
    CONSTRAINT fk_on_album FOREIGN KEY (album_id) REFERENCES album (id),
    CONSTRAINT fk_on_artista FOREIGN KEY (artista_id) REFERENCES artista (id)
);