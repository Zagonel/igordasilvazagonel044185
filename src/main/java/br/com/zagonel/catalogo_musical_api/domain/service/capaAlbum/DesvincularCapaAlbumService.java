package br.com.zagonel.catalogo_musical_api.domain.service.capaAlbum;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DesvincularCapaAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Transactional
    public void execute(UUID albumUuid, String path) {
        AlbumJpaEntity albumEntity = albumRepository.findByAlbumId(albumUuid)
                .orElseThrow(() -> new DomainException("Álbum não encontrado com o ID: " + albumUuid));

        Album albumDomain = albumMapper.toDomain(albumEntity);

        albumDomain.removerCapa(path);

        //Aqui tenho que remover do MinIO a imagem referente a esse path.

        albumMapper.updateEntityFromDomain(albumDomain, albumEntity);

        albumRepository.save(albumEntity);
    }
}
