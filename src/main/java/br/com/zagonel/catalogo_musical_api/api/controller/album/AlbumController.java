package br.com.zagonel.catalogo_musical_api.api.controller.album;

import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumSearchQuery;
import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumUpdateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.service.album.create.CreateAlbumService;
import br.com.zagonel.catalogo_musical_api.service.album.delete.DeleteAlbumService;
import br.com.zagonel.catalogo_musical_api.service.album.retrive.get.GetAlbumService;
import br.com.zagonel.catalogo_musical_api.service.album.retrive.list.ListAlbumService;
import br.com.zagonel.catalogo_musical_api.service.album.update.UpdateAlbumService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class AlbumController implements AlbumApi {

    private final CreateAlbumService createAlbumService;
    private final GetAlbumService getAlbumService;
    private final ListAlbumService listAlbumService;
    private final UpdateAlbumService updateAlbumService;
    private final DeleteAlbumService deleteAlbumService;

    @Override
    public ResponseEntity<AlbumResponseDTO> create(final AlbumCreateRequestDTO input) {
        final var response = createAlbumService.execute(input);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Override
    public Page<AlbumResponseDTO> list(
            final String titulo,
            final String nomeArtista,
            final Integer tipoArtista,
            final LocalDate dataInicio,
            final LocalDate dataFim,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        final var query = new AlbumSearchQuery(
                page,
                perPage,
                sort,
                direction,
                titulo,
                nomeArtista,
                tipoArtista,
                dataInicio,
                dataFim
        );
        return listAlbumService.execute(query);
    }

    @Override
    public AlbumResponseDTO getById(final UUID id) {
        return getAlbumService.execute(id);
    }

    @Override
    public ResponseEntity<AlbumResponseDTO> updateById(final UUID id, final AlbumUpdateRequestDTO input) {
        final var response = updateAlbumService.execute(id, input);
        return ResponseEntity.ok(response);
    }

    @Override
    public void deleteById(final UUID id) {
        deleteAlbumService.execute(id);
    }
}
