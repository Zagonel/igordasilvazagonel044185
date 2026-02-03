package br.com.zagonel.catalogo_musical_api.service.seguranca.jwt;

import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.RefreshTokenJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.RefreshTokenRepository;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class CreateRefreshTokenService {

    @Value("${security.jwt.refresh-expiration-minutes}")
    private Long refreshTokenDurationMin;

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    /**
     * Cria um Refresh Token do zero para um usuário (usado no Login)
     */
    @Transactional
    public String createRefreshToken(Long userId) {
        refreshTokenRepository.deleteByUserId(userId);
        RefreshTokenJpaEntity refreshToken = new RefreshTokenJpaEntity();
        refreshToken.setUser(userRepository.findById(userId).orElseThrow());

        refreshToken.setExpiryDate(Instant.now().plusMillis(TimeUnit.MINUTES.toMillis(refreshTokenDurationMin)));

        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken.getToken();
    }
}
