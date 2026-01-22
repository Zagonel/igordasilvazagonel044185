package br.com.zagonel.catalogo_musical_api.infrastructure.repository;

import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<AlbumJpaEntity, Long>, JpaSpecificationExecutor<AlbumJpaEntity> {
}
