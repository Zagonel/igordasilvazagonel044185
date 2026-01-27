package br.com.zagonel.catalogo_musical_api.domain;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.domain.model.Artista;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domínio: Artista")
public class ArtistaTest {

    @Test
    @DisplayName("Deve criar um artista válido")
    void deveCriarArtistaValido() {

        Artista artista = Artista.criarNovoArtista("Skillet", TipoArtista.BANDA);

        assertThat(artista.getId()).isNotNull();
        assertThat(artista.getNome()).isEqualTo("Skillet");
        assertThat(artista.getTipo()).isEqualTo(TipoArtista.BANDA);
        assertThat(artista.getAlbuns()).isEmpty();
    }

    @Test
    @DisplayName("Deve falhar ao criar um artista com tipo invalido")
    void deveFalharAoCriarArtistaComTipoInvalido() {

        assertThatThrownBy(() -> Artista.criarNovoArtista("Skillet", null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("O tipo do artista é obrigatório e deve ser válido.");
    }

    @Test
    @DisplayName("Deve alterar o nome do artista")
    void deveAlterarNomeArtista() {
        Artista artista = Artista.criarNovoArtista("Skillet", TipoArtista.BANDA);

        Artista artistaAlterado = artista.alterarNome("LinkPark");

        assertThat(artistaAlterado.getNome()).isEqualTo("LinkPark");
        assertThat(artistaAlterado.getId()).isEqualTo(artista.getId());
    }

    @Test
    @DisplayName("Deve falhar ao alterar o artista com nome invalido")
    void deveFalharAoAlterarComNomeInvalido() {
        Artista artista = Artista.criarNovoArtista("Skillet", TipoArtista.BANDA);

        assertThatThrownBy(() -> artista.alterarNome(""))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do artista não pode ser null ou vazio");

        assertThatThrownBy(() -> artista.alterarNome("1"))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Nome do artista deve ter entre 2 e 200 caracteres.");
    }

    @Test
    @DisplayName("Deve alterar o tipo do artista")
    void deveAlterarTipoArtista() {
        Artista artista = Artista.criarNovoArtista("Skillet", TipoArtista.BANDA);

        Artista artistaAlterado = artista.alterarTipoArtista(TipoArtista.CANTOR.getCodigo());

        assertThat(artistaAlterado.getNome()).isEqualTo("Skillet");
        assertThat(artistaAlterado.getTipo()).isEqualTo(TipoArtista.CANTOR);
        assertThat(artistaAlterado.getId()).isEqualTo(artista.getId());
    }

    @Test
    @DisplayName("Deve falhar ao alterar com tipo do artista invalido")
    void deveFalharAlterarTipoArtista() {
        Artista artista = Artista.criarNovoArtista("Skillet", TipoArtista.BANDA);

        assertThatThrownBy(() -> artista.alterarTipoArtista(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("O código do tipo do artista não pode ser nulo.");

        assertThatThrownBy(() -> artista.alterarTipoArtista(10))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("O código 10 não corresponde a um tipo de artista válido (1-Cantor, 2-Banda, 3-Dupla).");
    }

    @Test
    @DisplayName("Deve adicionar e vincular álbum com sucesso ao artista")
    void deveVincularAlbum() {
        Artista artista = Artista.criarNovoArtista("Daft Punk", TipoArtista.BANDA);
        Album album = Album.criarNovoAlbum("Random Access Memories", LocalDate.now());

        artista.vincularAlbum(album);

        assertThat(artista.getAlbuns()).hasSize(1);
        assertThat(artista.getAlbuns().contains(album)).isTrue();
    }

    @Test
    @DisplayName("Deve falhar ao vincular um álbum nulo ao artista")
    void deveFalharAoTentarVincularAlbumNulo() {
        Artista artista = Artista.criarNovoArtista("Daft Punk", TipoArtista.BANDA);

        assertThatThrownBy(() -> artista.vincularAlbum(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Não é possível adicionar um álbum nulo.");
    }

    @Test
    @DisplayName("Deve falhar ao vincular o mesmo álbum duas vezes ao mesmo artista")
    void deveFalharVinculoDuplicado() {
        Artista artista = Artista.criarNovoArtista("Artista X", TipoArtista.CANTOR);
        Album album = Album.criarNovoAlbum("Album Teste", LocalDate.now());

        artista.vincularAlbum(album);

        assertThatThrownBy(() -> artista.vincularAlbum(album))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Esse Album já está Vinculado a esse Artista");
    }

    @Test
    @DisplayName("Deve desvincular um álbum do artista com sucesso")
    void deveDesvincularAlbum() {
        Artista artista = Artista.criarNovoArtista("Daft Punk", TipoArtista.BANDA);
        Album album = Album.criarNovoAlbum("Random Access Memories", LocalDate.now());

        artista.vincularAlbum(album);
        artista.desvincularAlbum(album.getId());

        assertThat(artista.getAlbuns()).isEmpty();
    }

    @Test
    @DisplayName("Deve falhar ao tentar desvincular álbum com ID null")
    void deveFalharDesvincularAlbumComIdNull() {
        Artista artista = Artista.criarNovoArtista("Daft Punk", TipoArtista.BANDA);

        assertThatThrownBy(() -> artista.desvincularAlbum(null))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("O identificador do álbum é obrigatório para a exclusão.");
    }

    @Test
    @DisplayName("Deve falhar ao tentar desvincular um álbum que não está na lista do artista")
    void deveFalharAoDesvincularUmAlbumInexistente() {
        Artista artista = Artista.criarNovoArtista("Daft Punk", TipoArtista.BANDA);

        assertThatThrownBy(() -> artista.desvincularAlbum(UUID.randomUUID()))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("O álbum informado não foi encontrado na lista deste artista.");
    }


}
