package org.jacksonlaboratory.repository;

import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.monarchinitiative.phenol.ontology.data.TermId;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class TermRepositoryImpl implements TermRepository {

	private final EntityManager entityManager;

	public TermRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@ReadOnly
	public Optional<List<OntologyTerm>> findAll(){
		List<OntologyTerm> terms = entityManager.createQuery("SELECT t FROM OntologyTerm t", OntologyTerm.class).getResultStream().collect(Collectors.toList());
		return Optional.of(terms);
	}

	@Override
	@ReadOnly
	public Optional<OntologyTerm> findByTermId(TermId id) {
		Optional<OntologyTerm> term = entityManager.createQuery("SELECT t FROM OntologyTerm t WHERE t.id = :param1", OntologyTerm.class).setParameter("param1", id).getResultStream().findFirst();
		return term;
	}

	@Override
	public Optional<List<OntologyTerm>> findByTermIdIn(List<TermId> ids) {
		List<OntologyTerm> terms = entityManager.createQuery("SELECT t FROM OntologyTerm t WHERE t.id in :param1", OntologyTerm.class).setParameter("param1", ids).getResultList();
		Collections.sort(terms,
				Comparator.comparing(item -> ids.indexOf(TermId.of(item.getId()))));
		return Optional.of(terms);
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
		return sp.getResultStream().collect(Collectors.toList());
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
