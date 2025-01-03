package org.jacksonlaboratory.controller;

import io.micronaut.core.annotation.Nullable;
import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import org.jacksonlaboratory.model.dto.SimpleOntologyTerm;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.service.GraphService;
import org.jacksonlaboratory.service.TermService;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public List<OntologyTerm> all(
            @QueryValue(value = "filter")
            @Nullable
            @Parameter(schema = @Schema(maxLength = 250, type = "string", pattern = "^(HP:\\d{7})(,HP:\\d{7})*$"), required = false)
            @Pattern(regexp = "^(HP:\\d{7})(,HP:\\d{7})*$", message = "message = Invalid format. Expected format: HP:0001166 or HP:0001166,HP:0001234") String filter) {

        if (filter == null || filter.isBlank()){
            return this.termService.getAllOntologyTerms(List.of());
        } else {
            List<TermId> termIds = Arrays.stream(filter.split(",")).map(TermId::of).collect(Collectors.toUnmodifiableList());
            return this.termService.getAllOntologyTerms(termIds);
        }
    }

    /**
     * Get a term by ontology id
     * @param id The ontology term id
     * @return The term or null.
     */
    @Get(uri="/{id}", produces="application/json")
    public HttpResponse<?> details(@Schema(minLength = 1, maxLength = 20, type = "string", pattern = ".*")
                                       @PathVariable String id) {
        TermId termId = TermId.of(id);
        Optional<TermId> replacement = this.graphService.getMostRecentTermId(termId);

        if (replacement.isPresent()){
            termId = replacement.get();
        }

        Optional<OntologyTerm> term = this.termService.getOntologyTermByTermId(termId);
        if (term.isEmpty()){
            return HttpResponse.notFound();
        } else {
            return HttpResponse.ok(term.get());
        }
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
     * @return The ancestors of the term id
     */
    @Get(uri="/{id}/ancestors", produces="application/json")
    public List<SimpleOntologyTerm> ancestors(@Schema(minLength = 1, maxLength = 20, type = "string", pattern = ".*") @PathVariable String id){
        TermId termId = TermId.of(id);
        return this.graphService.getAncestors(termId);
    }

}
