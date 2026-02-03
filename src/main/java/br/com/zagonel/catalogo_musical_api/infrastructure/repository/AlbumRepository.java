package br.com.zagonel.catalogo_musical_api.infrastructure.repository;

import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumJpaEntity, Long>, JpaSpecificationExecutor<AlbumJpaEntity> {

    @Query("select a from AlbumJpaEntity a where a.albumId =:albumId")
    Optional<AlbumJpaEntity> findByAlbumId(@Param("albumId") UUID albumId);

//    @Override
//    @EntityGraph(attributePaths = {"artistas", "capas"})
//    @NonNull
//    Page<AlbumJpaEntity> findAll(@NonNull Specification<AlbumJpaEntity> spec, @NonNull Pageable pageable);
}
