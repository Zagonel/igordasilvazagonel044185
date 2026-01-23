package br.com.zagonel.catalogo_musical_api.service.album.create;

import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.domain.model.Album;
import br.com.zagonel.catalogo_musical_api.domain.model.Artista;
import br.com.zagonel.catalogo_musical_api.domain.service.album.create.CreateAlbumService;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.AlbumMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.mappers.ArtistaMapper;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.AlbumJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.ArtistaJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.AlbumRepository;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.ArtistaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class CreateAlbumServiceTest {

    @MockitoBean
    private AlbumRepository albumRepository;
    @MockitoBean
    private AlbumMapper albumMapper;
    @MockitoBean
    private ArtistaMapper artistaMapper;
    @MockitoBean
    private ArtistaRepository artistaRepository;

    @Autowired
    private CreateAlbumService createAlbumService;

    private AlbumCreateRequestDTO requestDTO;
    private UUID artistaUuid;

    @BeforeEach
    void setUp() {
        artistaUuid = UUID.randomUUID();
        requestDTO = new AlbumCreateRequestDTO();
        requestDTO.setTitulo("Thriller");
        requestDTO.setDataLancamento(LocalDate.of(1982, 11, 30));
        requestDTO.setArtistasIds(List.of(artistaUuid.toString()));
    }

    @Test
    @DisplayName("Deve criar um álbum com sucesso vinculado a um artista")
    void deveCriarAlbumComSucesso() {

        ArtistaJpaEntity artistaJpa = mock(ArtistaJpaEntity.class);
        Artista artistaDomain = mock(Artista.class);
        AlbumJpaEntity albumJpa = new AlbumJpaEntity();
        Album albumDomain = Album.criarNovoAlbum(requestDTO.getTitulo(), requestDTO.getDataLancamento());

        when(artistaRepository.findByArtistaId(artistaUuid)).thenReturn(Optional.of(artistaJpa));
        when(artistaMapper.toDomain(artistaJpa)).thenReturn(artistaDomain);
        when(artistaDomain.getId()).thenReturn(artistaUuid);

        when(albumMapper.toEntity(any(Album.class))).thenReturn(albumJpa);
        when(albumRepository.save(any(AlbumJpaEntity.class))).thenReturn(albumJpa);
        when(albumMapper.toDomain(albumJpa)).thenReturn(albumDomain);
        when(albumMapper.toResponse(any(Album.class))).thenReturn(new AlbumResponseDTO());

        // WHEN
        AlbumResponseDTO result = createAlbumService.execute(requestDTO);

        // THEN
        assertThat(result).isNotNull();
        verify(artistaRepository).findByArtistaId(artistaUuid);
        verify(albumRepository).save(any(AlbumJpaEntity.class));
    }

    @Test
    @DisplayName("Deve lançar DomainException quando o artista não for encontrado")
    void deveFalharQuandoArtistaNaoExistir() {
        // GIVEN
        when(artistaRepository.findByArtistaId(any(UUID.class))).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThatThrownBy(() -> createAlbumService.execute(requestDTO))
                .isInstanceOf(DomainException.class)
                .hasMessageContaining("Não foi possivel encontrar o artista");

        verify(albumRepository, never()).save(any());
    }
}
