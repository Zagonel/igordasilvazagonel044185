package br.com.zagonel.catalogo_musical_api.api.controller.artista_album;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequestMapping(value = "albuns/{albumId}/artistas/{artistaId}")
@Tag(name = "Relacionamento Artista e Álbum")
public interface ArtistaAlbumApi {

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Vincula um artista a um álbum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vínculo realizado"),
            @ApiResponse(responseCode = "404", description = "Álbum ou Artista não encontrado")
    })
    void vincular(@PathVariable UUID albumId, @PathVariable UUID artistaId);

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remove o vínculo entre um artista e um álbum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Vínculo removido"),
            @ApiResponse(responseCode = "404", description = "Álbum ou Artista não encontrado")
    })
    void desvincular(@PathVariable UUID albumId, @PathVariable UUID artistaId);
}
