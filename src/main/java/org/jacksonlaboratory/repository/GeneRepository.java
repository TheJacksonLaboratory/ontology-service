package org.jacksonlaboratory.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.jacksonlaboratory.model.Gene;

@Repository
public interface GeneRepository extends CrudRepository<Gene, Long> {
}
