package br.com.zagonel.catalogo_musical_api.infrastructure.repository;

import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumJpaEntity, Long>, JpaSpecificationExecutor<AlbumJpaEntity> {

    @Query("select a from AlbumJpaEntity a where a.albumId =: albumId")
    Optional<AlbumJpaEntity> findByAlbumId(@Param("albumId") UUID albumId);

}
