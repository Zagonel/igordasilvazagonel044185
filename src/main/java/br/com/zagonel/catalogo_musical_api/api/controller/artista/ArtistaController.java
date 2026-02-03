package br.com.zagonel.catalogo_musical_api.api.controller.artista;

import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaSearchQuery;
import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaUpdateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.service.artista.create.CreateArtistaService;
import br.com.zagonel.catalogo_musical_api.service.artista.delete.DeleteArtistaService;
import br.com.zagonel.catalogo_musical_api.service.artista.retrive.get.GetArtistaService;
import br.com.zagonel.catalogo_musical_api.service.artista.retrive.list.ListArtistaService;
import br.com.zagonel.catalogo_musical_api.service.artista.update.UpdateArtistaService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ArtistaController implements ArtistaApi {

    private final CreateArtistaService createArtistaService;
    private final GetArtistaService getArtistaService;
    private final ListArtistaService listArtistaService;
    private final UpdateArtistaService updateArtistaService;
    private final DeleteArtistaService deleteArtistaService;

    @Override
    public ResponseEntity<ArtistaResponseDTO> create(final ArtistaCreateRequestDTO input) {
        final var response = createArtistaService.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public Page<ArtistaResponseDTO> list(
            final String nomeArtista,
            final Integer tipoArtista,
            final String nomeAlbum,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        final var query = new ArtistaSearchQuery(
                page,
                perPage,
                sort,
                direction,
                nomeArtista,
                tipoArtista,
                nomeAlbum
        );
        return listArtistaService.execute(query);
    }

    @Override
    public ArtistaResponseDTO getById(final UUID id) {
        return getArtistaService.execute(id);
    }

    @Override
    public ResponseEntity<ArtistaResponseDTO> updateById(final UUID id, final ArtistaUpdateRequestDTO input) {
        final var response = updateArtistaService.execute(id, input);
        return ResponseEntity.ok(response);
    }

    @Override
    public void deleteById(final UUID id) {
        deleteArtistaService.execute(id);
    }
}
