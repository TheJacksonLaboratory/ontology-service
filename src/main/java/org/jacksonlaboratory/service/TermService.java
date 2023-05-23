package org.jacksonlaboratory.service;

import jakarta.inject.Singleton;
import org.jacksonlaboratory.model.OntologyTerm;
import org.jacksonlaboratory.repository.TermRepository;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.Optional;

@Singleton
public class TermService {
	private final TermRepository termRepository;
	public TermService(TermRepository termRepository) {
		this.termRepository = termRepository;
	}

	public Optional<OntologyTerm> getOntologyTermByTermId(TermId id){
		return this.termRepository.findById(id);
	}
}
