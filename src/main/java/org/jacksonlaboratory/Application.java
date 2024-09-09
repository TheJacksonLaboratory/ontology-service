package org.jacksonlaboratory;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.*;
import io.swagger.v3.oas.annotations.info.*;

@OpenAPIDefinition(
    info = @Info(
            title = "ontology-service-${ontology}",
            description = "A restful service for the ${ontology} ontology.",
            version = "0.5.17",
            contact = @Contact(name = "Michael Gargano", email = "Michael.Gargano@jax.org")
    )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.build(args).eagerInitSingletons(true).mainClass(Application.class).start();
    }
}
