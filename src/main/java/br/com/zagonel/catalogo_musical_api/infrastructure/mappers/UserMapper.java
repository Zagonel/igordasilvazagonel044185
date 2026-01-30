package br.com.zagonel.catalogo_musical_api.infrastructure.mappers;

import br.com.zagonel.catalogo_musical_api.api.dto.response.UsuarioResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.model.Usuario;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.UserJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserJpaEntity toEntity(Usuario domain) {
        if (domain == null) return null;

        UserJpaEntity entity = new UserJpaEntity();
        entity.setUserId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setEmail(domain.getEmail());
        entity.setSenha(domain.getSenha());
        entity.setRole(domain.getRole());

        return entity;
    }

    public Usuario toDomain(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return Usuario.restaurar(
                entity.getUserId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getSenha(),
                entity.getRole()
        );
    }

    public UsuarioResponseDTO toResponse(UserJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        return new UsuarioResponseDTO(entity.getEmail());
    }
}
