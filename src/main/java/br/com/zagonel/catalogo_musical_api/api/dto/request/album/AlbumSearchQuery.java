package br.com.zagonel.catalogo_musical_api.api.dto.request.album;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;

import java.time.LocalDate;

public record AlbumSearchQuery(
        int page,
        int perPage,
        String sort,
        String direction,
        String titulo,
        String nomeArtista,
        TipoArtista tipoArtista,
        LocalDate dataInicio,
        LocalDate dataFim
) {
}
