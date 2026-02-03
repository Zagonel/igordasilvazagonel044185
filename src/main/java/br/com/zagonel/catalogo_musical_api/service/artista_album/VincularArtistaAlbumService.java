package br.com.zagonel.catalogo_musical_api.service.artista_album;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
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

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VincularArtistaAlbumService {

    private final AlbumRepository albumRepository;
    private final ArtistaRepository artistaRepository;
    private final AlbumMapper albumMapper;
    private final ArtistaMapper artistaMapper;

    @Transactional
    public void execute(UUID albumUuid, UUID artistaUuid) {
        AlbumJpaEntity albumEntity = albumRepository.findByAlbumId(albumUuid)
                .orElseThrow(() -> new DomainException("Álbum não encontrado"));

        ArtistaJpaEntity artistaEntity = artistaRepository.findByArtistaId(artistaUuid)
                .orElseThrow(() -> new DomainException("Artista não encontrado"));

        Album albumDomain = albumMapper.toDomain(albumEntity);
        Artista artista = artistaMapper.toDomain(artistaEntity);

        albumDomain.vincularArtista(artista);
        artista.vincularAlbum(albumDomain);

        albumMapper.updateEntityFromDomain(albumDomain, albumEntity);

        albumRepository.save(albumEntity);
    }
}
