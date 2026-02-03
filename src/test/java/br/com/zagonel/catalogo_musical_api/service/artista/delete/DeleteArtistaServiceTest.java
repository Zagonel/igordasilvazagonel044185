package br.com.zagonel.catalogo_musical_api.service.artista.delete;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
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
@Import(DeleteArtistaService.class)
class DeleteArtistaServiceTest {

    @Autowired
    private ArtistaRepository artistaRepository;

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private DeleteArtistaService deleteArtistaService;

    @Autowired
    private TestEntityManager entityManager;

    private UUID artistaUuid;
    private UUID albumUuid;

    @BeforeEach
    void setUp() {
        artistaUuid = UUID.randomUUID();
        ArtistaJpaEntity artista = new ArtistaJpaEntity();
        artista.setArtistaId(artistaUuid);
        artista.setNome("Arctic Monkeys");
        artista.setTipo(TipoArtista.BANDA);
        artistaRepository.save(artista);

        albumUuid = UUID.randomUUID();
        AlbumJpaEntity album = new AlbumJpaEntity();
        album.setAlbumId(albumUuid);
        album.setTitulo("AM");
        album.setDataLancamento(LocalDate.of(2013, 9, 9));
        album.setArtistas(new ArrayList<>(List.of(artista)));
        albumRepository.save(album);

        entityManager.flush();
        entityManager.clear();
    }

    @Test
    @DisplayName("Deve deletar um artista com sucesso e remover vínculos")
    void deveDeletarArtistaComSucesso() {
        deleteArtistaService.execute(artistaUuid);

        entityManager.flush();
        entityManager.clear();

        assertThat(artistaRepository.findByArtistaId(artistaUuid)).isEmpty();

        var albumNoBanco = albumRepository.findByAlbumId(albumUuid).get();
        assertThat(albumNoBanco.getArtistas()).isEmpty();
    }

    @Test
    @DisplayName("Deve lançar exceção ao tentar deletar artista inexistente")
    void deveFalharAoDeletarInexistente() {
        UUID idInexistente = UUID.randomUUID();

        assertThatThrownBy(() -> deleteArtistaService.execute(idInexistente))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Artista não encontrado");
    }
}
