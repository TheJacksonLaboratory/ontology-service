package org.jacksonlaboratory.controller;

import io.micronaut.http.annotation.*;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.service.TermService;
import org.monarchinitiative.phenol.ontology.data.TermId;
import java.util.Optional;

@Controller("${api-url.prefix}/${ontology}/term")
public class TermController {

    private TermService termService;

    public TermController(TermService termService) {
        this.termService = termService;
    }

    @Get(uri="/{id}", produces="application/json")
    public OntologyTerm details(@PathVariable TermId id) {
        Optional<OntologyTerm> term = this.termService.getOntologyTermByTermId(id);
        return term.orElse(null);
    }

    @Get(uri="/{id}/parents", produces="application/json")
    public OntologyTerm parents(@PathVariable TermId id){
        return null;
    }

    @Get(uri="/{id}/children", produces="application/json")
    public OntologyTerm children(@PathVariable TermId id){
        return null;
    }

}