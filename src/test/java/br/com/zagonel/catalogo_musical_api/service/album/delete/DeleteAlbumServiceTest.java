package br.com.zagonel.catalogo_musical_api.service.album.delete;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.service.album.delete.DeleteAlbumService;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
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
        // GIVEN: O repositório encontra o álbum
        when(albumRepository.findByAlbumId(albumUuid)).thenReturn(Optional.of(albumJpaEntity));

        // WHEN: Executamos a exclusão
        // THEN: Não deve lançar nenhuma exceção
        assertThatCode(() -> deleteAlbumService.execute(albumUuid))
                .doesNotThrowAnyException();

        // Verifica se o método delete foi chamado com a entidade correta
        verify(albumRepository, times(1)).delete(albumJpaEntity);
        verify(albumRepository, times(1)).findByAlbumId(albumUuid);
    }

    @Test
    @DisplayName("Deve lançar DomainException ao tentar deletar um álbum inexistente")
    void deveFalharAoDeletarAlbumInexistente() {
        // GIVEN: O repositório não encontra o álbum
        when(albumRepository.findByAlbumId(any(UUID.class))).thenReturn(Optional.empty());

        // WHEN & THEN: Deve lançar a exceção com a mensagem correta
        assertThatThrownBy(() -> deleteAlbumService.execute(albumUuid))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Álbum não encontrado");

        // Garante que o método delete NUNCA foi chamado
        verify(albumRepository, never()).delete((AlbumJpaEntity) any());
    }
}
