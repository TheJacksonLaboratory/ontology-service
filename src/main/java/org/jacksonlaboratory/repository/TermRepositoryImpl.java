package org.jacksonlaboratory.repository;

import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.monarchinitiative.phenol.ontology.data.TermId;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Singleton
public class TermRepositoryImpl implements TermRepository {
	private final EntityManager entityManager;

	public TermRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@ReadOnly
	public Optional<OntologyTerm> findByTermId(TermId id) {
		OntologyTerm term = entityManager.createQuery("SELECT t FROM OntologyTerm t WHERE t.id = :param1", OntologyTerm.class).setParameter("param1", id).getSingleResult();
		return Optional.ofNullable(term);
	}

	/*
		Use our h2 lucene index and a search term to find objects similar in our tables. Currently,
		we will only index by ontology id, term and synonym.
	 */
	@Override
	@Transactional
	public List<OntologyTerm> search(String searchTerm) {
		TypedQuery<OntologyTerm> sp = entityManager.createNamedQuery("searchQuery", OntologyTerm.class);
		sp.setParameter("param1", String.format("%s*", searchTerm));
		return sp.getResultList();
	}

	@Override
	@Transactional
	public OntologyTerm save(OntologyTerm term) {
		entityManager.persist(term);
		return term;
	}

	@Override
	@Transactional
	public void saveAll(List<OntologyTerm> terms){
		for(var i=0; i < terms.size(); i++){
			if ( i > 0 && i % 5000 == 0){
				this.entityManager.flush();
				this.entityManager.clear();
			}
			this.entityManager.persist(terms.get(i));
		}
	}

	@Transactional
	public void configure(){
		this.entityManager.createNativeQuery("CALL FTL_CREATE_INDEX('PUBLIC', 'ONTOLOGY_TERM', 'NAME,SYNONYMS,ID')").executeUpdate();
	}
}
