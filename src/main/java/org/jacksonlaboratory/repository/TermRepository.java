package org.jacksonlaboratory.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.monarchinitiative.phenol.ontology.data.TermId;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
public abstract class TermRepository implements CrudRepository<OntologyTerm, Long> {


	private final EntityManager entityManager;

	public TermRepository(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	public abstract Optional<OntologyTerm> findByTermId(@NotNull TermId termId);

	/*
		Use our h2 lucene index and a search term to find objects similar in our tables. Currently,
		we will only index by ontology id, term and synonym.
	 */
	@Transactional
	public List<OntologyTerm> search(String searchTerm) {
		TypedQuery<OntologyTerm> sp = entityManager.createNamedQuery("searchQuery", OntologyTerm.class);
		sp.setParameter("param1", String.format("%s*", searchTerm));
		return sp.getResultList();
	}

	@Transactional
	public void saveAll(List<OntologyTerm> terms){
		for(var i=0; i < terms.size(); i++){
			if ( i > 0 && i % 5000 == 0){
				this.entityManager.flush();
			}
			this.entityManager.persist(terms.get(i));
		}
	}

	@Transactional
	public void create_index(){
		this.entityManager.createNativeQuery("CALL FTL_CREATE_INDEX('PUBLIC', 'ONTOLOGY_TERM', 'NAME,SYNONYMS,ID')").executeUpdate();
	}
}
