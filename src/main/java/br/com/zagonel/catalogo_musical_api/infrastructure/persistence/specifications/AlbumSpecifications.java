package br.com.zagonel.catalogo_musical_api.infrastructure.persistence.specifications;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class AlbumSpecifications {

    public static Specification<AlbumJpaEntity> comTitulo(String titulo) {
        return (root, query, cb) -> {
            if(titulo == null || titulo.isBlank()){
                return null;
            }

            return cb.like(cb.lower(root.get("titulo")), "%" + titulo.toLowerCase() + "%");

        };
    }

    public static Specification<AlbumJpaEntity> comDataIntervalo(LocalDate inicio, LocalDate fim) {
        return (root, query, cb) -> {

            if (inicio != null && fim != null) {
                return cb.between(root.get("dataLancamento"), inicio, fim);
            } else if (inicio != null) {
                return cb.greaterThanOrEqualTo(root.get("dataLancamento"), inicio);
            } else if (fim != null) {
                return cb.lessThanOrEqualTo(root.get("dataLancamento"), fim);
            }
            return null;
        };
    }

    public static Specification<AlbumJpaEntity> comNomeArtista(String nomeArtista) {
        return (root, query, cb) -> {
            if (nomeArtista == null || nomeArtista.isBlank()){
                return null;
            }
            query.distinct(true);
            Join<AlbumJpaEntity, ArtistaJpaEntity> artistas = root.join("artistas");
            return cb.like(cb.lower(artistas.get("nome")), "%" + nomeArtista.toLowerCase() + "%");
        };
    }

    public static Specification<AlbumJpaEntity> comTipoArtista(TipoArtista tipo) {
        return (root, query, cb) -> {
            if (tipo == null){
                return null;
            }
            query.distinct(true);
            Join<AlbumJpaEntity, ArtistaJpaEntity> artistas = root.join("artistas");
            return cb.equal(artistas.get("tipo"), tipo);
        };
    }
}
