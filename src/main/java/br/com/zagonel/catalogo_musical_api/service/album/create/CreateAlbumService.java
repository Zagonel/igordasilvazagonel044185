package br.com.zagonel.catalogo_musical_api.service.album.create;

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
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CreateAlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final AlbumMapper albumMapper;
    private final ArtistaMapper artistaMapper;

    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public AlbumResponseDTO execute(AlbumCreateRequestDTO requestDTO) {
        Album albumDomain = Album.criarNovoAlbum(
                requestDTO.getTitulo(),
                requestDTO.getDataLancamento()
        );

        List<ArtistaJpaEntity> artistasGerenciados = new ArrayList<>();

        if (requestDTO.getArtistasIds() != null) {
            for (String artistaUuidStr : requestDTO.getArtistasIds()) {

                UUID artistaUuid = UUID.fromString(artistaUuidStr);
                ArtistaJpaEntity artistaJpa = artistaRepository.findByArtistaId(artistaUuid)
                        .orElseThrow(() -> new DomainException("Não foi possivel encontrar o artista com ID: " + artistaUuid));

                albumDomain.vincularArtista(artistaMapper.toDomain(artistaJpa));
                artistasGerenciados.add(artistaJpa);
            }
        }

        AlbumJpaEntity albumEntity = albumMapper.toEntity(albumDomain);
        albumEntity.setArtistas(artistasGerenciados);

        AlbumJpaEntity savedEntity = albumRepository.save(albumEntity);

        AlbumResponseDTO response = albumMapper.toResponse(albumMapper.toDomain(savedEntity));

        messagingTemplate.convertAndSend("/topic/albuns", response);

        return response;
    }
}
