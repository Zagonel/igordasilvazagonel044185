package br.com.zagonel.catalogo_musical_api.service.segurança;

import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.usuario.LoginRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.LoginResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.autenticacao.AutenticacaoUsuarioService;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.jwt.CreateRefreshTokenService;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.jwt.JwtService;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.UserJpaEntity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AutenticacaoUsuarioServiceTest {

    @InjectMocks
    private AutenticacaoUsuarioService autenticacaoUsuarioService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    @Mock
    private UserJpaEntity userJpaEntity;

    @Mock
    private CreateRefreshTokenService createRefreshTokenService;

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar token")
    void deveRealizarLoginComSucesso() {
        var email = "igor@email.com";
        var senha = "senha123";
        var tokenEsperado = "token.jwt.valido";
        var refreshTokenEsperado = "token.refresh.jwt.valido";

        var loginRequest = new LoginRequestDTO();
        loginRequest.setEmail(email);
        loginRequest.setSenha(senha);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);

        when(authentication.getPrincipal()).thenReturn(userJpaEntity);

        when(jwtService.generateToken(userJpaEntity)).thenReturn(tokenEsperado);

        when(createRefreshTokenService.createRefreshToken(userJpaEntity.getId())).thenReturn(refreshTokenEsperado);

        LoginResponseDTO response = autenticacaoUsuarioService.login(loginRequest);

        assertNotNull(response);
        assertEquals(email, response.getUserName());
        assertEquals(tokenEsperado, response.getAccessToken());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).generateToken(userJpaEntity);
    }

    @Test
    @DisplayName("Deve lançar exceção quando as credenciais forem inválidas")
    void deveLancarExcecaoQuandoCredenciaisInvalidas() {
        var loginRequest = new LoginRequestDTO();
        loginRequest.setEmail("igor@email.com");
        loginRequest.setSenha("senhaErrada");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThrows(BadCredentialsException.class, () -> {
            autenticacaoUsuarioService.login(loginRequest);
        });

        verify(jwtService, never()).generateToken(any());
    }
}
