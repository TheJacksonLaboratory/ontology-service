package org.jacksonlaboratory.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.Translation;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface TranslationRepository extends CrudRepository<Translation, Long> {

	List<Translation> findAllByTerm(@NotNull OntologyTerm term);
}
