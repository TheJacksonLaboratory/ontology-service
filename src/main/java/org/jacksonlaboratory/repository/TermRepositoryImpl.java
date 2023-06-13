package org.jacksonlaboratory.repository;

import io.micronaut.runtime.ApplicationConfiguration;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.model.OntologyTerm;
import org.monarchinitiative.phenol.ontology.data.TermId;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Singleton
public class TermRepositoryImpl implements TermRepository {
	private final EntityManager entityManager;
	private final ApplicationConfiguration applicationConfiguration;

	public TermRepositoryImpl(EntityManager entityManager, ApplicationConfiguration applicationConfiguration) {
		this.entityManager = entityManager;
		this.applicationConfiguration = applicationConfiguration;
	}

	@Override
	@ReadOnly
	public Optional<OntologyTerm> findById(TermId id) {
		return Optional.ofNullable(entityManager.find(OntologyTerm.class, id));
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
		for (OntologyTerm term:
			 terms
		) {
			this.entityManager.persist(term);
			this.entityManager.flush();
		}
	}

	@Transactional
	public void configure(){
		this.entityManager.createNativeQuery("CALL FTL_CREATE_INDEX('PUBLIC', 'ONTOLOGY_TERM', 'NAME,SYNONYMS,ID')").executeUpdate();
	}
}
