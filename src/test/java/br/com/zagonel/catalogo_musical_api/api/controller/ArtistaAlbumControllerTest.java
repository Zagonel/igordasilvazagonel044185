package br.com.zagonel.catalogo_musical_api.api.controller;

import br.com.zagonel.catalogo_musical_api.api.controller.artista_album.ArtistaAlbumController;
import br.com.zagonel.catalogo_musical_api.domain.service.artista_album.DesvincularArtistaAlbumService;
import br.com.zagonel.catalogo_musical_api.domain.service.artista_album.VincularArtistaAlbumService;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.jwt.JwtService;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.usuario.CustomUserDetailsService;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtistaAlbumController.class)
class ArtistaAlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VincularArtistaAlbumService vincularArtistaAlbumService;

    @MockitoBean
    private DesvincularArtistaAlbumService desvincularArtistaAlbumService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("PUT /albuns/{albumId}/artistas/{artistaId} - Deve retornar 204 ao vincular")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar204AoVincularArtista() throws Exception {
        var albumId = UUID.randomUUID();
        var artistaId = UUID.randomUUID();

        doNothing().when(vincularArtistaAlbumService).execute(albumId, artistaId);

        mockMvc.perform(put("/albuns/{albumId}/artistas/{artistaId}", albumId, artistaId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(vincularArtistaAlbumService).execute(albumId, artistaId);
    }

    @Test
    @DisplayName("DELETE /albuns/{albumId}/artistas/{artistaId} - Deve retornar 204 ao desvincular")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar204AoDesvincularArtista() throws Exception {
        var albumId = UUID.randomUUID();
        var artistaId = UUID.randomUUID();

        doNothing().when(desvincularArtistaAlbumService).execute(albumId, artistaId);

        mockMvc.perform(delete("/albuns/{albumId}/artistas/{artistaId}", albumId, artistaId)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(desvincularArtistaAlbumService).execute(albumId, artistaId);
    }
}
