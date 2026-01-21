package br.com.zagonel.catalogo_musical_api.domain.enums;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import lombok.Getter;

@Getter
public enum TipoArtista {
    CANTOR(1,"Cantor(a)"),
    BANDA(2,"Banda"),
    DUPLA(3,"Dupla");

    private final int codigo;
    private final String descricao;

    TipoArtista(int codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public static TipoArtista fromCodigo(int codigo) {
        for (TipoArtista tipo : values()) {
            if (tipo.codigo == codigo) {
                return tipo;
            }
        }
        throw new DomainException("O código " + codigo + " não corresponde a um tipo de artista válido (1-Cantor, 2-Banda, 3-Dupla).");
    }

}
