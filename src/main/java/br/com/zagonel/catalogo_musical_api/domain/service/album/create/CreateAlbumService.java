package br.com.zagonel.catalogo_musical_api.domain.service.album.create;

import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.ArtistaMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.ArtistaRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final ArtistaMapper artistaMapper;
    private final ArtistaRepository artistaRepository;

    @Transactional
    public AlbumResponseDTO execute(AlbumCreateRequestDTO requestDTO) {
        Album albumDomain = Album.criarNovoAlbum(
                requestDTO.getTitulo(),
                requestDTO.getDataLancamento()
        );

        if (requestDTO.getArtistasIds() != null) {
            requestDTO.getArtistasIds().forEach(artistaUuid -> {
                ArtistaJpaEntity artistaJpaEntity = artistaRepository.findByArtistaId(UUID.fromString(artistaUuid))
                        .orElseThrow(() -> new DomainException("Não foi possivel encontrar o artista com ID: " + artistaUuid));
                albumDomain.vincularArtista(artistaMapper.toDomain(artistaJpaEntity));
            });
        }

        AlbumJpaEntity albumEntity = albumMapper.toEntity(albumDomain);

        AlbumJpaEntity savedEntity = albumRepository.save(albumEntity);

        return albumMapper.toResponse(albumMapper.toDomain(savedEntity));
    }
}
