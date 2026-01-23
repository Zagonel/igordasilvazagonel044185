package br.com.zagonel.catalogo_musical_api.domain;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.domain.model.Artista;
import br.com.zagonel.catalogo_musical_api.domain.model.CapaAlbum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domínio: Álbum")
class AlbumTest {

    @Test
    @DisplayName("Deve criar um álbum válido")
    void deveCriarAlbumValido() {
        Album album = Album.criarNovoAlbum("Discovery", LocalDate.of(2001, 3, 12));

        assertThat(album.getId()).isNotNull();
        assertThat(album.getTitulo()).isEqualTo("Discovery");
        assertThat(album.getDataLancamento()).isEqualTo(LocalDate.of(2001, 3, 12));
        assertThat(album.getArtistas()).isEmpty();
    }

    @Test
    @DisplayName("Deve falhar ao criar álbum com título inválido")
    void deveFalharTituloInvalido() {
        assertThatThrownBy(() -> Album.criarNovoAlbum("", LocalDate.now()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("título");
    }

    @Test
    @DisplayName("Deve falhar ao criar álbum com data futura")
    void deveFalharDataFutura() {
        LocalDate dataFutura = LocalDate.now().plusDays(1);
        assertThatThrownBy(() -> Album.criarNovoAlbum("Future Sound", dataFutura))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("data futura");
    }

    @Test
    @DisplayName("Deve alterar título com sucesso")
    void deveAlterarTitulo() {
        Album album = Album.criarNovoAlbum("Original", LocalDate.now());
        Album albumAlterado = album.alterarTitulo("Novo Titulo");

        assertThat(albumAlterado.getTitulo()).isEqualTo("Novo Titulo");
        assertThat(albumAlterado.getId()).isEqualTo(album.getId());
    }

    @Test
    @DisplayName("Deve adicionar e vincular artista com sucesso")
    void deveVincularArtista() {
        Album album = Album.criarNovoAlbum("Random Access Memories", LocalDate.now());

        Artista artista = Artista.criarNovoArtista("Daft Punk", TipoArtista.BANDA);

        album.vincularArtista(artista);

        assertThat(album.getArtistas()).hasSize(1);
        assertThat(album.possuiArtista()).isTrue();
    }

    @Test
    @DisplayName("Deve falhar ao vincular o mesmo artista duas vezes")
    void deveFalharVinculoDuplicado() {
        Album album = Album.criarNovoAlbum("Album Teste", LocalDate.now());
        Artista artista = Artista.criarNovoArtista("Artista X", TipoArtista.CANTOR);

        album.vincularArtista(artista);

        assertThatThrownBy(() -> album.vincularArtista(artista))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Esse artista já está vinculado a esse Album.");
    }

    @Test
    @DisplayName("Deve gerenciar a troca de capa principal corretamente")
    void deveGerenciarCapaPrincipal() {
        Album album = Album.criarNovoAlbum("Capa Teste", LocalDate.now());

        CapaAlbum capa1 = CapaAlbum.criarCapaAlbum("path/1.jpg", "Frente", true);
        CapaAlbum capa2 = CapaAlbum.criarCapaAlbum("path/2.jpg", "Verso", true); // Nova principal

        album.adicionarCapa(capa1);
        assertThat(album.getCapaPrincipal()).isPresent().contains(capa1);

        album.adicionarCapa(capa2);

        assertThat(capa1.isPrincipal()).isFalse();
        assertThat(album.getCapaPrincipal()).isPresent().contains(capa2);
        assertThat(album.getCapas()).hasSize(2);
    }
}
