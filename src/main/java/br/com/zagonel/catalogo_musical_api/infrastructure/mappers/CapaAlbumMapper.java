package br.com.zagonel.catalogo_musical_api.infrastructure.mappers;

import br.com.zagonel.catalogo_musical_api.api.dto.response.CapaAlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.model.CapaAlbum;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.embeddables.CapaAlbumEmbeddable;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CapaAlbumMapper {

    CapaAlbum toDomain(CapaAlbumEmbeddable entity);

    CapaAlbumEmbeddable toEntity(CapaAlbum domain);

    CapaAlbumResponseDTO toResponse(CapaAlbum domain);
}
