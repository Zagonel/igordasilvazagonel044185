package br.com.zagonel.catalogo_musical_api.infrastructure.persistence;

import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.embeddables.CapaAlbumEmbeddable;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "album")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(name = "album_id", nullable = false, unique = true)
    private UUID albumId;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "data_lancamento")
    private LocalDate dataLancamento;

    @ManyToMany
    @JoinTable(
            name = "artista_album",
            joinColumns = @JoinColumn(name = "album_id"),
            inverseJoinColumns = @JoinColumn(name = "artista_id")
    )
    private List<ArtistaJpaEntity> artistas = new ArrayList<>();

    @ElementCollection
    @CollectionTable(name = "album_capa", joinColumns = @JoinColumn(name = "album_id"))
    private List<CapaAlbumEmbeddable> capas = new ArrayList<>();

}
