package br.com.zagonel.catalogo_musical_api.api.dto.response;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class ArtistaResponseDTO {
    private UUID id;
    private String nome;
    private TipoArtista tipo;
    private List<AlbumResumidoResponseDTO> albuns;

    @Getter
    @Setter
    public static class AlbumResumidoResponseDTO {
        private UUID id;
        private String titulo;
        private LocalDate dataLancamento;
    }
}


