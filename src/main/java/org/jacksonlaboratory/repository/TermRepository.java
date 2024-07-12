package org.jacksonlaboratory.repository;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.monarchinitiative.phenol.ontology.data.TermId;


import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TermRepository {

	/***
	 * This finds terms we have
	 * @return empty list or list of {@link OntologyTerm}
	 */
	List<OntologyTerm> findAll();

	/***
	 * Fins a single ontology term
	 * @param id the {@link TermId} we are searching for
	 * @return an optional {@link OntologyTerm}
	 */
	Optional<OntologyTerm> findByTermId(@NotNull TermId id);

	/***
	 * Finds ontology terms that exist in our list
	 * @param ids the list of {@link TermId} we are searching for
	 * @return an empty list or list of {@link OntologyTerm}
	 */
	List<OntologyTerm> findByTermIdIn(@NotEmpty List<TermId> ids);

	/***
	 * Search for an ontology term
	 * @param searchTerm - the input query
	 * @param prefixSearch - whether or not the search term starts with a CURIE
	 * @return
	 */
	List<OntologyTerm> search(String searchTerm, boolean prefixSearch);

	/***
	 * Saving terms
	 * @param terms a non empty list of {@link OntologyTerm}
	 */
	void saveAll(@NotEmpty List<OntologyTerm> terms);

	/***
	 * This is used for any additional database configuration needed post startup
	 */
	void configure();
}
