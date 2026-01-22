package br.com.zagonel.catalogo_musical_api.infrastructure.persistence.specifications;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import org.springframework.data.jpa.domain.Specification;

public class AlbumSpecifications {

    public static Specification<AlbumJpaEntity> porTitulo(String titulo) {
        return (root, query, cb) -> {
            if (titulo == null || titulo.isBlank()) return null;
            return cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%");
        };
    }

    public static Specification<AlbumJpaEntity> temArtistaDoTipo(TipoArtista tipo) {
        return (root, query, cb) -> {
            if (tipo == null) return null;
            return cb.equal(root.join("artistas").get("tipo"), tipo);
        };
    }
}
