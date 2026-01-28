package br.com.zagonel.catalogo_musical_api.infrastructure.mappers;

import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.model.Artista;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

@Component
@NoArgsConstructor
public class ArtistaMapper {

    public ArtistaJpaEntity toEntity(Artista domain) {

        if (domain == null) {
            return null;
        }

        ArtistaJpaEntity entity = new ArtistaJpaEntity();
        entity.setArtistaId(domain.getId());
        entity.setNome(domain.getNome());
        entity.setTipo(domain.getTipo());
        return entity;
    }

    public Artista toDomain(ArtistaJpaEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Artista(
                entity.getArtistaId(),
                entity.getNome(),
                entity.getTipo(),
                new ArrayList<>()
        );
    }

    public ArtistaResponseDTO toResponse(Artista domain) {
        if (domain == null) {
            return null;
        }
        ArtistaResponseDTO dto = new ArtistaResponseDTO();
        dto.setId(domain.getId());
        dto.setNome(domain.getNome());
        dto.setTipo(domain.getTipo());
        return dto;
    }

    public void updateEntityFromDomain(Artista domain, ArtistaJpaEntity entity) {
        if (domain == null || entity == null) {
            return;
        }
        entity.setNome(domain.getNome());
        entity.setTipo(domain.getTipo());
    }
}
