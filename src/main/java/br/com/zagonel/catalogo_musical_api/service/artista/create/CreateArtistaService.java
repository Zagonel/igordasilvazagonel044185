package br.com.zagonel.catalogo_musical_api.service.artista.create;

import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Artista;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.ArtistaMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateArtistaService {

    private final ArtistaRepository artistaRepository;
    private final AlbumRepository albumRepository;
    private final ArtistaMapper artistaMapper;
    private final AlbumMapper albumMapper;

    @Transactional
    public ArtistaResponseDTO execute(ArtistaCreateRequestDTO requestDTO) {
        Artista artistaDomain = Artista.criarNovoArtista(
                requestDTO.getNome(),
                requestDTO.getTipo()
        );

        List<AlbumJpaEntity> albunsGerenciados = new ArrayList<>();
        ArtistaJpaEntity artistaEntity = artistaMapper.toEntity(artistaDomain);

        if (requestDTO.getAlbunsIds() != null) {
            requestDTO.getAlbunsIds().forEach(albumUuid -> {

                AlbumJpaEntity albumJpaEntity = albumRepository.findByAlbumId(UUID.fromString(albumUuid))
                        .orElseThrow(() -> new DomainException("Não foi possível encontrar o álbum com ID: " + albumUuid));

                artistaDomain.vincularAlbum(albumMapper.toDomain(albumJpaEntity));
                albumJpaEntity.getArtistas().add(artistaEntity);

                albunsGerenciados.add(albumJpaEntity);
            });
        }

        artistaEntity.setAlbuns(albunsGerenciados);

        ArtistaJpaEntity savedEntity = artistaRepository.save(artistaEntity);

        return artistaMapper.toResponse(artistaMapper.toDomain(savedEntity));
    }
}
