package br.com.zagonel.catalogo_musical_api.infrastructure.mappers;

import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AlbumMapper {

    private final ArtistaMapper artistaMapper;
    private final CapaAlbumMapper capaAlbumMapper;

    public AlbumJpaEntity toEntity(Album domain) {

        if (domain == null) {
            return null;
        }

        AlbumJpaEntity entity = new AlbumJpaEntity();
        entity.setAlbumId(domain.getId());
        entity.setTitulo(domain.getTitulo());
        entity.setDataLancamento(domain.getDataLancamento());

        if (domain.getCapas() != null) {
            entity.setCapas(domain.getCapas().stream()
                    .map(capaAlbumMapper::toEntity)
                    .toList());
        }

        return entity;
    }

    public Album toDomain(AlbumJpaEntity entity) {

        if (entity == null) {
            return null;
        }

        return new Album(
                entity.getAlbumId(),
                entity.getTitulo(),
                entity.getDataLancamento(),
                entity.getCapas().stream().map(capaAlbumMapper::toDomain).toList(),
                entity.getArtistas().stream().map(artistaMapper::toDomain).toList()
        );
    }

    public AlbumResponseDTO toResponse(Album domain) {
        if (domain == null) {
            return null;
        }

        AlbumResponseDTO dto = new AlbumResponseDTO();
        dto.setId(domain.getId());
        dto.setTitulo(domain.getTitulo());
        dto.setDataLancamento(domain.getDataLancamento());
        return dto;
    }

    public void updateEntityFromDomain(Album domain, AlbumJpaEntity entity) {
        if (domain == null || entity == null) {
            return;
        }

        entity.setTitulo(domain.getTitulo());
        entity.setDataLancamento(domain.getDataLancamento());
    }
}
