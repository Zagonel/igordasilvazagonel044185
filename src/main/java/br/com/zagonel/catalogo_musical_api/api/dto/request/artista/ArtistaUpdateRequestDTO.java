package br.com.zagonel.catalogo_musical_api.api.dto.request.artista;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ArtistaUpdateRequestDTO {
    private String nome;
    private TipoArtista tipo;
}
