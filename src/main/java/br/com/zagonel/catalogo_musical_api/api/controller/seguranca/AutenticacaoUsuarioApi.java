package br.com.zagonel.catalogo_musical_api.api.controller.seguranca;

import br.com.zagonel.catalogo_musical_api.api.dto.request.usuario.LoginRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.usuario.UsuarioCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.LoginResponseDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.UsuarioResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("auth")
@Tag(name = "Autenticação", description = "Endpoints para gestão de acesso e novos utilizadores")
public interface AutenticacaoUsuarioApi {

    @Operation(summary = "Realiza o login de um utilizador")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/login")
    ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data);

    @Operation(summary = "Regista um novo utilizador no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Utilizador criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos"),
            @ApiResponse(responseCode = "422", description = "Erro de regra de negócio (ex: email já existe)"),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @PostMapping("/register")
    ResponseEntity<UsuarioResponseDTO> register(@RequestBody @Valid UsuarioCreateRequestDTO data);
}
