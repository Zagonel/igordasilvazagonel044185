package br.com.zagonel.catalogo_musical_api.domain.service.seguranca.autenticacao;

import br.com.zagonel.catalogo_musical_api.api.dto.request.usuario.LoginRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.LoginResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.jwt.JwtService;
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

    public LoginResponseDTO login(LoginRequestDTO data){
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.getEmail(), data.getSenha());

        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = jwtService.generateToken((UserJpaEntity) Objects.requireNonNull(auth.getPrincipal()));

        return new LoginResponseDTO(data.getEmail(), token);
    }
}
