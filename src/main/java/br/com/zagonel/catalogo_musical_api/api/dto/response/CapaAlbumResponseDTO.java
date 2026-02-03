package br.com.zagonel.catalogo_musical_api.api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class CapaAlbumResponseDTO {
    private String path;
    private String descricao;
    private LocalDateTime dataUpload;
}
