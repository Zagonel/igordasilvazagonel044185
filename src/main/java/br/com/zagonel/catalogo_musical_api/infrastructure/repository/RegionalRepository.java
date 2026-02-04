package br.com.zagonel.catalogo_musical_api.infrastructure.repository;

import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.RegionalJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegionalRepository extends JpaRepository<RegionalJpaEntity, Long> {

    List<RegionalJpaEntity> findByAtivoTrue();

    Optional<RegionalJpaEntity> findByCodigoAndAtivoTrue(Integer codigo);
}
