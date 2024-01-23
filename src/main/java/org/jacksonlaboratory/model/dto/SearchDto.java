package org.jacksonlaboratory.model.dto;

import org.jacksonlaboratory.model.entity.OntologyTerm;

import java.util.List;

public class SearchDto {
	private final List<OntologyTerm> terms;
	private final int totalCount;

	public SearchDto(List<OntologyTerm> terms, int totalCount) {
		this.terms = terms;
		this.totalCount = totalCount;
	}

	public List<OntologyTerm> getTerms() {
		return terms;
	}

	public int getTotalCount() {
		return totalCount;
	}
}
