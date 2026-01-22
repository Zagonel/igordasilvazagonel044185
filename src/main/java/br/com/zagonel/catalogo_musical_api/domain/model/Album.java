package br.com.zagonel.catalogo_musical_api.domain.model;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Getter
public class Album {

    private final UUID id;
    private final String titulo;
    private final LocalDate dataLancamento;
    private List<CapaAlbum> capas;
    private final List<Artista> artistas;
    private final List<Musica> musicas;

    public Album(UUID id, String titulo, LocalDate dataLancamento, List<CapaAlbum> capas, List<Artista> artistas, List<Musica> musicas) {
        this.id = id;
        this.titulo = titulo;
        this.dataLancamento = dataLancamento;
        this.capas = capas;
        this.artistas = artistas;
        this.musicas = musicas;
    }

    public static Album criarNovoAlbum(String titulo, LocalDate dataLancamento) {
        validarTitulo(titulo);
        validarDataLancamento(dataLancamento);
        return new Album(
                UUID.randomUUID(),
                titulo,
                dataLancamento,
                new ArrayList<>(),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public Album alterarTitulo(String novoTitulo) {
        validarTitulo(novoTitulo);
        return new Album(this.id, novoTitulo, this.dataLancamento, this.capas, this.artistas, this.musicas);
    }

    public Album alterarDataLancamento(LocalDate novaData) {
        validarDataLancamento(novaData);
        return new Album(this.id, this.titulo, novaData, this.capas, this.artistas, this.musicas);
    }

    public void adicionarMusica(Musica musica) {
        if (musica == null) {
            throw new DomainException("A música não pode ser nula.");
        }
        this.musicas.add(musica);
    }

    public void vincularArtista(Artista artista) {
        if (artista == null) {
            throw new DomainException("Artista não pode ser null.");
        }

        boolean jaVinculado = this.artistas.stream().anyMatch(a -> a.getId().equals(artista.getId()));
        if (!jaVinculado) {
            this.artistas.add(artista);
        } else {
            throw new DomainException("Esse artista já está vinculado a esse Album.");
        }
    }

    public void adicionarCapa(CapaAlbum novaCapa) {
        if (novaCapa == null) {
            throw new DomainException("Dados da capa são obrigatórios.");
        }

        if (novaCapa.isPrincipal()) {
            this.capas.forEach(c -> c.alterarStatusPrincipal(false));
        }

        this.capas.add(novaCapa);
    }

    private static void validarTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty() || titulo.length() > 200) {
            throw new DomainException("O título do álbum deve ter entre 1 e 200 caracteres.");
        }
    }

    private static void validarDataLancamento(LocalDate data) {
        if (data != null && data.isAfter(LocalDate.now())) {
            throw new DomainException("A data de lançamento não pode ser uma data futura.");
        }
    }

    public boolean possuiArtista() {
        return !artistas.isEmpty();
    }

    public Optional<CapaAlbum> getCapaPrincipal() {
        return capas.stream().filter(CapaAlbum::isPrincipal).findFirst();
    }

}
