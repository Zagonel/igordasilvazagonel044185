package br.com.zagonel.catalogo_musical_api.api.dto.request.artista;

public record ArtistaSearchQuery(
        int page,
        int perPage,
        String sort,
        String direction,
        String nome,
        Integer tipo,
        String nomeAlbum
) {
}
