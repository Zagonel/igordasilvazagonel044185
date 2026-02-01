package br.com.zagonel.catalogo_musical_api.domain.service.capaAlbum;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.domain.model.CapaAlbum;
import br.com.zagonel.catalogo_musical_api.domain.service.minio.MinioStorageService;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VincularCapaAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final MinioStorageService minioStorageService;

    @Transactional
    public void execute(UUID albumUuid, MultipartFile file, String descricao, boolean principal) {

        String pathGerado = minioStorageService.upload(file, albumUuid);

        AlbumJpaEntity albumEntity = albumRepository.findByAlbumId(albumUuid)
                .orElseThrow(() -> new DomainException("Álbum não encontrado"));

        Album albumDomain = albumMapper.toDomain(albumEntity);

        CapaAlbum novaCapa = CapaAlbum.criarCapaAlbum(pathGerado, descricao, principal);
        albumDomain.adicionarCapa(novaCapa);

        albumMapper.updateEntityFromDomain(albumDomain, albumEntity);

        albumRepository.save(albumEntity);
    }
}
