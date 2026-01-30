package br.com.zagonel.catalogo_musical_api.api.dto.request.album;

import java.time.LocalDate;

public record AlbumSearchQuery(
        int page,
        int perPage,
        String sort,
        String direction,
        String titulo,
        String nomeArtista,
        Integer tipoArtista,
        LocalDate dataInicio,
        LocalDate dataFim
) {
}
