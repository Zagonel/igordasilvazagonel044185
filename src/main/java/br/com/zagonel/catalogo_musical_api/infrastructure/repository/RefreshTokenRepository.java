package br.com.zagonel.catalogo_musical_api.infrastructure.repository;

import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.RefreshTokenJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenJpaEntity, Long> {

    @Query("select r from RefreshTokenJpaEntity r where r.token =:token")
    Optional<RefreshTokenJpaEntity> findByToken(@Param("token") String token);

    @Transactional
    @Modifying
    @Query("delete from RefreshTokenJpaEntity r where r.user.id =:user_id")
    void deleteByUserId(@Param("user_id") Long userId);

}
