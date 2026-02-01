package br.com.zagonel.catalogo_musical_api.api.controller.artista_album;

import br.com.zagonel.catalogo_musical_api.domain.service.artista_album.DesvincularArtistaAlbumService;
import br.com.zagonel.catalogo_musical_api.domain.service.artista_album.VincularArtistaAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ArtistaAlbumController implements ArtistaAlbumApi {

    private final VincularArtistaAlbumService vincularArtistaAlbumService;
    private final DesvincularArtistaAlbumService desvincularArtistaAlbumService;

    @Override
    public void vincular(UUID albumId, UUID artistaId) {
        vincularArtistaAlbumService.execute(albumId, artistaId);
    }

    @Override
    public void desvincular(UUID albumId, UUID artistaId) {
        desvincularArtistaAlbumService.execute(albumId, artistaId);
    }
}
