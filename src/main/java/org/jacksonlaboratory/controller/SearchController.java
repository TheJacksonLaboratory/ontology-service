package org.jacksonlaboratory.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import org.jacksonlaboratory.model.OntologyTerm;
import org.jacksonlaboratory.service.TermService;

import java.util.List;
@Controller("${api-url.prefix}/${ontology}/search")
public class SearchController {

	private TermService termService;

	public SearchController(TermService termService) {
		this.termService = termService;
	}

	@Get(uri="/", produces="application/json")
	public List<OntologyTerm> search(@QueryValue("q") String query) {
		return this.termService.searchOntologyTerm(query);
	}
}
