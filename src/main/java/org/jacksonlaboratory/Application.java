package org.jacksonlaboratory;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

import java.sql.SQLException;

@OpenAPIDefinition(
    info = @Info(
            title = "ontology-service-${ontology}",
            description = "A restful service for the ${ontology} ontology.",
            contact = @Contact(name = "Michael Gargano", email = "Michael.Gargano@jax.org")
    )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.build(args).eagerInitSingletons(true).mainClass(Application.class).start();
    }
}
