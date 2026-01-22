package br.com.zagonel.catalogo_musical_api.infrastructure.persistence;

import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "artista")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistaJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(nullable = false)
    private Long id;

    @Column(name = "artista_id", nullable = false, unique = true)
    private UUID artistaId;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", nullable = false, length = 20)
    private TipoArtista tipo;

    @ManyToMany(mappedBy = "artistas")
    private List<AlbumJpaEntity> albuns = new ArrayList<>();
}
