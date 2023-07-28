package org.jacksonlaboratory.controller;

import io.micronaut.http.annotation.*;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.service.GraphService;
import org.jacksonlaboratory.service.TermService;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.List;
import java.util.Optional;

@Controller("${api-url.prefix}/${ontology}/term")
public class TermController {

    private final TermService termService;
    private final GraphService graphService;

    public TermController(TermService termService, GraphService graphService) {
        this.termService = termService;
        this.graphService = graphService;
    }

    @Get(uri="/{id}", produces="application/json")
    public OntologyTerm details(@PathVariable TermId id) {
        Optional<OntologyTerm> term = this.termService.getOntologyTermByTermId(id);
        return term.orElse(null);
    }

    @Get(uri="/{id}/ancestors", produces="application/json")
    public List<OntologyTerm> parents(@PathVariable TermId id){
        return this.graphService.getAncestors(id);
    }

    @Get(uri="/{id}/children", produces="application/json")
    public List<OntologyTerm> children(@PathVariable TermId id){
        return this.graphService.getChildren(id);
    }

}
