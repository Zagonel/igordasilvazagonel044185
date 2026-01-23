package br.com.zagonel.catalogo_musical_api.domain.service.album.delete;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteAlbumService {

    private final AlbumRepository albumRepository;

    @Transactional
    public void execute(UUID albumUuid) {

        AlbumJpaEntity album = albumRepository.findByAlbumId(albumUuid)
                .orElseThrow(() -> new DomainException("Álbum não encontrado"));
        //Preciso deletar as capas de album atreladas ao album do MinIO
        albumRepository.delete(album);
    }
}
