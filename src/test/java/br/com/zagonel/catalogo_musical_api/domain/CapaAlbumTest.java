package br.com.zagonel.catalogo_musical_api.domain;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.CapaAlbum;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("Domínio: CapaAlbum")
public class CapaAlbumTest {

    @Test
    @DisplayName("Deve criar uma capa de album válida")
    void deveCriarCapaAlbumValida() {
        CapaAlbum capaAlbum = CapaAlbum.criarCapaAlbum("path/1.jpg", "Frente", true);

        assertThat(capaAlbum.getPath()).isNotNull();
        assertThat(capaAlbum.getDescricao()).isEqualTo("Frente");
        assertThat(capaAlbum.isPrincipal()).isEqualTo(true);
        assertThat(capaAlbum.getPath()).isEqualTo("path/1.jpg");
    }

    @Test
    @DisplayName("Deve falhar ao criar uma capa de album devido ao path invalido")
    void deveFalharCriarCapaAlbumComPathInvalido() {
        assertThatThrownBy(() -> CapaAlbum.criarCapaAlbum("", "Frente", true))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("O caminho (path) da imagem é obrigatório.");
    }

    @Test
    @DisplayName("Deve falhar ao criar uma capa de album devido a descricao invalida")
    void deveFalharCriarCapaAlbumComDescricaoInvalida() {
        assertThatThrownBy(() -> CapaAlbum.criarCapaAlbum("path/1.jpg", "", true))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("A descrição da imagem é obrigatório.");
    }
}
