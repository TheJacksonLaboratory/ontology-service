package org.jacksonlaboratory.controller;

import io.micronaut.http.annotation.*;
import org.monarchinitiative.phenol.ontology.data.TermId;

@Controller("${api-prefix.hpo}/term")
public class HpoController {

    @Get(uri="/{id}", produces="application/json")
    public String details(@PathVariable TermId id) {
        return "Example Response";
    }

    @Get(uri="/{id}/genes", produces="application/json")
    public String genes(@PathVariable TermId id) {
        return "Example Response";
    }

    @Get(uri="/{id}/diseases", produces="application/json")
    public String diseases(@PathVariable TermId id) {
        return "Example Response";
    }
}