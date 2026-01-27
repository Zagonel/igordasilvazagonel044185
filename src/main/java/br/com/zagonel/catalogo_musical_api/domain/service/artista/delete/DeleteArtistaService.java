package br.com.zagonel.catalogo_musical_api.domain.service.artista.delete;

import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.ArtistaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DeleteArtistaService {

    private final ArtistaRepository artistaRepository;

    @Transactional
    public void execute(UUID artistaId) {
        ArtistaJpaEntity artista = artistaRepository.findByArtistaId(artistaId)
                .orElseThrow(() -> new DomainException("Artista não encontrado com o ID: " + artistaId));

        artistaRepository.delete(artista);
    }
}
