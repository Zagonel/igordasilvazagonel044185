package br.com.zagonel.catalogo_musical_api.api.dto.request.regional;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegionalExternalDTO(
        @JsonProperty("id") Integer codigo,
        @JsonProperty("nome") String nome
) {
}
