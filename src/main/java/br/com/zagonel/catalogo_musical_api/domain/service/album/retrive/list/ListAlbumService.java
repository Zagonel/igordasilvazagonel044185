package br.com.zagonel.catalogo_musical_api.domain.service.album.retrive.list;

import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumSearchQuery;
import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.specifications.AlbumSpecifications;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListAlbumService {

    private final AlbumRepository albumRepository;
    private final AlbumMapper albumMapper;

    @Transactional(readOnly = true)
    public Page<AlbumResponseDTO> execute(AlbumSearchQuery query) {

        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), query.sort());

        PageRequest pageRequest = PageRequest.of(query.page(), query.perPage(), sort);

        Specification<AlbumJpaEntity> spec = Specification.where(AlbumSpecifications.comTitulo(query.titulo()))
                .and(AlbumSpecifications.comDataIntervalo(query.dataInicio(), query.dataFim()))
                .and(AlbumSpecifications.comNomeArtista(query.nomeArtista()));

        if (query.tipoArtista() != null) {
            spec = spec.and(AlbumSpecifications.comTipoArtista(TipoArtista.fromCodigo(query.tipoArtista())));
        }

        return albumRepository.findAll(spec, pageRequest)
                .map(entity -> albumMapper.toResponse(albumMapper.toDomain(entity)));
    }
}
