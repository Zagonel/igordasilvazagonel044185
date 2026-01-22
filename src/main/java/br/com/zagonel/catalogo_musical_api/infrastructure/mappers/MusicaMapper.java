package br.com.zagonel.catalogo_musical_api.infrastructure.mappers;

import br.com.zagonel.catalogo_musical_api.domain.model.Musica;
import br.com.zagonel.catalogo_musical_api.infrastructure.persistence.embeddables.MusicaEmbeddable;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.time.Duration;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface MusicaMapper {

    @Mapping(target = "duracao", source = "duracaoSegundos", qualifiedByName = "longToDuration")
    Musica toDomain(MusicaEmbeddable entity);

    @Mapping(target = "duracaoSegundos", source = "duracao", qualifiedByName = "durationToLong")
    MusicaEmbeddable toEntity(Musica domain);

    @Named("longToDuration")
    default Duration longToDuration(Long seconds) {
        return seconds != null ? Duration.ofSeconds(seconds) : null;
    }

    @Named("durationToLong")
    default Long durationToLong(Duration duration) {
        return duration != null ? duration.getSeconds() : null;
    }
}
