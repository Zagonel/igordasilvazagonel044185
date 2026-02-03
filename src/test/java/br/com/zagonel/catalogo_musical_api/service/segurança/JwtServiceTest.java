package br.com.zagonel.catalogo_musical_api.service.segurança;

import br.com.zagonel.catalogo_musical_api.service.seguranca.jwt.JwtService;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.UserJpaEntity;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @InjectMocks
    private JwtService jwtService;

    @Mock
    private UserJpaEntity user;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(jwtService, "secret", "my-secret-key-test-1234567890");
        ReflectionTestUtils.setField(jwtService, "expirationMinutes", 5);
    }

    @Test
    @DisplayName("Deve gerar um token válido")
    void deveGerarTokenValido() {
        when(user.getEmail()).thenReturn("teste@email.com");
        String token = jwtService.generateToken(user);
        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    @DisplayName("Deve validar um token com sucesso e retornar o email (subject)")
    void deveValidarTokenComSucesso() {
        when(user.getEmail()).thenReturn("teste@email.com");

        String token = jwtService.generateToken(user);
        String subject = jwtService.validateToken(token);

        assertEquals("teste@email.com", subject);
    }

    @Test
    @DisplayName("Deve retornar vazio ao validar token com assinatura inválida")
    void deveRetornarVazioParaTokenInvalido() {
        String tokenFalso = JWT.create()
                .withIssuer("catalogo-musical-api")
                .withSubject("teste@email.com")
                .sign(Algorithm.HMAC256("outra-chave-qualquer"));

        String subject = jwtService.validateToken(tokenFalso);

        assertEquals("Invalido", subject, "Deveria retornar string Invalido para assinatura inválida");
    }

    @Test
    @DisplayName("Deve retornar vazio para token mal formatado")
    void deveRetornarVazioParaTokenMalFormatado() {
        String subject = jwtService.validateToken("token.invalido.123");
        assertEquals("Invalido", subject);
    }
}
