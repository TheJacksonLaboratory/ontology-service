package org.jacksonlaboratory.repository;

import io.micronaut.context.annotation.Property;
import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.OntologyTermBuilder;
import org.monarchinitiative.phenol.ontology.data.TermId;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Singleton
public class TermRepositoryImpl implements TermRepository {

	private final EntityManager entityManager;

	@Property(name = "ontology") String ontologyName;

	public TermRepositoryImpl(EntityManager entityManager) {
		this.entityManager = entityManager;
	}

	@Override
	@ReadOnly
	public List<OntologyTerm> findAll(){
		return entityManager.createQuery("SELECT t FROM OntologyTerm t", OntologyTerm.class).getResultStream().collect(Collectors.toList());
	}

	@Override
	@ReadOnly
	public Optional<OntologyTerm> findByTermId(TermId id) {
		Optional<OntologyTerm> term = entityManager.createQuery("SELECT t FROM OntologyTerm t WHERE t.id = :param1", OntologyTerm.class).setParameter("param1", id).getResultStream().findFirst();
		return term;
	}

	@Override
	public List<OntologyTerm> findByTermIdIn(List<TermId> ids) {
		List<OntologyTerm> terms = entityManager.createQuery("SELECT t FROM OntologyTerm t WHERE t.id in :param1", OntologyTerm.class).setParameter("param1", ids).getResultList();
		terms.sort(Comparator.comparing(item -> ids.indexOf(TermId.of(item.getId()))));
		return terms;
	}

	/*
		Use our h2 lucene index and a search term to find objects similar in our tables. Currently,
		we will only index by term and synonym.
	 */
	@Override
	@Transactional
	public List<OntologyTerm> search(String searchTerm, boolean prefixSearch) {
		if (prefixSearch){
			return searchPartialId(searchTerm).collect(Collectors.toList());
		} else {
			TypedQuery<OntologyTerm> sp = entityManager.createNamedQuery("searchQuery", OntologyTerm.class);
			sp.setParameter("param1", String.format("%s*", searchTerm));
			return sp.getResultStream().collect(Collectors.toList());

		}
	}

	Stream<OntologyTerm> searchPartialId(String partial){
		@SuppressWarnings("unchecked")
		Stream<Object[]> result = entityManager
				.createNativeQuery("SELECT t.* FROM ONTOLOGY_TERM t WHERE t.id like :param1")
				.setParameter("param1", partial + "%").getResultStream();
		return result.map(row ->
				new OntologyTermBuilder().setDescendantCount((int) row[0]).setId(TermId.of((String) row[4]))
						.setName((String) row[5]).setDefinition((String) row[2]).setComment("").setXrefs((String) row[7]).createOntologyTerm()
		);
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
		this.entityManager.createNativeQuery("CALL FTL_CREATE_INDEX('PUBLIC', 'ONTOLOGY_TERM', 'NAME,SYNONYMS')").executeUpdate();
	}
}
