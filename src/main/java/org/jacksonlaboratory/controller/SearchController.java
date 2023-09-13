package org.jacksonlaboratory.controller;

import com.fasterxml.jackson.annotation.JsonView;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.service.TermService;
import org.jacksonlaboratory.view.Views;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Controller("${api-url.prefix}/${ontology}/search")
public class SearchController {

	private TermService termService;

	public SearchController(TermService termService) {
		this.termService = termService;
	}

	 /**
	 * Search for ontology term, id, synonym
     * @param query The search value
     * @return List of matching ontology terms
     */
	@JsonView(Views.Term.class)
	@Get(uri="/", produces="application/json")
	public List<OntologyTerm> search(@QueryValue("q") @Schema(minLength = 3, maxLength = 250, type = "string", pattern = ".*")  String query) {
		return this.termService.searchOntologyTerm(query);
	}
}
