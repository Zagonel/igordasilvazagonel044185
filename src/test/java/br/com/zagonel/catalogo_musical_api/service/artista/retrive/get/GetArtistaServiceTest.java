package br.com.zagonel.catalogo_musical_api.service.artista.retrive.get;

import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.ArtistaMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.CapaAlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.ArtistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@Import({
        GetArtistaService.class,
        ArtistaMapper.class,
        AlbumMapper.class,
        CapaAlbumMapper.class
})
class GetArtistaServiceTest {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private GetArtistaService getArtistaService;

    @Autowired
    private TestEntityManager entityManager;

    private UUID artistaUuid;

    @BeforeEach
    void setUp() {
        artistaUuid = UUID.randomUUID();
        ArtistaJpaEntity artista = new ArtistaJpaEntity();
        artista.setArtistaId(artistaUuid);
        artista.setNome("Daft Punk");
        artista.setTipo(TipoArtista.BANDA);

        artistaRepository.save(artista);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Deve retornar um artista com sucesso ao buscar por ID válido")
    void deveBuscarArtistaComSucesso() {
        ArtistaResponseDTO result = getArtistaService.execute(artistaUuid);

        assertThat(result).isNotNull();
        assertThat(result.getNome()).isEqualTo("Daft Punk");
        assertThat(result.getTipo()).isEqualTo(TipoArtista.BANDA);
        assertThat(result.getId()).isEqualTo(artistaUuid);
    }

    @Test
    @DisplayName("Deve lançar DomainException quando o artista não for encontrado")
    void deveFalharQuandoArtistaNaoExistir() {
        UUID idInexistente = UUID.randomUUID();

        assertThatThrownBy(() -> getArtistaService.execute(idInexistente))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Artista não encontrado com o ID: " + idInexistente);
    }
}
