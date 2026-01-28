package br.com.zagonel.catalogo_musical_api.service.album.create;

import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.service.album.create.CreateAlbumService;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.ArtistaMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.CapaAlbumMapper;
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
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
@Import({
        CreateAlbumService.class,
        AlbumMapper.class,
        ArtistaMapper.class,
        CapaAlbumMapper.class
})
class CreateAlbumServiceTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private CreateAlbumService createAlbumService;

    @Autowired
    private TestEntityManager entityManager;

    private AlbumCreateRequestDTO albumCreateRequestDTO;
    private UUID artistaUuid;

    @BeforeEach
    void setUp() {
        artistaUuid = UUID.randomUUID();
        albumCreateRequestDTO = new AlbumCreateRequestDTO();
        albumCreateRequestDTO.setTitulo("Thriller");
        albumCreateRequestDTO.setDataLancamento(LocalDate.of(1982, 11, 30));
        albumCreateRequestDTO.setArtistasIds(List.of(artistaUuid.toString()));

        ArtistaJpaEntity artistaJpa = new ArtistaJpaEntity();
        artistaJpa.setArtistaId(artistaUuid);
        artistaJpa.setNome("Michael Jackson");
        artistaJpa.setTipo(TipoArtista.CANTOR);
        artistaRepository.save(artistaJpa);
    }

    @Test
    @DisplayName("Deve criar um álbum com sucesso e persistir vínculos no H2")
    void deveCriarAlbumComSucesso() {

        AlbumResponseDTO result = createAlbumService.execute(albumCreateRequestDTO);
        assertThat(result).isNotNull();
        assertEquals(albumCreateRequestDTO.getTitulo(), result.getTitulo());

        var albumPersistido = albumRepository.findAll().stream()
                .filter(a -> a.getTitulo().equals("Thriller"))
                .findFirst()
                .orElseThrow();

        assertThat(albumPersistido.getAlbumId()).isNotNull();
        assertThat(albumPersistido.getTitulo()).isEqualTo("Thriller");

        assertThat(albumPersistido.getArtistas()).hasSize(1);
        assertThat(albumPersistido.getArtistas().getFirst().getArtistaId()).isEqualTo(artistaUuid);

        entityManager.flush();
        entityManager.clear();

        var artistaNoBanco = artistaRepository.findByArtistaId(artistaUuid).get();
        assertThat(artistaNoBanco.getAlbuns()).hasSize(1);
        assertThat(artistaNoBanco.getAlbuns().getFirst().getTitulo()).isEqualTo("Thriller");
    }

    @Test
    @DisplayName("Deve lançar DomainException quando o artista não for encontrado")
    void deveFalharQuandoArtistaNaoExistir() {
        UUID ramdomUUID = UUID.randomUUID();
        albumCreateRequestDTO.setArtistasIds(List.of(ramdomUUID.toString()));

        assertThatThrownBy(() -> createAlbumService.execute(albumCreateRequestDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Não foi possivel encontrar o artista com ID: " + ramdomUUID);

        assertThat(albumRepository.findAll()).isEmpty();
    }
}
