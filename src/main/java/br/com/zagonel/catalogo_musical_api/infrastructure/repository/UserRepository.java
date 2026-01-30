package br.com.zagonel.catalogo_musical_api.infrastructure.repository;

import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.UserJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserJpaEntity, Long> {

    @Query("select u from UserJpaEntity u where u.email =:email")
    Optional<UserJpaEntity> findByEmail(@Param("email") String email);

    @Query("select (count(u) > 0) from UserJpaEntity u where u.email =:email")
    boolean existsByEmail(@Param("email") String email);

}
