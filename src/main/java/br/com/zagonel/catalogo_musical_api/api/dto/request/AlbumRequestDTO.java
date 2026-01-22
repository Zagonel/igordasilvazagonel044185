package br.com.zagonel.catalogo_musical_api.api.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class AlbumRequestDTO {
    private String titulo;
    private LocalDate dataLancamento;
    private List<String> artistasIds;
}
