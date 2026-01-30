package br.com.zagonel.catalogo_musical_api.domain.model;

import br.com.zagonel.catalogo_musical_api.domain.enums.UserRole;
import lombok.Getter;

import java.util.UUID;

@Getter
public class Usuario {
    private final UUID id;
    private final String nome;
    private final String email;
    private final String senha;
    private final UserRole role;

    private Usuario(UUID id, String nome, String email, String senha, UserRole role) {
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

}
