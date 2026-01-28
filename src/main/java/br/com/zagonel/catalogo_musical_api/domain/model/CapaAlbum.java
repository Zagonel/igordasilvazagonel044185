package br.com.zagonel.catalogo_musical_api.domain.model;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.time.LocalDateTime;


@Getter
@EqualsAndHashCode
public class CapaAlbum {

    private final String path;
    private final String descricao;
    private boolean principal;
    private final LocalDateTime dataUpload;

    public CapaAlbum(String path, String descricao, boolean principal, LocalDateTime dataUpload) {
        this.path = path;
        this.descricao = descricao;
        this.principal = principal;
        this.dataUpload = dataUpload;
    }

    public static CapaAlbum criarCapaAlbum(String path, String descricao, boolean principal) {
        if (path == null || path.isBlank()) {
            throw new DomainException("O caminho (path) da imagem é obrigatório.");
        }
        if (descricao == null || descricao.isBlank()) {
            throw new DomainException("A descrição da imagem é obrigatório.");
        }
        return new CapaAlbum(path, descricao, principal, LocalDateTime.now());
    }

    public void alterarStatusPrincipal(boolean status) {
        this.principal = status;
    }
}
