package org.jacksonlaboratory.repository;

import io.micronaut.data.annotation.Repository;
import io.micronaut.data.repository.CrudRepository;
import org.jacksonlaboratory.model.entity.Translation;

import java.util.UUID;

@Repository
public interface TranslationRepository extends CrudRepository<Translation, UUID> {
}
