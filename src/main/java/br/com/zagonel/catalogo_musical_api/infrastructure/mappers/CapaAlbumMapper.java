package br.com.zagonel.catalogo_musical_api.infrastructure.mappers;

import br.com.zagonel.catalogo_musical_api.api.dto.response.CapaAlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.model.CapaAlbum;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.embeddables.CapaAlbumEmbeddable;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class CapaAlbumMapper {

    public CapaAlbumEmbeddable toEntity(CapaAlbum domain) {

        if (domain == null) {
            return null;
        }

        CapaAlbumEmbeddable entity = new CapaAlbumEmbeddable();
        entity.setPath(domain.getPath());
        entity.setDescricao(domain.getDescricao());
        entity.setUploadAt(domain.getDataUpload());
        return entity;
    }

    public CapaAlbum toDomain(CapaAlbumEmbeddable entity) {
        if (entity == null) {
            return null;
        }
        return new CapaAlbum(
                entity.getPath(),
                entity.getDescricao(),
                entity.getUploadAt()
        );
    }

    public CapaAlbumResponseDTO toResponse(CapaAlbum domain) {
        if (domain == null) {
            return null;
        }

        CapaAlbumResponseDTO dto = new CapaAlbumResponseDTO();
        dto.setPath(domain.getPath());
        dto.setDescricao(domain.getDescricao());
        dto.setDataUpload(domain.getDataUpload());
        return dto;
    }
}