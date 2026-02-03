package br.com.zagonel.catalogo_musical_api.infrastructure.mappers;

import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.domain.model.Artista;
import br.com.zagonel.catalogo_musical_api.domain.model.CapaAlbum;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

        List<CapaAlbum> capaAlbumList = entity.getCapas() != null ?
                new ArrayList<>(entity.getCapas().stream().map(capaAlbumMapper::toDomain).toList()) :
                new ArrayList<>();

        List<Artista> artistaList = entity.getArtistas() != null ?
                new ArrayList<>(entity.getArtistas().stream().map(artistaMapper::toDomain).toList()) :
                new ArrayList<>();

        return new Album(
                entity.getAlbumId(),
                entity.getTitulo(),
                entity.getDataLancamento(),
                capaAlbumList,
                artistaList
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

        if (domain.getCapas() != null) {
            dto.setCapas(domain.getCapas().stream()
                    .map(capaAlbumMapper::toResponse)
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

        if (domain.getArtistas() != null) {
            dto.setArtistas(domain.getArtistas().stream()
                    .map(artista -> new AlbumResponseDTO.ArtistaResumidoResponseDTO(
                            artista.getId(),
                            artista.getNome(),
                            artista.getTipo()
                    ))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }

        return dto;
    }

    public void updateEntityFromDomain(Album domain, AlbumJpaEntity entity) {
        if (domain == null || entity == null) {
            return;
        }

        entity.setTitulo(domain.getTitulo());
        entity.setDataLancamento(domain.getDataLancamento());

        if (domain.getCapas() != null) {
            entity.getCapas().clear();
            entity.getCapas().addAll(domain.getCapas().stream()
                    .map(capaAlbumMapper::toEntity)
                    .toList());
        }

    }
}
