package org.jacksonlaboratory.repository;

import io.micronaut.data.repository.CrudRepository;
import org.jacksonlaboratory.model.entity.Translation;

public interface TranslationRepository extends CrudRepository<Translation, Long> {
}
