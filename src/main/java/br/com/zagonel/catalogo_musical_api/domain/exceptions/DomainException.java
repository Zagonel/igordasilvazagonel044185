package br.com.zagonel.catalogo_musical_api.domain.exceptions;

import lombok.Getter;

import java.util.Collections;
import java.util.List;

/**
 * Exceção base para representar erros de regra de negócio.
 */
@Getter
public class DomainException extends RuntimeException {

    private final List<String> errors;

    public DomainException(String message) {
        super(message);
        this.errors = Collections.singletonList(message);
    }

    public DomainException(String message, List<String> errors) {
        super(message);
        this.errors = errors != null ? errors : Collections.emptyList();
    }

    /**
     * Factory method para criar exceções a partir de uma lista de mensagens de erro.
     */
    public static DomainException fromList(List<String> errors) {
        String mainMessage = String.join(", ", errors);
        return new DomainException(mainMessage, errors);
    }
}
