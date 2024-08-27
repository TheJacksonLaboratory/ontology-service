package org.jacksonlaboratory.controller;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jacksonlaboratory.model.dto.SimpleOntologyTerm;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.service.GraphService;
import org.jacksonlaboratory.service.TermService;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.List;
import java.util.Optional;

@Controller("${api-url.prefix}/${ontology}/terms")
public class TermController {

    private final TermService termService;
    private final GraphService graphService;

    public TermController(TermService termService, GraphService graphService) {
        this.termService = termService;
        this.graphService = graphService;
    }

    /**
     * Get all terms
     * @return List of all ontology terms
     */
    @Get(uri="/", produces="application/json")
    public List<OntologyTerm> all() {
        return this.termService.getAllOntologyTerms();
    }

    /**
     * Get a term by ontology id
     * @param id The ontology term id
     * @return The term or null.
     */
    @Get(uri="/{id}", produces="application/json")
    public HttpResponse<?> details(@Schema(minLength = 1, maxLength = 20, type = "string", pattern = ".*") @PathVariable String id) {
        TermId termId = TermId.of(id);
        Optional<OntologyTerm> term = this.termService.getOntologyTermByTermId(termId);
        return HttpResponse.ok(term.orElse(null));
    }

    /**
     * Get the parents of the ontology id
     * @param id The ontology term id
     * @return The parents of the term id
     */
    @Get(uri="/{id}/parents", produces="application/json")
    public List<SimpleOntologyTerm> parents(@Schema(minLength = 1, maxLength = 20, type = "string", pattern = ".*") @PathVariable String id){
        TermId termId = TermId.of(id);
        return this.graphService.getParents(termId);
    }

    /**
     * Get the children of the ontology id
     * @param id The ontology term id
     * @return The children of the term id
     */
    @Get(uri="/{id}/children", produces="application/json")
    public List<SimpleOntologyTerm> children(@Schema(minLength = 1, maxLength = 20, type = "string", pattern = ".*") @PathVariable String id){
        TermId termId = TermId.of(id);
        return this.graphService.getChildren(termId);
    }

    /**
     * Get the descendants of the ontology id
     * @param id The ontology term id
     * @return The descendants of the term id
     */
    @Get(uri="/{id}/descendants", produces="application/json")
    public List<SimpleOntologyTerm> descendants(@Schema(minLength = 1, maxLength = 20, type = "string", pattern = ".*") @PathVariable String id){
        TermId termId = TermId.of(id);
        return this.graphService.getDescendants(termId);
    }

    /**
     * Get the ancestors of the ontology id
     * @param id The ontology term id
     * @return The descendants of the term id
     */
    @Get(uri="/{id}/ancestors", produces="application/json")
    public List<SimpleOntologyTerm> ancestors(@Schema(minLength = 1, maxLength = 20, type = "string", pattern = ".*") @PathVariable String id){
        TermId termId = TermId.of(id);
        return this.graphService.getAncestors(termId);
    }

}
