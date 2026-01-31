package br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.usuario;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
public class UsuarioCreateRequestDTO {

    @NotBlank(message = "Email é Obrigatorio")
    private String email;

    @NotBlank(message = "Nome é Obrigatorio")
    private String nome;

    @NotBlank(message = "Senha é Obrigatorio")
    private String senha;
}
