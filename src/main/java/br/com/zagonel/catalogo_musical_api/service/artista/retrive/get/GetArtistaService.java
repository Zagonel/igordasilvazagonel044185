package br.com.zagonel.catalogo_musical_api.service.artista.retrive.get;

import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Artista;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.ArtistaMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GetArtistaService {

    private final ArtistaRepository artistaRepository;
    private final ArtistaMapper artistaMapper;

    @Transactional(readOnly = true)
    public ArtistaResponseDTO execute(UUID artistaId) {
        ArtistaJpaEntity entity = artistaRepository.findByArtistaId(artistaId)
                .orElseThrow(() -> new DomainException("Artista não encontrado com o ID: " + artistaId));

        Artista domain = artistaMapper.toDomain(entity);

        return artistaMapper.toResponse(domain);
    }
}
