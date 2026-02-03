package br.com.zagonel.catalogo_musical_api.infrastructure.mappers;

import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.domain.model.Artista;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
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

        List<Album> albumList = entity.getAlbuns() != null ?
                new ArrayList<>(entity.getAlbuns().stream().map(albumEntity -> new Album(
                        albumEntity.getAlbumId(),
                        albumEntity.getTitulo(),
                        albumEntity.getDataLancamento(),
                        null, null)).toList()) :
                new ArrayList<>();

        return new Artista(
                entity.getArtistaId(),
                entity.getNome(),
                entity.getTipo(),
                albumList
        );
    }

    public ArtistaResponseDTO toResponse(Artista domain) {
        if (domain == null) return null;

        ArtistaResponseDTO dto = new ArtistaResponseDTO();
        dto.setId(domain.getId());
        dto.setNome(domain.getNome());
        dto.setTipo(domain.getTipo());

        if (domain.getAlbuns() != null) {
            dto.setAlbuns(domain.getAlbuns().stream()
                    .map(album -> {
                        ArtistaResponseDTO.AlbumResumidoResponseDTO albumDto = new ArtistaResponseDTO.AlbumResumidoResponseDTO();
                        albumDto.setId(album.getId());
                        albumDto.setTitulo(album.getTitulo());
                        albumDto.setDataLancamento(album.getDataLancamento());
                        return albumDto;
                    })
                    .toList());
        }

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
