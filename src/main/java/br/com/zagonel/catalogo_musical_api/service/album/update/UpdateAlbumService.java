package br.com.zagonel.catalogo_musical_api.service.album.update;

import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumUpdateRequestDTO;
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
public class UpdateAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Transactional
    public AlbumResponseDTO execute(UUID albumUuid, AlbumUpdateRequestDTO dto) {

        AlbumJpaEntity albumJpaEntity = albumRepository.findByAlbumId(albumUuid)
                .orElseThrow(() -> new DomainException("Álbum não encontrado"));

        Album albumDomain = albumMapper.toDomain(albumJpaEntity);

        if (dto.getTitulo() != null) {
            albumDomain = albumDomain.alterarTitulo(dto.getTitulo());
        }

        if (dto.getDataLancamento() != null) {
            albumDomain = albumDomain.alterarDataLancamento(dto.getDataLancamento());
        }

        albumMapper.updateEntityFromDomain(albumDomain, albumJpaEntity);

        albumRepository.save(albumJpaEntity);

        return albumMapper.toResponse(albumDomain);
    }
}
