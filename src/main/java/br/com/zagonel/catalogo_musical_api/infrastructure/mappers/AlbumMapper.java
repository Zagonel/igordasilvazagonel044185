package br.com.zagonel.catalogo_musical_api.infrastructure.mappers;

import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {ArtistaMapper.class, MusicaMapper.class, CapaAlbumMapper.class})
public interface AlbumMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "albumId", source = "id")
    AlbumJpaEntity toEntity(Album domain);

    @Mapping(target = "id", source = "albumId")
    Album toDomain(AlbumJpaEntity entity);

    AlbumResponseDTO toResponse(Album domain);
}
