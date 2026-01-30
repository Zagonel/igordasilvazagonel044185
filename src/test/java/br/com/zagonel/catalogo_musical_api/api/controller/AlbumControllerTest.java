package br.com.zagonel.catalogo_musical_api.api.controller;

import br.com.zagonel.catalogo_musical_api.api.controller.album.AlbumController;
import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.album.AlbumSearchQuery;
import br.com.zagonel.catalogo_musical_api.api.dto.response.AlbumResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.service.album.create.CreateAlbumService;
import br.com.zagonel.catalogo_musical_api.domain.service.album.delete.DeleteAlbumService;
import br.com.zagonel.catalogo_musical_api.domain.service.album.retrive.get.GetAlbumService;
import br.com.zagonel.catalogo_musical_api.domain.service.album.retrive.list.ListAlbumService;
import br.com.zagonel.catalogo_musical_api.domain.service.album.update.UpdateAlbumService;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.jwt.JwtService;
import br.com.zagonel.catalogo_musical_api.domain.service.seguranca.usuario.CustomUserDetailsService;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AlbumController.class)
class AlbumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateAlbumService createAlbumService;

    @MockitoBean
    private GetAlbumService getAlbumService;

    @MockitoBean
    private ListAlbumService listAlbumService;

    @MockitoBean
    private UpdateAlbumService updateAlbumService;

    @MockitoBean
    private DeleteAlbumService deleteAlbumService;

    @MockitoBean
    private JwtService jwtService;

    @MockitoBean
    private UserRepository userRepository;

    @MockitoBean
    private CustomUserDetailsService customUserDetailsService;

    @Test
    @DisplayName("POST /albuns - Deve retornar 201 ao criar álbum")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar201AoCriar() throws Exception {
        var input = new AlbumCreateRequestDTO();
        input.setTitulo("Thriller");
        input.setDataLancamento(LocalDate.of(1982, 11, 30));

        var response = new AlbumResponseDTO();
        response.setTitulo("Thriller");

        when(createAlbumService.execute(any())).thenReturn(response);

        mockMvc.perform(post("/albuns")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.titulo").value("Thriller"));
    }

    @Test
    @DisplayName("GET /albuns - Deve retornar 200 ao listar")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar200AoListar() throws Exception {
        var response = new AlbumResponseDTO();
        response.setTitulo("Discovery");

        when(listAlbumService.execute(any(AlbumSearchQuery.class)))
                .thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/albuns")
                        .with(csrf())
                        .param("titulo", "Discovery")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].titulo").value("Discovery"));
    }

    @Test
    @DisplayName("GET /albuns/{id} - Deve retornar 200 ao buscar por ID")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar200AoBuscarPorId() throws Exception {
        var id = UUID.randomUUID();
        var response = new AlbumResponseDTO();
        response.setId(id);

        when(getAlbumService.execute(id)).thenReturn(response);

        mockMvc.perform(get("/albuns/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    @DisplayName("DELETE /albuns/{id} - Deve retornar 204 ao excluir")
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deveRetornar204AoExcluir() throws Exception {
        var id = UUID.randomUUID();

        doNothing().when(deleteAlbumService).execute(id);

        mockMvc.perform(delete("/albuns/{id}", id)
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(deleteAlbumService).execute(id);
    }
}
