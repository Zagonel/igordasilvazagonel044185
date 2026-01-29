package br.com.zagonel.catalogo_musical_api.service.artista.update;

import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaUpdateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.service.artista.update.UpdateArtistaService;
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
        UpdateArtistaService.class,
        ArtistaMapper.class,
        AlbumMapper.class,
        CapaAlbumMapper.class
})
class UpdateArtistaServiceTest {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private UpdateArtistaService updateArtistaService;

    @Autowired
    private TestEntityManager entityManager;

    private UUID artistaUuid;

    @BeforeEach
    void setUp() {
        UUID albumUuid = UUID.randomUUID();
        AlbumJpaEntity album = new AlbumJpaEntity();
        album.setAlbumId(albumUuid);
        album.setTitulo("Discovery");
        album.setDataLancamento(LocalDate.of(2001, 3, 12));
        albumRepository.save(album);

        artistaUuid = UUID.randomUUID();
        ArtistaJpaEntity artista = new ArtistaJpaEntity();
        artista.setArtistaId(artistaUuid);
        artista.setNome("Daft Punk");
        artista.setTipo(TipoArtista.BANDA);
        artista.setAlbuns(new ArrayList<>(List.of(album)));
        artistaRepository.save(artista);

        album.setArtistas(new ArrayList<>(List.of(artista)));
        albumRepository.save(album);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Deve atualizar dados básicos do artista com sucesso")
    void deveAtualizarDadosBasicosComSucesso() {
        ArtistaUpdateRequestDTO dto = new ArtistaUpdateRequestDTO();
        dto.setNome("Guy-Manuel de Homem-Christo");
        dto.setTipo(TipoArtista.CANTOR);

        ArtistaResponseDTO result = updateArtistaService.execute(artistaUuid, dto);

        entityManager.flush();
        entityManager.clear();

        assertThat(result.getNome()).isEqualTo("Guy-Manuel de Homem-Christo");
        assertThat(result.getTipo()).isEqualTo(TipoArtista.CANTOR);

        ArtistaJpaEntity artistaNoBanco = artistaRepository.findByArtistaId(artistaUuid).get();
        assertThat(artistaNoBanco.getNome()).isEqualTo("Guy-Manuel de Homem-Christo");
        assertThat(artistaNoBanco.getTipo()).isEqualTo(TipoArtista.CANTOR);
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar atualizar artista inexistente")
    void deveFalharAoAtualizarArtistaInexistente() {
        ArtistaUpdateRequestDTO dto = new ArtistaUpdateRequestDTO();
        dto.setNome("Novo Nome");

        assertThatThrownBy(() -> updateArtistaService.execute(UUID.randomUUID(), dto))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Artista não encontrado");
    }

    @Test
    @DisplayName("Deve manter os álbuns vinculados ao atualizar apenas o nome")
    void deveManterAlbunsAoAtualizarDadosBasicos() {
        ArtistaUpdateRequestDTO dto = new ArtistaUpdateRequestDTO();
        dto.setNome("Daft Punk (Updated)");

        updateArtistaService.execute(artistaUuid, dto);

        entityManager.flush();
        entityManager.clear();

        ArtistaJpaEntity artistaNoBanco = artistaRepository.findByArtistaId(artistaUuid).get();
        assertThat(artistaNoBanco.getAlbuns()).hasSize(1);
        assertThat(artistaNoBanco.getAlbuns().get(0).getTitulo()).isEqualTo("Discovery");
    }
}
