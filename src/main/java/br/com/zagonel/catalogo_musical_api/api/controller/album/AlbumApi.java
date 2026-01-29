package br.com.zagonel.catalogo_musical_api.api.controller.album;

import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumUpdateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.UUID;

@RequestMapping(value = "albuns")
@Tag(name = "Álbuns")
public interface AlbumApi {

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Cria um novo álbum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Criado com sucesso"),
            @ApiResponse(responseCode = "422", description = "Um erro de validação foi lançado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno do servidor foi lançado"),
    })
    ResponseEntity<AlbumResponseDTO> create(@RequestBody @Valid AlbumCreateRequestDTO input);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Lista todos os álbuns de forma paginada")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Álbuns listados"),
            @ApiResponse(responseCode = "500", description = "Um erro interno do servidor foi lançado"),
    })
    Page<AlbumResponseDTO> list(
            @RequestParam(name = "titulo", required = false) final String titulo,
            @RequestParam(name = "nomeArtista", required = false) final String nomeArtista,
            @RequestParam(name = "tipoArtista", required = false) final Integer tipoArtista,
            @RequestParam(name = "dataInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataInicio,
            @RequestParam(name = "dataFim", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) final LocalDate dataFim,
            @RequestParam(name = "page", required = false, defaultValue = "0") final int page,
            @RequestParam(name = "perPage", required = false, defaultValue = "10") final int perPage,
            @RequestParam(name = "sort", required = false, defaultValue = "titulo") final String sort,
            @RequestParam(name = "dir", required = false, defaultValue = "asc") final String direction
    );

    @GetMapping(value = "{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(summary = "Busca um álbum pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Álbum recuperado"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno do servidor foi lançado"),
    })
    AlbumResponseDTO getById(@PathVariable UUID id);

    @PutMapping(
            value = "{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @Operation(summary = "Atualiza um álbum pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Álbum atualizado"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "422", description = "Um erro de validação foi lançado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno do servidor foi lançado"),
    })
    ResponseEntity<AlbumResponseDTO> updateById(@PathVariable UUID id, @RequestBody @Valid AlbumUpdateRequestDTO input);

    @DeleteMapping(value = "{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Exclui um álbum pelo seu identificador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Álbum excluído"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "500", description = "Um erro interno do servidor foi lançado"),
    })
    void deleteById(@PathVariable UUID id);
}
