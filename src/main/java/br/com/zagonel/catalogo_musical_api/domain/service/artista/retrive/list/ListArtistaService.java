package br.com.zagonel.catalogo_musical_api.domain.service.artista.retrive.list;

import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaSearchQuery;
import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.ArtistaMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.specifications.ArtistaSpecifications;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ListArtistaService {

    private final ArtistaRepository artistaRepository;
    private final ArtistaMapper artistaMapper;

    @Transactional(readOnly = true)
    public Page<ArtistaResponseDTO> execute(ArtistaSearchQuery query) {

        Sort sort = Sort.by(Sort.Direction.fromString(query.direction()), query.sort());
        PageRequest pageRequest = PageRequest.of(query.page(), query.perPage(), sort);

        Specification<ArtistaJpaEntity> spec = Specification.where(ArtistaSpecifications.comNome(query.nome()))
                .and(ArtistaSpecifications.comNomeAlbum(query.nomeAlbum()));

        if (query.tipo() != null) {
            spec = spec.and(ArtistaSpecifications.comTipo(TipoArtista.fromCodigo(query.tipo())));
        }

        return artistaRepository.findAll(spec, pageRequest)
                .map(entity -> artistaMapper.toResponse(artistaMapper.toDomain(entity)));
    }
}
