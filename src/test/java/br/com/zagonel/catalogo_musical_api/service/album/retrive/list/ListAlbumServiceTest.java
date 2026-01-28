package br.com.zagonel.catalogo_musical_api.service.album.retrive.list;

import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumSearchQuery;
import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.service.album.retrive.list.ListAlbumService;
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
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@Import({
        ListAlbumService.class,
        AlbumMapper.class,
        ArtistaMapper.class,
        CapaAlbumMapper.class
})
class ListAlbumServiceTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private ListAlbumService listAlbumService;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        ArtistaJpaEntity artista = new ArtistaJpaEntity();
        artista.setArtistaId(UUID.randomUUID());
        artista.setNome("Iron Maiden");
        artista.setTipo(TipoArtista.BANDA);
        artistaRepository.save(artista);

        AlbumJpaEntity album1 = new AlbumJpaEntity();
        album1.setAlbumId(UUID.randomUUID());
        album1.setTitulo("Powerslave");
        album1.setDataLancamento(LocalDate.of(1984, 9, 3));
        album1.setArtistas(new ArrayList<>(List.of(artista)));
        albumRepository.save(album1);

        AlbumJpaEntity album2 = new AlbumJpaEntity();
        album2.setAlbumId(UUID.randomUUID());
        album2.setTitulo("Senjutsu");
        album2.setDataLancamento(LocalDate.of(2021, 9, 3));
        album2.setArtistas(new ArrayList<>(List.of(artista)));
        albumRepository.save(album2);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Deve listar todos os álbuns com paginação")
    void deveListarAlbunsComPaginacao() {
        AlbumSearchQuery query = new AlbumSearchQuery(0, 10, "titulo", "ASC", null, null, null, null, null);

        Page<AlbumResponseDTO> result = listAlbumService.execute(query);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getTitulo()).isEqualTo("Powerslave");
    }

    @Test
    @DisplayName("Deve filtrar álbuns por título parcial")
    void deveFiltrarPorTitulo() {
        AlbumSearchQuery query = new AlbumSearchQuery(0, 10, "titulo", "ASC", "Power", null, null, null, null);

        Page<AlbumResponseDTO> result = listAlbumService.execute(query);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitulo()).isEqualTo("Powerslave");
    }

    @Test
    @DisplayName("Deve filtrar álbuns por nome do artista")
    void deveFiltrarPorNomeArtista() {
        AlbumSearchQuery query = new AlbumSearchQuery(0, 10, "titulo", "ASC", null, "Maiden", null, null, null);

        Page<AlbumResponseDTO> result = listAlbumService.execute(query);

        assertThat(result.getContent()).hasSize(2);
    }

    @Test
    @DisplayName("Deve filtrar álbuns por intervalo de data de lançamento")
    void deveFiltrarPorIntervaloDatas() {
        AlbumSearchQuery query = new AlbumSearchQuery(
                0, 10, "titulo", "ASC", null, null, null,
                LocalDate.of(1980, 1, 1),
                LocalDate.of(1990, 12, 31)
        );

        Page<AlbumResponseDTO> result = listAlbumService.execute(query);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getTitulo()).isEqualTo("Powerslave");
    }
}
