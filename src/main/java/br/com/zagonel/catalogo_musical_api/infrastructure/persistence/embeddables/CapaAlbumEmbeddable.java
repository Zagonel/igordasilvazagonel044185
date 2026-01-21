package br.com.zagonel.catalogo_musical_api.infrastructure.persistence.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CapaAlbumEmbeddable {

    @Column(nullable = false, unique = true)
    private String path;

    @Column(name = "descricao", length = 100)
    private String descricao;

    @Column(name = "principal", nullable = false)
    private boolean principal;

    @Column(name = "upload_at")
    private LocalDateTime uploadAt;

}
