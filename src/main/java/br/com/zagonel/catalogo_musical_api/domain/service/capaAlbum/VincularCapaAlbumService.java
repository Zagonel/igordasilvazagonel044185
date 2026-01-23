package br.com.zagonel.catalogo_musical_api.domain.service.capaAlbum;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.domain.model.CapaAlbum;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VincularCapaAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Transactional
    public void execute(UUID albumUuid, String path, String descricao, boolean principal) {

        //Aqui vai o MinIo, vou receber a multifilePart, criar um ID e caminho de pasta montar o CapaAlbum e então adicionar ao Album.

        AlbumJpaEntity albumEntity = albumRepository.findByAlbumId(albumUuid)
                .orElseThrow(() -> new DomainException("Álbum não encontrado"));

        Album albumDomain = albumMapper.toDomain(albumEntity);

        CapaAlbum novaCapa = CapaAlbum.criarCapaAlbum(path, descricao, principal);
        albumDomain.adicionarCapa(novaCapa);

        albumMapper.updateEntityFromDomain(albumDomain, albumEntity);

        albumRepository.save(albumEntity);
    }
}
