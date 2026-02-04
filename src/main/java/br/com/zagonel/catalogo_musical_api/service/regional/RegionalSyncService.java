package br.com.zagonel.catalogo_musical_api.service.regional;

import br.com.zagonel.catalogo_musical_api.api.dto.request.regional.RegionalExternalDTO;
import br.com.zagonel.catalogo_musical_api.domain.exceptions.DomainException;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.RegionalJpaEntity;
import br.com.zagonel.catalogo_musical_api.infrastructure.repository.RegionalRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegionalSyncService {

    private final RegionalRepository regionalRepository;
    private final RestClient restClient = RestClient.create();

    private static final String URL_REGIONAIS = "https://integrador-argus-api.geia.vip/v1/regionais";

    @Transactional
    public void processarSincronizacao() {

        log.info("Iniciando sincronização de regionais...");

        //TODO: Debuggar aqui para ver por que ta duplicando a inserção e também olhar o websocket para ver se ta tudo ok.

        List<RegionalExternalDTO> listaExterna = buscarRegionaisExternas();

        Map<Integer, RegionalJpaEntity> regionaisAtivasMap = regionalRepository.findByAtivoTrue()
                .stream()
                .collect(Collectors.toMap(RegionalJpaEntity::getCodigo, Function.identity()));

        List<RegionalJpaEntity> entidadesParaSalvar = new ArrayList<>();

        for (RegionalExternalDTO dto : listaExterna) {
            RegionalJpaEntity entidadeBanco = regionaisAtivasMap.get(dto.codigo());

            if (entidadeBanco == null) {
                entidadesParaSalvar.add(criarNovaEntidade(dto));
            } else {
                if (!entidadeBanco.getNome().equals(dto.nome())) {
                    entidadeBanco.setAtivo(false);
                    entidadesParaSalvar.add(entidadeBanco);
                    entidadesParaSalvar.add(criarNovaEntidade(dto));
                }
                regionaisAtivasMap.remove(dto.codigo());
            }
        }

        for (RegionalJpaEntity entidadeSobrou : regionaisAtivasMap.values()) {
            entidadeSobrou.setAtivo(false);
            entidadesParaSalvar.add(entidadeSobrou);
            log.info("Regional inativada por ausência na API: {}", entidadeSobrou.getNome());
        }

        if (!entidadesParaSalvar.isEmpty()) {
            regionalRepository.saveAll(entidadesParaSalvar);
        }

        log.info("Sincronização finalizada. Registros processados: {}", entidadesParaSalvar.size());
    }

    private RegionalJpaEntity criarNovaEntidade(RegionalExternalDTO dto) {
        RegionalJpaEntity nova = new RegionalJpaEntity();
        nova.setCodigo(dto.codigo());
        nova.setNome(dto.nome());
        nova.setAtivo(true);
        return nova;
    }

    private List<RegionalExternalDTO> buscarRegionaisExternas() {
        try {
            return restClient.get()
                    .uri(URL_REGIONAIS)
                    .retrieve()
                    .body(new ParameterizedTypeReference<>() {
                    });
        } catch (Exception e) {
            log.error("Erro ao buscar regionais externas", e);
            throw new DomainException("Falha na integração com API de Regionais");
        }
    }
}
