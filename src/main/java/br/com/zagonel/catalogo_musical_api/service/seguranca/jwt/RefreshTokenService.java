package br.com.zagonel.catalogo_musical_api.service.seguranca.jwt;

import br.com.zagonel.catalogo_musical_api.api.dto.request.segurança.TokenResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.RefreshTokenJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    /**
     * Processa a renovação: Valida o token antigo e gera um novo Access Token
     */
    @Transactional
    public TokenResponseDTO processRefreshToken(String requestToken) {
        return refreshTokenRepository.findByToken(requestToken)
                .map(this::VerificaValidadeToken)
                .map(RefreshTokenJpaEntity::getUser)
                .map(user -> {
                    String newAccessToken = jwtService.generateToken(user);
                    return new TokenResponseDTO(newAccessToken, requestToken);
                })
                .orElseThrow(() -> new DomainException("Refresh token não encontrado no banco de dados!"));
    }

    private RefreshTokenJpaEntity VerificaValidadeToken(RefreshTokenJpaEntity token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new DomainException("Refresh token expirado. Por favor, faça login novamente.");
        }
        return token;
    }
}
