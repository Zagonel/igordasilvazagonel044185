package br.com.zagonel.catalogo_musical_api.service.artista.create;

import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.service.artista.create.CreateArtistaService;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.ArtistaMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.CapaAlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
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
        CreateArtistaService.class,
        ArtistaMapper.class,
        AlbumMapper.class,
        CapaAlbumMapper.class
})
class CreateArtistaServiceTest {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private CreateArtistaService createArtistaService;

    @Autowired
    private TestEntityManager entityManager;

    private ArtistaCreateRequestDTO artistaCreateRequestDTO;
    private UUID albumUuid;

    @BeforeEach
    void setUp() {
        albumUuid = UUID.randomUUID();
        artistaCreateRequestDTO = new ArtistaCreateRequestDTO();
        artistaCreateRequestDTO.setNome("Daft Punk");
        artistaCreateRequestDTO.setTipo(TipoArtista.BANDA);
        artistaCreateRequestDTO.setAlbunsIds(List.of(albumUuid.toString()));

        AlbumJpaEntity albumJpa = new AlbumJpaEntity();
        albumJpa.setAlbumId(albumUuid);
        albumJpa.setTitulo("Discovery");
        albumJpa.setDataLancamento(LocalDate.of(2001, 3, 12));
        albumRepository.save(albumJpa);
    }

    @Test
    @DisplayName("Deve criar um artista com sucesso e persistir vínculos no H2")
    void deveCriarArtistaComSucesso() {

        ArtistaResponseDTO result = createArtistaService.execute(artistaCreateRequestDTO);
        assertThat(result).isNotNull();
        assertEquals(artistaCreateRequestDTO.getNome(), result.getNome());

        var artistaPersistido = artistaRepository.findAll().stream()
                .filter(a -> a.getNome().equals("Daft Punk"))
                .findFirst()
                .orElseThrow();

        assertThat(artistaPersistido.getArtistaId()).isNotNull();
        assertThat(artistaPersistido.getNome()).isEqualTo("Daft Punk");
        assertThat(artistaPersistido.getTipo()).isEqualTo(TipoArtista.BANDA);
        assertThat(artistaPersistido.getAlbuns().getFirst().getAlbumId()).isEqualTo(albumUuid);
        assertThat(artistaPersistido.getAlbuns()).hasSize(1);

        entityManager.flush();
        entityManager.clear();

        var albumNoBanco = albumRepository.findByAlbumId(albumUuid).get();
        assertThat(albumNoBanco.getArtistas()).hasSize(1);
        assertThat(albumNoBanco.getArtistas().getFirst().getNome()).isEqualTo("Daft Punk");
    }

    @Test
    @DisplayName("Deve lançar DomainException quando o álbum não for encontrado")
    void deveFalharQuandoAlbumNaoExistir() {
        UUID randomUUID = UUID.randomUUID();
        artistaCreateRequestDTO.setAlbunsIds(List.of(randomUUID.toString()));

        assertThatThrownBy(() -> createArtistaService.execute(artistaCreateRequestDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Não foi possível encontrar o álbum com ID: " + randomUUID);

        assertThat(artistaRepository.findAll()).isEmpty();
    }
}
