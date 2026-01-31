package br.com.zagonel.catalogo_musical_api.domain.model;

import br.com.zagonel.catalogo_musical_api.domain.enums.UserRole;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import lombok.Getter;

import java.util.UUID;
import java.util.regex.Pattern;

@Getter
public class Usuario {
    private final UUID id;
    private final String nome;
    private final String email;
    private final String senha;
    private final UserRole role;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    private Usuario(UUID id, String nome, String email, String senha, UserRole role) {
        validarNome(nome);
        validarEmail(email);
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.role = role;
    }

    public static Usuario criarNovoUsuario(String nome, String email, String senhaCriptografada, UserRole role) {
        return new Usuario(UUID.randomUUID(), nome, email, senhaCriptografada, role);
    }

    public static Usuario restaurar(UUID id, String nome, String email, String senha, UserRole role) {
        return new Usuario(id, nome, email, senha, role);
    }

    private void validarNome(String nome) {
        if (nome == null || nome.trim().length() < 3) {
            throw new DomainException("O nome do usuário deve ter pelo menos 3 caracteres.");
        }
    }

    private void validarEmail(String email) {
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new DomainException("O formato do e-mail é inválido.");
        }
    }

}
