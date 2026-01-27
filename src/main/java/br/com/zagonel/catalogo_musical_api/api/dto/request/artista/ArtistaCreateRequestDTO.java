package br.com.zagonel.catalogo_musical_api.api.dto.request.artista;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ArtistaCreateRequestDTO {
    private String nome;
    private TipoArtista tipo;
    private List<String> albunsIds;
}
