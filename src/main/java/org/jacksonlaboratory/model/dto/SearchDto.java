package org.jacksonlaboratory.model.dto;

import org.jacksonlaboratory.model.entity.OntologyTerm;

import java.util.List;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SearchDto searchDto = (SearchDto) o;
		return totalCount == searchDto.totalCount && Objects.equals(terms, searchDto.terms);
	}

	@Override
	public int hashCode() {
		return Objects.hash(terms, totalCount);
	}
}
