package br.com.zagonel.catalogo_musical_api.service.album.delete;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.service.album.delete.DeleteAlbumService;
import br.com.zagonel.catalogo_musical_api.domain.service.album.retrive.list.ListAlbumService;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.ArtistaMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.CapaAlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@DataJpaTest
@ActiveProfiles("test")
@Import({
        DeleteAlbumService.class,
})
class DeleteAlbumServiceTest {

    @MockitoBean
    private AlbumRepository albumRepository;

    @Autowired
    private DeleteAlbumService deleteAlbumService;

    private UUID albumUuid;
    private AlbumJpaEntity albumJpaEntity;

    @BeforeEach
    void setUp() {
        albumUuid = UUID.randomUUID();
        albumJpaEntity = new AlbumJpaEntity();
    }

    @Test
    @DisplayName("Deve deletar um álbum com sucesso ao informar um ID válido")
    void deveDeletarAlbumComSucesso() {
        when(albumRepository.findByAlbumId(albumUuid)).thenReturn(Optional.of(albumJpaEntity));

        assertThatCode(() -> deleteAlbumService.execute(albumUuid))
                .doesNotThrowAnyException();

        verify(albumRepository, times(1)).delete(albumJpaEntity);
        verify(albumRepository, times(1)).findByAlbumId(albumUuid);
    }

    @Test
    @DisplayName("Deve lançar DomainException ao tentar deletar um álbum inexistente")
    void deveFalharAoDeletarAlbumInexistente() {
        when(albumRepository.findByAlbumId(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> deleteAlbumService.execute(albumUuid))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Álbum não encontrado");

        verify(albumRepository, never()).delete((AlbumJpaEntity) any());
    }
}
