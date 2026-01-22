package br.com.zagonel.catalogo_musical_api.infrastructure.persistence.specifications;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import org.springframework.data.jpa.domain.Specification;

public class ArtistaSpecifications {
    public static Specification<ArtistaJpaEntity> porNome(String nome) {
        return (root, query, cb) -> {
            if (nome == null || nome.isBlank()) return null;
            return cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        };
    }

    public static Specification<ArtistaJpaEntity> porTipo(TipoArtista tipo) {
        return (root, query, cb) -> {
            if (tipo == null) return null;
            return cb.equal(root.get("tipo"), tipo);
        };
    }
}
