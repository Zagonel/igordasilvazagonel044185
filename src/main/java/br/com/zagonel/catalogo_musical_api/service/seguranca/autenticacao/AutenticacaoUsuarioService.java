package br.com.zagonel.catalogo_musical_api.service.seguranca.autenticacao;

import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.usuario.LoginRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.LoginResponseDTO;
import br.com.zagonel.catalogo_musical_api.service.seguranca.jwt.CreateRefreshTokenService;
import br.com.zagonel.catalogo_musical_api.service.seguranca.jwt.JwtService;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.UserJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AutenticacaoUsuarioService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final CreateRefreshTokenService refreshTokenService;

    public LoginResponseDTO login(LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getSenha());

        var auth = this.authenticationManager.authenticate(usernamePassword);
        var user = (UserJpaEntity) auth.getPrincipal();

        var accessToken = jwtService.generateToken((UserJpaEntity) Objects.requireNonNull(auth.getPrincipal()));

        assert user != null;
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new LoginResponseDTO(data.getEmail(), accessToken, refreshToken);
    }
}
