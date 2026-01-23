package br.com.zagonel.catalogo_musical_api.api.dto.request.album;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumCreateRequestDTO {
    private String titulo;
    private LocalDate dataLancamento;
    private List<String> artistasIds;
}
