package org.jacksonlaboratory.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.jacksonlaboratory.model.Disease;
import org.monarchinitiative.phenol.ontology.data.TermId;

@Repository
public interface DiseaseRepository extends CrudRepository<Disease, TermId> {
}
