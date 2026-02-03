package br.com.zagonel.catalogo_musical_api.service.artista.retrive.list;

import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaSearchQuery;
import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
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
        ListArtistaService.class,
        ArtistaMapper.class,
        AlbumMapper.class,
        CapaAlbumMapper.class
})
class ListArtistaServiceTest {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ListArtistaService listArtistaService;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        ArtistaJpaEntity artista1 = new ArtistaJpaEntity();
        artista1.setArtistaId(UUID.randomUUID());
        artista1.setNome("Iron Maiden");
        artista1.setTipo(TipoArtista.BANDA);
        artistaRepository.save(artista1);

        ArtistaJpaEntity artista2 = new ArtistaJpaEntity();
        artista2.setArtistaId(UUID.randomUUID());
        artista2.setNome("Michael Jackson");
        artista2.setTipo(TipoArtista.CANTOR);
        artistaRepository.save(artista2);

        AlbumJpaEntity album = new AlbumJpaEntity();
        album.setAlbumId(UUID.randomUUID());
        album.setTitulo("Powerslave");
        album.setDataLancamento(LocalDate.of(1984, 9, 3));
        album.setArtistas(new ArrayList<>(List.of(artista1)));
        albumRepository.save(album);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Deve listar todos os artistas com paginação")
    void deveListarArtistasComPaginacao() {
        ArtistaSearchQuery query = new ArtistaSearchQuery(0, 10, "nome", "ASC", null, null, null);

        Page<ArtistaResponseDTO> result = listArtistaService.execute(query);

        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getNome()).isEqualTo("Iron Maiden");
    }

    @Test
    @DisplayName("Deve filtrar artistas por nome parcial")
    void deveFiltrarPorNome() {
        ArtistaSearchQuery query = new ArtistaSearchQuery(0, 10, "nome", "ASC", "Michael", null, null);

        Page<ArtistaResponseDTO> result = listArtistaService.execute(query);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getNome()).isEqualTo("Michael Jackson");
    }

    @Test
    @DisplayName("Deve filtrar artistas por tipo")
    void deveFiltrarPorTipo() {
        ArtistaSearchQuery query = new ArtistaSearchQuery(0, 10, "nome", "ASC", null, TipoArtista.BANDA.getCodigo(), null);

        Page<ArtistaResponseDTO> result = listArtistaService.execute(query);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getNome()).isEqualTo("Iron Maiden");
    }

    @Test
    @DisplayName("Deve filtrar artistas por nome de álbum vinculado")
    void deveFiltrarPorNomeAlbum() {
        ArtistaSearchQuery query = new ArtistaSearchQuery(0, 10, "nome", "ASC", null, null, "Powerslave");

        Page<ArtistaResponseDTO> result = listArtistaService.execute(query);

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getNome()).isEqualTo("Iron Maiden");
    }
}
