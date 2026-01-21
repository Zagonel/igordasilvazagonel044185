package br.com.zagonel.catalogo_musical_api.infrastructure.persistence.embeddables;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MusicaEmbeddable {

    @Column(nullable = false, length = 200)
    private String titulo;

    @Column(nullable = false)
    private Long duracaoSegundos; // Armazenamos o valor bruto para facilitar o mapeamento

    @Column(nullable = false)
    private Integer ordem;

}
