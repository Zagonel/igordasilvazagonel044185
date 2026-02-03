package br.com.zagonel.catalogo_musical_api.service.album.retrive.get;

import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({
        GetAlbumService.class,
        AlbumMapper.class
})
public class GetAlbumServiceTest {

    @MockitoBean
    private AlbumRepository albumRepository;

    @MockitoBean
    private AlbumMapper albumMapper;

    @Autowired
    private GetAlbumService getAlbumService;

    private UUID albumUuid;
    private AlbumJpaEntity albumJpaEntity;
    private Album albumDomain;

    @BeforeEach
    void setUp() {
        albumUuid = UUID.randomUUID();
        albumJpaEntity = new AlbumJpaEntity();
        albumDomain = Album.criarNovoAlbum("Discovery", LocalDate.of(2001, 3, 12));
    }

    @Test
    @DisplayName("Deve retornar um álbum com sucesso ao buscar por ID válido")
    void deveBuscarAlbumComSucesso() {
        // GIVEN
        when(albumRepository.findByAlbumId(albumUuid)).thenReturn(Optional.of(albumJpaEntity));
        when(albumMapper.toDomain(albumJpaEntity)).thenReturn(albumDomain);
        when(albumMapper.toResponse(albumDomain)).thenReturn(new AlbumResponseDTO());

        // WHEN
        AlbumResponseDTO result = getAlbumService.execute(albumUuid);

        // THEN
        assertThat(result).isNotNull();
        verify(albumRepository).findByAlbumId(albumUuid);
        verify(albumMapper).toDomain(albumJpaEntity);
        verify(albumMapper).toResponse(albumDomain);
    }

    @Test
    @DisplayName("Deve lançar DomainException quando o álbum não for encontrado")
    void deveFalharQuandoAlbumNaoExistir() {
        // GIVEN
        when(albumRepository.findByAlbumId(any(UUID.class))).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> getAlbumService.execute(albumUuid))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Álbum não encontrado com o ID: " + albumUuid);

        verify(albumMapper, never()).toDomain(any());
        verify(albumMapper, never()).toResponse(any());
    }
}
