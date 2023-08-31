package org.jacksonlaboratory.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.micronaut.http.annotation.*;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.service.GraphService;
import org.jacksonlaboratory.service.TermService;
import org.jacksonlaboratory.view.Views;
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
    @JsonView(Views.Term.class)
    @Get(uri="/", produces="application/json")
    public List<OntologyTerm> all() {
        return this.termService.getAllOntologyTerms();
    }

    /**
     * Get a term by ontology id
     * @param id The ontology term id
     * @return The term or null.
     */
    @JsonView(Views.Term.class)
    @Get(uri="/{id}", produces="application/json")
    public OntologyTerm details(@PathVariable TermId id) {
        Optional<OntologyTerm> term = this.termService.getOntologyTermByTermId(id);
        return term.orElse(null);
    }

    /**
     * Get the parents of the ontology id
     * @param id The ontology term id
     * @return The parents of the term id
     */
    @JsonView(Views.GraphOnly.class)
    @Get(uri="/{id}/parents", produces="application/json")
    public List<OntologyTerm> ancestors(@PathVariable TermId id){
        return this.graphService.getParents(id);
    }

    /**
     * Get the children of the ontology id
     * @param id The ontology term id
     * @return The children of the term id
     */
    @JsonView(Views.GraphOnly.class)
    @Get(uri="/{id}/children", produces="application/json")
    public List<OntologyTerm> children(@PathVariable TermId id){
        return this.graphService.getChildren(id);
    }

}
