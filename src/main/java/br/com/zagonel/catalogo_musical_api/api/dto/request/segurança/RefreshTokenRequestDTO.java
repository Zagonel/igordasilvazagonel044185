package br.com.zagonel.catalogo_musical_api.api.dto.request.segurança;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RefreshTokenRequestDTO {
    @NotBlank(message = "O refresh token é obrigatório")
    private String token;
}
