package br.com.zagonel.catalogo_musical_api.service.album.update;

import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumUpdateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.service.album.update.UpdateAlbumService;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.ArtistaMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.CapaAlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.ArtistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@Import({
        UpdateAlbumService.class,
        AlbumMapper.class,
        ArtistaMapper.class,
        CapaAlbumMapper.class
})
class UpdateAlbumServiceTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private UpdateAlbumService updateAlbumService;

    @Autowired
    private TestEntityManager entityManager;

    private UUID albumUuid;

    @BeforeEach
    void setUp() {
        UUID artistaUuid = UUID.randomUUID();
        ArtistaJpaEntity artista = new ArtistaJpaEntity();
        artista.setArtistaId(artistaUuid);
        artista.setNome("Daft Punk");
        artista.setTipo(TipoArtista.BANDA);
        artistaRepository.save(artista);

        albumUuid = UUID.randomUUID();
        AlbumJpaEntity album = new AlbumJpaEntity();
        album.setAlbumId(albumUuid);
        album.setTitulo("Discovery");
        album.setDataLancamento(LocalDate.of(2001, 3, 12));
        album.setArtistas(new ArrayList<>(List.of(artista)));
        albumRepository.save(album);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Deve atualizar dados básicos do álbum com sucesso")
    void deveAtualizarDadosBasicosComSucesso() {
        AlbumUpdateRequestDTO dto = new AlbumUpdateRequestDTO();
        dto.setTitulo("Homework");
        dto.setDataLancamento(LocalDate.of(1997, 1, 20));

        AlbumResponseDTO result = updateAlbumService.execute(albumUuid, dto);

        entityManager.flush();
        entityManager.clear();

        assertThat(result.getTitulo()).isEqualTo("Homework");

        AlbumJpaEntity albumNoBanco = albumRepository.findByAlbumId(albumUuid).get();
        assertThat(albumNoBanco.getTitulo()).isEqualTo("Homework");
        assertThat(albumNoBanco.getDataLancamento()).isEqualTo(LocalDate.of(1997, 1, 20));
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar álbum inexistente")
    void deveFalharAoAtualizarAlbumInexistente() {
        AlbumUpdateRequestDTO dto = new AlbumUpdateRequestDTO();
        dto.setTitulo("Novo Titulo");

        assertThatThrownBy(() -> updateAlbumService.execute(UUID.randomUUID(), dto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Álbum não encontrado");
    }

    @Test
    @DisplayName("Deve manter os artistas vinculados ao atualizar apenas o título")
    void deveManterArtistasAoAtualizarDadosBasicos() {
        AlbumUpdateRequestDTO dto = new AlbumUpdateRequestDTO();
        dto.setTitulo("Discovery Special Edition");

        updateAlbumService.execute(albumUuid, dto);

        entityManager.flush();
        entityManager.clear();

        AlbumJpaEntity albumNoBanco = albumRepository.findByAlbumId(albumUuid).get();
        assertThat(albumNoBanco.getArtistas()).hasSize(1);
        assertThat(albumNoBanco.getArtistas().get(0).getNome()).isEqualTo("Daft Punk");
    }
}
