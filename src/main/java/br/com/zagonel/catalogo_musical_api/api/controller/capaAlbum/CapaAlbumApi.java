package br.com.zagonel.catalogo_musical_api.api.controller.capaAlbum;

import br.com.zagonel.catalogo_musical_api.api.dto.request.capaAlbum.CapaAlbumRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequestMapping(value = "albuns/{albumId}/capas")
@Tag(name = "Capas de Álbum")
public interface CapaAlbumApi {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Vincula uma nova capa ao álbum e faz upload para o MinIO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Capa vinculada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Álbum não encontrado"),
            @ApiResponse(responseCode = "500", description = "Erro interno ao processar upload")
    })
    void vincularCapa(
            @PathVariable UUID albumId,
            @RequestParam("files") List<MultipartFile> files,
            @ModelAttribute CapaAlbumRequest metadata
    );

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Desvincula uma capa do álbum e remove do MinIO")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Capa removida com sucesso"),
            @ApiResponse(responseCode = "404", description = "Álbum ou capa não encontrada")
    })
    void desvincularCapa(
            @PathVariable UUID albumId,
            @RequestParam("path") String path
    );

    @GetMapping("/link-assinado")
    @Operation(
            summary = "Gera um link temporário assinado",
            description = "Retorna uma URL do MinIO válida por 30 minutos para acessar o arquivo de imagem diretamente."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Link gerado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Caminho do arquivo inválido"),
            @ApiResponse(responseCode = "500", description = "Erro ao comunicar com o servidor de storage")
    })
    String gerarLinkAssinado(
            @Parameter(description = "O path completo do arquivo no storage (ex: albums/capa.jpg)", required = true)
            @PathVariable UUID albumId,
            @RequestParam String path
    );

}
