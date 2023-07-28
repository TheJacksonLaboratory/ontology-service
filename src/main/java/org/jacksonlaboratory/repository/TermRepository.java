package org.jacksonlaboratory.repository;

import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.monarchinitiative.phenol.ontology.data.TermId;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

public interface TermRepository {

	Optional<OntologyTerm> findByTermId(@NotBlank TermId id);

	Optional<List<OntologyTerm>> findByTermIdIn(@NotBlank List<TermId> ids);

	List<OntologyTerm> search(@NotBlank String query);

	OntologyTerm save(@NotBlank OntologyTerm term);

	void saveAll(@NotBlank List<OntologyTerm> terms);

	void configure();
}
