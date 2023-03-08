package org.jacksonlaboratory.model.converter;

import io.micronaut.core.convert.ConversionContext;
import io.micronaut.core.convert.TypeConverter;
import jakarta.inject.Singleton;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.Optional;

@Singleton
public class TermIdTypeConverter implements TypeConverter<String, TermId> {
    @Override
    public Optional<TermId> convert(String object, Class<TermId> targetType, ConversionContext context) {
        return Optional.of(TermId.of(object));
    }
}
