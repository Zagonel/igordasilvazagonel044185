package br.com.zagonel.catalogo_musical_api.infrastructure.persistence.specifications;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public class ArtistaSpecifications {

    public static Specification<ArtistaJpaEntity> comNome(String nome) {
        return (root, query, cb) -> (nome == null || nome.isBlank()) ? null :
                cb.like(cb.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
    }

    public static Specification<ArtistaJpaEntity> comTipo(TipoArtista tipo) {
        return (root, query, cb) -> tipo == null ? null :
                cb.equal(root.get("tipo"), tipo);
    }

    public static Specification<ArtistaJpaEntity> comNomeAlbum(String nomeAlbum) {
        return (root, query, cb) -> {
            if (nomeAlbum == null || nomeAlbum.isBlank()) return null;

            query.distinct(true);
            Join<ArtistaJpaEntity, AlbumJpaEntity> albuns = root.join("albuns");
            return cb.like(cb.lower(albuns.get("titulo")), "%" + nomeAlbum.toLowerCase() + "%");
        };
    }
}
