package br.com.zagonel.catalogo_musical_api.api.controller;

import br.com.zagonel.catalogo_musical_api.api.controller.capaAlbum.CapaAlbumController;
import br.com.zagonel.catalogo_musical_api.api.dto.request.capaAlbum.CapaAlbumRequest;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.UserRepository;
import br.com.zagonel.catalogo_musical_api.service.capaAlbum.DesvincularCapaAlbumService;
import br.com.zagonel.catalogo_musical_api.service.capaAlbum.VincularCapaAlbumService;
import br.com.zagonel.catalogo_musical_api.service.seguranca.jwt.JwtService;
import br.com.zagonel.catalogo_musical_api.service.seguranca.usuario.CustomUserDetailsService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CapaAlbumController.class)
class CapaAlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VincularCapaAlbumService vincularCapaAlbumService;

    @MockitoBean
    private DesvincularCapaAlbumService desvincularCapaAlbumService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("POST /albuns/{id}/capas - Deve retornar 201 ao fazer upload de capa")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar201AoVincularCapa() throws Exception {
        var albumId = UUID.randomUUID();
        var file = new MockMultipartFile("files", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "imagem".getBytes());

        ArgumentCaptor<CapaAlbumRequest> requestCaptor = ArgumentCaptor.forClass(CapaAlbumRequest.class);

        mockMvc.perform(multipart("/albuns/{albumId}/capas", albumId)
                        .file(file)
                        .param("descricao", "Capa Frontal")
                        .with(csrf()))
                .andExpect(status().isCreated());

        verify(vincularCapaAlbumService).execute(eq(albumId), any(), requestCaptor.capture());

        CapaAlbumRequest capturedRequest = requestCaptor.getValue();
        assertEquals("Capa Frontal", capturedRequest.descricao());
    }

    @Test
    @DisplayName("DELETE /albuns/{id}/capas - Deve retornar 204 ao remover capa")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar204AoDesvincularCapa() throws Exception {
        var albumId = UUID.randomUUID();
        var path = "albums/foto.jpg";

        doNothing().when(desvincularCapaAlbumService).execute(albumId, path);

        mockMvc.perform(delete("/albuns/{albumId}/capas", albumId)
                        .param("path", path)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(desvincularCapaAlbumService).execute(albumId, path);
    }
}
