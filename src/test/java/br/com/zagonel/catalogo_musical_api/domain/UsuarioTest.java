package br.com.zagonel.catalogo_musical_api.domain;

import br.com.zagonel.catalogo_musical_api.domain.enums.UserRole;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Usuario;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Domínio: Usuario")
public class UsuarioTest {

    @Test
    @DisplayName("Deve instanciar um usuário válido")
    void deveInstanciarUsuarioValido() {
        final var nomeExpected = "Igor Zagonel";
        final var emailExpected = "igor@email.com";
        final var senhaExpected = "senha123";
        final var roleExpected = UserRole.USUARIO;

        final var usuario = Usuario.criarNovoUsuario(nomeExpected, emailExpected, senhaExpected, roleExpected);

        assertNotNull(usuario.getId());
        assertEquals(nomeExpected, usuario.getNome());
        assertEquals(emailExpected, usuario.getEmail());
        assertEquals(senhaExpected, usuario.getSenha());
        assertEquals(roleExpected, usuario.getRole());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com nome inválido")
    void deveLancarExcecaoNomeInvalido() {
        final var nomeCurto = "Ig";
        final var email = "igor@email.com";
        final var senha = "senha";

        final var exception = assertThrows(DomainException.class, () ->
                Usuario.criarNovoUsuario(nomeCurto, email, senha, UserRole.USUARIO)
        );

        assertEquals("O nome do usuário deve ter pelo menos 3 caracteres.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar usuário com email em formato inválido")
    void deveLancarExcecaoEmailInvalido() {
        final var nome = "Igor Zagonel";
        final var emailInvalido = "email-sem-arroba.com";
        final var senha = "senha";

        final var exception = assertThrows(DomainException.class, () ->
                Usuario.criarNovoUsuario(nome, emailInvalido, senha, UserRole.USUARIO)
        );

        assertEquals("O formato do e-mail é inválido.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve restaurar um usuário com sucesso")
    void deveRestaurarUsuario() {
        final var idExpected = UUID.randomUUID();
        final var nomeExpected = "Admin";
        final var emailExpected = "admin@email.com";
        final var senhaExpected = "hash123";
        final var roleExpected = UserRole.ADMIN;

        final var usuario = Usuario.restaurar(idExpected, nomeExpected, emailExpected, senhaExpected, roleExpected);

        assertEquals(idExpected, usuario.getId());
        assertEquals(nomeExpected, usuario.getNome());
        assertEquals(emailExpected, usuario.getEmail());
        assertEquals(roleExpected, usuario.getRole());
    }
}
