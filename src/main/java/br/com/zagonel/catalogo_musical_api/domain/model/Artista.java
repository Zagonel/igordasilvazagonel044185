package br.com.zagonel.catalogo_musical_api.domain.model;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class Artista {
    private final UUID id;
    private final String nome;
    private final TipoArtista tipo;
    private final List<Album> albuns;

    public Artista(UUID id, String nome, TipoArtista tipo, List<Album> albuns) {
        this.id = id;
        this.nome = nome;
        this.tipo = tipo;
        this.albuns = albuns;
    }

    public static Artista criarNovoArtista(String nome, TipoArtista tipo) {
        List<Album> albuns = new ArrayList<>();

        validarNome(nome);
        if (tipo == null) {
            throw new DomainException("O tipo do artista é obrigatório e deve ser válido.");
        }

        return new Artista(UUID.randomUUID(), nome, tipo, albuns);
    }

    public Artista alterarNome(String novoNome) {
        validarNome(novoNome);
        return new Artista(this.getId(), novoNome, this.getTipo(), this.getAlbuns());
    }

    public Artista alterarTipoArtista(Integer novoTipoCodigo) {
        if (novoTipoCodigo == null) {
            throw new DomainException("O código do tipo do artista não pode ser nulo.");
        }
        try {
            TipoArtista novoTipo = TipoArtista.fromCodigo(novoTipoCodigo);
            return new Artista(this.id, this.nome, novoTipo, this.albuns);
        } catch (DomainException e) {
            throw new DomainException(e.getMessage());
        }
    }

    public void vincularAlbum(Album album) {
        if (album == null) {
            throw new DomainException("Não é possível adicionar um álbum nulo.");
        }

        boolean jaExiste = this.albuns.stream()
                .anyMatch(a -> a.getId().equals(album.getId()));

        if (!jaExiste) {
            this.albuns.add(album);
        } else {
            throw new DomainException("Esse Album já está Vinculado a esse Artista");
        }
    }

    public void desvincularAlbum(UUID albumUuid) {
        if (albumUuid == null) {
            throw new DomainException("O identificador do álbum é obrigatório para a exclusão.");
        }

        boolean removido = this.albuns.removeIf(album -> album.getId().equals(albumUuid));

        if (!removido) {
            throw new DomainException("O álbum informado não foi encontrado na lista deste artista.");
        }
    }

    private static void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new DomainException("Nome do artista não pode ser null ou vazio");
        }
        if (nome.trim().length() < 2 || nome.length() > 200) {
            throw new DomainException("Nome do artista deve ter entre 2 e 200 caracteres.");
        }
    }


}
