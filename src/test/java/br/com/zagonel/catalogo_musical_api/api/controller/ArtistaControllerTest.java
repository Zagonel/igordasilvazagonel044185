package br.com.zagonel.catalogo_musical_api.api.controller;

import br.com.zagonel.catalogo_musical_api.api.controller.artista.ArtistaController;
import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaCreateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaSearchQuery;
import br.com.zagonel.catalogo_musical_api.api.dto.request.artista.ArtistaUpdateRequestDTO;
import br.com.zagonel.catalogo_musical_api.api.dto.response.ArtistaResponseDTO;
import br.com.zagonel.catalogo_musical_api.domain.enums.TipoArtista;
import br.com.zagonel.catalogo_musical_api.domain.service.artista.create.CreateArtistaService;
import br.com.zagonel.catalogo_musical_api.domain.service.artista.delete.DeleteArtistaService;
import br.com.zagonel.catalogo_musical_api.domain.service.artista.retrive.get.GetArtistaService;
import br.com.zagonel.catalogo_musical_api.domain.service.artista.retrive.list.ListArtistaService;
import br.com.zagonel.catalogo_musical_api.domain.service.artista.update.UpdateArtistaService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArtistaController.class)
class ArtistaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateArtistaService createArtistaService;

    @MockitoBean
    private GetArtistaService getArtistaService;

    @MockitoBean
    private ListArtistaService listArtistaService;

    @MockitoBean
    private UpdateArtistaService updateArtistaService;

    @MockitoBean
    private DeleteArtistaService deleteArtistaService;

    @Test
    @DisplayName("POST /artistas - Deve retornar 201 ao criar artista")
    void deveRetornar201AoCriar() throws Exception {
        var input = new ArtistaCreateRequestDTO();
        input.setNome("Michael Jackson");
        input.setTipo(TipoArtista.CANTOR);

        var response = new ArtistaResponseDTO();
        response.setNome("Michael Jackson");

        when(createArtistaService.execute(any())).thenReturn(response);

        mockMvc.perform(post("/artistas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome").value("Michael Jackson"));
    }

    @Test
    @DisplayName("GET /artistas - Deve retornar 200 ao listar")
    void deveRetornar200AoListar() throws Exception {
        var response = new ArtistaResponseDTO();
        response.setNome("Daft Punk");

        when(listArtistaService.execute(any(ArtistaSearchQuery.class)))
                .thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/artistas")
                        .param("nome", "Daft Punk")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].nome").value("Daft Punk"));
    }

    @Test
    @DisplayName("PUT /artistas/{id} - Deve retornar 200 ao atualizar")
    void deveRetornar200AoAtualizar() throws Exception {
        var id = UUID.randomUUID();
        var input = new ArtistaUpdateRequestDTO();
        input.setNome("Novo Nome");

        var response = new ArtistaResponseDTO();
        response.setNome("Novo Nome");

        when(updateArtistaService.execute(eq(id), any())).thenReturn(response);

        mockMvc.perform(put("/artistas/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("Novo Nome"));
    }
}
