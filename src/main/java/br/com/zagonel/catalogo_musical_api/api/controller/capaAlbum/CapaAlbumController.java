package br.com.zagonel.catalogo_musical_api.api.controller.capaAlbum;

import br.com.zagonel.catalogo_musical_api.api.dto.request.capaAlbum.CapaAlbumRequest;
import br.com.zagonel.catalogo_musical_api.service.capaAlbum.DesvincularCapaAlbumService;
import br.com.zagonel.catalogo_musical_api.service.capaAlbum.VincularCapaAlbumService;
import br.com.zagonel.catalogo_musical_api.service.minio.RetriveLinkPreAssinadoMinioStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CapaAlbumController implements CapaAlbumApi {

    private final VincularCapaAlbumService vincularCapaAlbumService;
    private final DesvincularCapaAlbumService desvincularCapaAlbumService;
    private final RetriveLinkPreAssinadoMinioStorageService storageService;

    @Override
    public void vincularCapa(UUID albumId, List<MultipartFile> files, CapaAlbumRequest metadata) {
        vincularCapaAlbumService.execute(albumId, files, metadata);
    }

    @Override
    public void desvincularCapa(UUID albumId, String path) {
        desvincularCapaAlbumService.execute(albumId, path);
    }

    @Override
    public String gerarLinkAssinado(UUID albumId, String path) {
        return storageService.execute(path);
    }

}
