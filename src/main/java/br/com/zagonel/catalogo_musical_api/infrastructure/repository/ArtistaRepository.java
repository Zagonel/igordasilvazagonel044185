package br.com.zagonel.catalogo_musical_api.infrastructure.repository;

import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ArtistaRepository extends JpaRepository<ArtistaJpaEntity, Long>, JpaSpecificationExecutor<ArtistaJpaEntity> {

    @Query("select a from ArtistaJpaEntity a where a.artistaId =:artistaId")
    Optional<ArtistaJpaEntity> findByArtistaId(@Param("artistaId") UUID artistaId);


}
