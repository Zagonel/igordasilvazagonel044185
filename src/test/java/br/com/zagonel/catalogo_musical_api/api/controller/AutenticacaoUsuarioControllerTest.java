package br.com.zagonel.catalogo_musical_api.api.controller;

import br.com.zagonel.catalogo_musical_api.api.controller.seguranca.AutenticacaoUsuarioController;
import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.RefreshTokenRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.TokenResponseDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.usuario.LoginRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.usuario.UsuarioCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.LoginResponseDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.UsuarioResponseDTO;
import br.com.zagonel.catalogo_musical_api.configuration.security.JwtAuthenticationFilter;
import br.com.zagonel.catalogo_musical_api.configuration.security.WebSecurityConfig;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.autenticacao.AutenticacaoUsuarioService;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.jwt.JwtService;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.jwt.RefreshTokenService;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.usuario.CreateUsuarioService;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.usuario.CustomUserDetailsService;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AutenticacaoUsuarioController.class)
@Import({WebSecurityConfig.class, JwtAuthenticationFilter.class})
class AutenticacaoUsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AutenticacaoUsuarioService autenticacaoUsuarioService;

    @MockitoBean
    private CreateUsuarioService createUsuarioService;

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("POST /auth/login - Deve autenticar e retornar tokens")
    void deveAutenticarUsuarioComSucesso() throws Exception {
        var loginRequest = new LoginRequestDTO("igor@email.com", "senha123");

        var tokenResponse = new LoginResponseDTO(loginRequest.getEmail(), "access-token-jwt", "refresh-token-uuid");

        when(autenticacaoUsuarioService.login(any(LoginRequestDTO.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/auth/login")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("access-token-jwt"))
                .andExpect(jsonPath("$.userName").value("igor@email.com"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-uuid"));
    }

    @Test
    @DisplayName("POST /auth/register - Deve criar usuário e retornar 201")
    void deveCriarUsuarioComSucesso() throws Exception {
        var createRequest = new UsuarioCreateRequestDTO();
        createRequest.setNome("Igor");
        createRequest.setEmail("igor@email.com");
        createRequest.setSenha("senha123");

        var usuarioResponse = new UsuarioResponseDTO("igor@email.com");

        when(createUsuarioService.execute(any(UsuarioCreateRequestDTO.class))).thenReturn(usuarioResponse);

        mockMvc.perform(post("/auth/register")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("igor@email.com"));
    }

    @Test
    @DisplayName("POST /auth/refresh-token - Deve renovar o access token")
    void deveRenovarTokenComSucesso() throws Exception {

        var refreshRequest = new RefreshTokenRequestDTO("refresh-token-valido");

        var tokenResponse = new TokenResponseDTO("novo-access-token-jwt", "refresh-token-valido");

        when(refreshTokenService.processRefreshToken(any(String.class))).thenReturn(tokenResponse);

        mockMvc.perform(post("/auth/refresh-token")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("novo-access-token-jwt"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token-valido"));
    }
}
