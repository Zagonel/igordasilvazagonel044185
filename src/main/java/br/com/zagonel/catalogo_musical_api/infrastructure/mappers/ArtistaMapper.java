package br.com.zagonel.catalogo_musical_api.infrastructure.mappers;

import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.domain.model.Artista;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ArtistaMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artistaId", source = "id")
    @Mapping(target = "albuns", ignore = true)
    ArtistaJpaEntity toEntity(Artista domain);

    @Mapping(target = "id", source = "artistaId")
    @Mapping(target = "albuns", ignore = true)
    Artista toDomain(ArtistaJpaEntity entity);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "artistaId", source = "id")
    void updateEntityFromDomain(Artista domain, @MappingTarget ArtistaJpaEntity entity);

    ArtistaResponseDTO toResponse(Artista domain);

    ArtistaResponseDTO.AlbumResumidoResponseDTO toAlbumResumido(Album domain);
}
