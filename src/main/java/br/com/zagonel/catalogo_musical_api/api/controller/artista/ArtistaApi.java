package br.com.zagonel.catalogo_musical_api.api.controller.artista;

import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaUpdateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(value = "artistas")
@Tag(name = "Artistas")
public interface ArtistaApi {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Cria um novo artista")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Um erro de validação foi lançado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno do servidor foi lançado"),
    })
    ResponseEntity<ArtistaResponseDTO> create(@RequestBody @Valid ArtistaCreateRequestDTO input);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os artistas de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artistas listados"),
            @ApiResponse(responseCode = "500", description = "Um erro interno do servidor foi lançado"),
    })
    Page<ArtistaResponseDTO> list(
            @RequestParam(name = "nome", required = false) final String nomeArtista,
            @RequestParam(name = "tipo", required = false) final Integer tipoArtista,
            @RequestParam(name = "nomeAlbum", required = false) final String nomeAlbum,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "nome") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Busca um artista pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista recuperado"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno do servidor foi lançado"),
    })
    ArtistaResponseDTO getById(@PathVariable UUID id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Atualiza um artista pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Artista atualizado"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado"),
            @ApiResponse(responseCode = "422", description = "Um erro de validação foi lançado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno do servidor foi lançado"),
    })
    ResponseEntity<ArtistaResponseDTO> updateById(@PathVariable UUID id, @RequestBody @Valid ArtistaUpdateRequestDTO input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um artista pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Artista excluído"),
            @ApiResponse(responseCode = "404", description = "Artista não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno do servidor foi lançado"),
    })
    void deleteById(@PathVariable UUID id);
}
