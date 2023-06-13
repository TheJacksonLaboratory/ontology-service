package org.jacksonlaboratory.repository;

import org.jacksonlaboratory.model.OntologyTerm;
import org.monarchinitiative.phenol.ontology.data.TermId;

import javax.validation.constraints.NotBlank;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TermRepository {

	Optional<OntologyTerm> findById(@NotBlank TermId id);
	List<OntologyTerm> search(@NotBlank String query);

	OntologyTerm save(@NotBlank OntologyTerm term);

	void saveAll(@NotBlank List<OntologyTerm> terms);

	void configure();
}
