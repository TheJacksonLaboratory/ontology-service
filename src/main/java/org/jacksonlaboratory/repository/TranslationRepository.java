package org.jacksonlaboratory.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.Translation;
import java.util.List;

@Repository
public interface TranslationRepository extends CrudRepository<@Valid Translation, @NotNull Long> {

	List<Translation> findAllByTerm(@NotNull OntologyTerm term);
}
