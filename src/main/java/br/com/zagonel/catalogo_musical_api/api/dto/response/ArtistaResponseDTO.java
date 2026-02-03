package br.com.zagonel.catalogo_musical_api.api.dto.response;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonPropertyOrder({"id", "nome", "tipo", "albuns"})
public class ArtistaResponseDTO {
    private UUID id;
    private String nome;
    private TipoArtista tipo;
    private List<AlbumResumidoResponseDTO> albuns;

    @Getter
    @Setter
    @JsonPropertyOrder({"id", "titulo", "dataLancamento"})
    public static class AlbumResumidoResponseDTO {
        private UUID id;
        private String titulo;
        private LocalDate dataLancamento;
    }
}


