package br.com.zagonel.catalogo_musical_api.domain.service.album.retrive.get;

import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Transactional(readOnly = true)
    public AlbumResponseDTO execute(UUID albumId) {

        AlbumJpaEntity entity = albumRepository.findByAlbumId(albumId)
                .orElseThrow(() -> new DomainException("Álbum não encontrado com o ID: " + albumId));

        Album domain = albumMapper.toDomain(entity);

        return albumMapper.toResponse(domain);
    }
}
