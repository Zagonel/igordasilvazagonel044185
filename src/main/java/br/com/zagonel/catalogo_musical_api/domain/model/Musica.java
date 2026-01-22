package br.com.zagonel.catalogo_musical_api.domain.model;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import lombok.Getter;

import java.time.Duration;

@Getter
public class Musica {

    public final String titulo;
    public final Duration duracao;
    public final Integer ordem;

    public Musica(String titulo, Duration duracao, Integer ordem) {
        this.titulo = titulo;
        this.duracao = duracao;
        this.ordem = ordem;
    }

    public static Musica criarNovaMusica(String titulo, Duration duracao, Integer ordem) {
        if (titulo == null || titulo.isBlank()) {
            throw new DomainException("O titulo da Musica é obrigatório.");
        }

        if (duracao == null || duracao.isNegative() || duracao.isZero()) {
            throw new DomainException("A duração da Musica é Obrigatória e não pode ser zero ou negativa");
        }

        return new Musica(titulo, duracao, ordem);
    }
}