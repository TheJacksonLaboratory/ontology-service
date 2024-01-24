package org.jacksonlaboratory.controller;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.QueryValue;
import io.micronaut.serde.annotation.SerdeImport;
import org.jacksonlaboratory.model.dto.SearchDto;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.service.TermService;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.stream.Collectors;

@Controller("${api-url.prefix}/${ontology}/search")
@SerdeImport(SearchDto.class)
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
	@Get(produces="application/json")
	public SearchDto search(
			@QueryValue("q") @Schema(minLength = 3, maxLength = 250, type = "string", pattern = ".*")  String query,
			@QueryValue(value = "page", defaultValue = "0") @Schema(maxLength = 1000, type = "number") int page,
			@QueryValue(value = "limit", defaultValue = "10") @Schema(maxLength = 1, type = "number") int limit
	) {
		page = page * limit;
		List<OntologyTerm> terms = this.termService.searchOntologyTerm(query);
		if (limit == -1) {
			return new SearchDto(terms,  terms.size());
		} else {
			return new SearchDto(terms.stream().skip(page).limit(limit).collect(Collectors.toList()),  terms.size());
		}
	}
}
