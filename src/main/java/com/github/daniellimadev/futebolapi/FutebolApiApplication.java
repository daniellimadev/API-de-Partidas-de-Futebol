package com.github.daniellimadev.futebolapi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Projeto Final - API de Partidas de Futebol",
        version = "1",
        description = "A aplicação consistirá em criar uma API de partidas de futebol, onde o usuário poderá manusear dados de clubes, de partidas e de estádios, bem como fazer cruzamento de dados dessas entidades."))
public class FutebolApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(FutebolApiApplication.class, args);
    }

}
