package org.jacksonlaboratory.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.PathVariable;
import org.monarchinitiative.phenol.ontology.data.TermId;

@Controller("${api-prefix.hpo}/gene")
public class GeneController {

    @Get(uri="/{id}", produces="application/json")
    public String details(@PathVariable TermId id) {
        return "Example Response";
    }
}
