package br.com.zagonel.catalogo_musical_api.api.controller.seguranca;


import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.RefreshTokenRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.TokenResponseDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.usuario.LoginRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.usuario.UsuarioCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.LoginResponseDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.UsuarioResponseDTO;
import br.com.zagonel.catalogo_musical_api.service.seguranca.autenticacao.AutenticacaoUsuarioService;
import br.com.zagonel.catalogo_musical_api.service.seguranca.jwt.RefreshTokenService;
import br.com.zagonel.catalogo_musical_api.service.seguranca.usuario.CreateUsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AutenticacaoUsuarioController implements AutenticacaoUsuarioApi {

    private final AutenticacaoUsuarioService autenticacaoUsuarioService;
    private final CreateUsuarioService createUsuarioService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO data) {
        var token = autenticacaoUsuarioService.login(data);
        return ResponseEntity.ok(token);
    }

    @Override
    public ResponseEntity<UsuarioResponseDTO> register(UsuarioCreateRequestDTO data) {
        var usuarioResponse = createUsuarioService.execute(data);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioResponse);
    }

    @Override
    public ResponseEntity<TokenResponseDTO> refreshToken(RefreshTokenRequestDTO request) {
        var tokenResponse = refreshTokenService.processRefreshToken(request.getToken());
        return ResponseEntity.ok(tokenResponse);
    }
}
