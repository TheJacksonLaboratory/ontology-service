package org.jacksonlaboratory.service;

import jakarta.inject.Singleton;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.repository.TermRepository;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.List;
import java.util.Optional;

@Singleton
public class TermService {
	private TermRepository termRepository;

	public TermService(TermRepository termRepository){
		this.termRepository = termRepository;
	}

	public Optional<OntologyTerm> getOntologyTermByTermId(TermId id){
		return this.termRepository.findById(id);
	}

	public List<OntologyTerm> searchOntologyTerm(String q){
		return this.termRepository.search(q);
	}

	private List<OntologyTerm> getInternationalTerms(TermId id){
		return null;
	}
}