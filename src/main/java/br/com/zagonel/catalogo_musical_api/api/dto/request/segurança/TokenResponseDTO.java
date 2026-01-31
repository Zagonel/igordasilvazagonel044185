package br.com.zagonel.catalogo_musical_api.api.dto.request.segurança;

public record TokenResponseDTO(
        String accessToken,
        String refreshToken
) {
}
