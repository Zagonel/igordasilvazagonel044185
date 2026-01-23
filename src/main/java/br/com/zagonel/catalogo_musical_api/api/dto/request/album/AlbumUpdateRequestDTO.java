package br.com.zagonel.catalogo_musical_api.api.dto.request.album;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AlbumUpdateRequestDTO {
    private String titulo;
    private LocalDate dataLancamento;
}
