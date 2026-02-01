package br.com.zagonel.catalogo_musical_api.api.controller.capaAlbum;

import br.com.zagonel.catalogo_musical_api.domain.service.capaAlbum.DesvincularCapaAlbumService;
import br.com.zagonel.catalogo_musical_api.domain.service.capaAlbum.VincularCapaAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class CapaAlbumController implements CapaAlbumApi {

    private final VincularCapaAlbumService vincularCapaAlbumService;
    private final DesvincularCapaAlbumService desvincularCapaAlbumService;

    @Override
    public void vincularCapa(UUID albumId, MultipartFile file, String descricao, boolean principal) {
        vincularCapaAlbumService.execute(albumId, file, descricao, principal);
    }

    @Override
    public void desvincularCapa(UUID albumId, String path) {
        desvincularCapaAlbumService.execute(albumId, path);
    }
}
