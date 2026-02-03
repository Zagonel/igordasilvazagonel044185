package br.com.zagonel.catalogo_musical_api.service.segurança;

import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.usuario.UsuarioCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.service.seguranca.usuario.CreateUsuarioService;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.UserMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
@ActiveProfiles("test")
@Import({CreateUsuarioService.class, UserMapper.class})
class CreateUsuarioServiceTest {

    @Autowired
    private CreateUsuarioService createUsuarioService;

    @Autowired
    private UserRepository userRepository;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @Test
    @DisplayName("Deve persistir um usuário no banco de dados e retornar o DTO correto")
    void devePersistirUsuarioNoBanco() {
        var request = new UsuarioCreateRequestDTO();
        request.setNome("Igor Zagonel");
        request.setEmail("igor@email.com");
        request.setSenha("senha123");

        String hashEsperado = "hash_criptografado_123";
        when(passwordEncoder.encode(anyString())).thenReturn(hashEsperado);

        var response = createUsuarioService.execute(request);

        assertNotNull(response);
        assertEquals("igor@email.com", response.username());

        var usuarioNoBanco = userRepository.findByEmail("igor@email.com");
        assertTrue(usuarioNoBanco.isPresent(), "O usuário deveria ter sido salvo no banco");

        var entity = usuarioNoBanco.get();
        assertEquals("Igor Zagonel", entity.getNome());
        assertEquals(hashEsperado, entity.getSenha());
        assertNotNull(entity.getUserId(), "O UUID deve ter sido gerado pelo domínio");
    }

    @Test
    @DisplayName("Deve lançar DomainException ao tentar cadastrar e-mail duplicado")
    void deveLancarExcecaoAoCadastrarEmailDuplicado() {
        // GIVEN
        var request = new UsuarioCreateRequestDTO();
        request.setNome("Igor");
        request.setEmail("igor@email.com");
        request.setSenha("123");

        String hashEsperado = "hash_criptografado_123";
        when(passwordEncoder.encode(anyString())).thenReturn(hashEsperado);

        createUsuarioService.execute(request); // Primeiro cadastro

        assertThatThrownBy(() -> createUsuarioService.execute(request))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Já existe um usuario cadastrado com o email: " + request.getEmail());

    }
}
