package br.com.zagonel.catalogo_musical_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@EnableScheduling
public class CatalogoMusicalApiApplication {

    static void main(String[] args) {
        SpringApplication.run(CatalogoMusicalApiApplication.class, args);
    }

}
