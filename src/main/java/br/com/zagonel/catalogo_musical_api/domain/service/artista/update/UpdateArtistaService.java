package br.com.zagonel.catalogo_musical_api.domain.service.artista.update;

import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaUpdateRequestDTO;
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
public class UpdateArtistaService {

    private final ArtistaRepository artistaRepository;
    private final ArtistaMapper artistaMapper;

    @Transactional
    public ArtistaResponseDTO execute(UUID artistaUuid, ArtistaUpdateRequestDTO dto) {
        ArtistaJpaEntity artistaEntity = artistaRepository.findByArtistaId(artistaUuid)
                .orElseThrow(() -> new DomainException("Artista não encontrado com o ID: " + artistaUuid));

        Artista artistaDomain = artistaMapper.toDomain(artistaEntity);

        if (dto.getNome() != null) {
            artistaDomain = artistaDomain.alterarNome(dto.getNome());
        }

        if (dto.getTipo() != null) {
            artistaDomain = artistaDomain.alterarTipoArtista(dto.getTipo().getCodigo());
        }

        artistaMapper.updateEntityFromDomain(artistaDomain, artistaEntity);

        artistaRepository.save(artistaEntity);

        return artistaMapper.toResponse(artistaDomain);
    }
}
