package br.com.zagonel.catalogo_musical_api.domain;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.domain.model.Artista;
import br.com.zagonel.catalogo_musical_api.domain.model.CapaAlbum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
    @DisplayName("Deve alterar a Data de lançamento com sucesso")
    void deveAlterarDataLancamento() {

        Album album = Album.criarNovoAlbum("Original", LocalDate.now());

        LocalDate novaData = LocalDate.now().minusDays(2);
        Album albumAlterado = album.alterarDataLancamento(novaData);

        assertThat(albumAlterado.getDataLancamento()).isEqualTo(novaData);
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
    @DisplayName("Deve Falhar ao vincular")
    void deveFalharAoTentarVincularArtista() {
        Album album = Album.criarNovoAlbum("Random Access Memories", LocalDate.now());

        assertThatThrownBy(() -> album.vincularArtista(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Artista não pode ser null.");
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
    @DisplayName("Deve desvincular um artista do album com sucesso")
    void deveDesvincularArtista() {
        Album album = Album.criarNovoAlbum("Random Access Memories", LocalDate.now());

        Artista artista = Artista.criarNovoArtista("Daft Punk", TipoArtista.BANDA);

        album.vincularArtista(artista);

        album.desvincularArtista(artista.getId());

        assertThat(album.getArtistas()).hasSize(0);
        assertThat(album.possuiArtista()).isFalse();
    }

    @Test
    @DisplayName("Deve falhar ao tentar desvincular com ID null")
    void deveFalharDesvincularArtista() {
        Album album = Album.criarNovoAlbum("Random Access Memories", LocalDate.now());

        assertThatThrownBy(() -> album.desvincularArtista(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("O identificador do artista é obrigatório para a exclusão.");
    }

    @Test
    @DisplayName("Deve falhar ao tentar desvincular um artista inexistente de um album")
    void deveFalharAoDesvincularUmArtistaInexistente() {
        Album album = Album.criarNovoAlbum("Random Access Memories", LocalDate.now());

        assertThatThrownBy(() -> album.desvincularArtista(UUID.randomUUID()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("O Artista informado não foi encontrado na lista de artistas vinculadas ao album.");
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

    @Test
    @DisplayName("Deve falhar ao adicionar capa do album por ser nula")
    void deveFalharAoAdicionarCapaNula() {
        Album album = Album.criarNovoAlbum("AlbumTeste", LocalDate.now());

        assertThatThrownBy(() -> album.adicionarCapa(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Dados da capa são obrigatórios.");

    }

    @Test
    @DisplayName("Deve ter sucesso ao remover capa do album")
    void deveRemoverCapaDoAlbum() {
        Album album = Album.criarNovoAlbum("AlbumTeste", LocalDate.now());

        CapaAlbum capa1 = CapaAlbum.criarCapaAlbum("path/1.jpg", "Frente", true);

        album.adicionarCapa(capa1);

        assertEquals(capa1.getPath(), album.getCapas().getFirst().getPath());
        assertEquals(capa1.getDescricao(), album.getCapas().getFirst().getDescricao());
        assertEquals(capa1.isPrincipal(), album.getCapas().getFirst().isPrincipal());

        album.removerCapa("path/1.jpg");

        assertThat(album.getCapas()).hasSize(0);
    }

    @Test
    @DisplayName("Deve ter falhar ao remover capa do album inexistente")
    void deveFalharAoRemoverCapaInexistenteDoAlbum() {
        Album album = Album.criarNovoAlbum("AlbumTeste", LocalDate.now());

        assertThatThrownBy(() -> album.removerCapa("INVALIDO"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Capa não encontrada para o caminho informado.");

        assertThatThrownBy(() -> album.removerCapa(""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("O caminho da capa é obrigatório para a remoção.");
    }

    @Test
    @DisplayName("Deve ter falhar ao remover capa do album com path invalido")
    void deveFalharAoRemoverCapaComPathBlankDoAlbum() {
        Album album = Album.criarNovoAlbum("AlbumTeste", LocalDate.now());

        assertThatThrownBy(() -> album.removerCapa(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("O caminho da capa é obrigatório para a remoção.");

    }

}
