package br.com.zagonel.catalogo_musical_api.service.capaAlbum;

import br.com.zagonel.catalogo_musical_api.api.dto.request.capaAlbum.CapaAlbumRequest;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.domain.model.CapaAlbum;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import br.com.zagonel.catalogo_musical_api.service.minio.MinioStorageService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VincularCapaAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;
    private final MinioStorageService minioStorageService;

    @Transactional
    public void execute(UUID albumUuid, List<MultipartFile> files, CapaAlbumRequest metadata) {
        AlbumJpaEntity albumEntity = albumRepository.findByAlbumId(albumUuid)
                .orElseThrow(() -> new DomainException("Álbum não encontrado"));

        Album albumDomain = albumMapper.toDomain(albumEntity);

        for (MultipartFile file : files) {
            String pathGerado = minioStorageService.upload(file, albumUuid);

            CapaAlbum novaCapa = CapaAlbum.criarCapaAlbum(pathGerado, metadata.descricao());
            albumDomain.adicionarCapa(novaCapa);
        }

        albumMapper.updateEntityFromDomain(albumDomain, albumEntity);
        albumRepository.save(albumEntity);
    }
}
