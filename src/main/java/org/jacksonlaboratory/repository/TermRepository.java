package org.jacksonlaboratory.repository;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.monarchinitiative.phenol.ontology.data.TermId;


import java.util.List;
import java.util.Optional;

public interface TermRepository {

	Optional<List<OntologyTerm>> findAll();
	Optional<OntologyTerm> findByTermId(@NotNull TermId id);

	Optional<List<OntologyTerm>> findByTermIdIn(@NotEmpty List<TermId> ids);

	@Transactional
	List<OntologyTerm> search(String searchTerm, boolean prefixSearch);

	void saveAll(@NotEmpty List<OntologyTerm> terms);

	void configure();
}
