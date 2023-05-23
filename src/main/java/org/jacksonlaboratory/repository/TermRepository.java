package org.jacksonlaboratory.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.jacksonlaboratory.model.OntologyTerm;
import org.monarchinitiative.phenol.ontology.data.TermId;

@Repository
public interface TermRepository extends CrudRepository<OntologyTerm, TermId> {}
