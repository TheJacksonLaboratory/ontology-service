package org.jacksonlaboratory.model.converter;

import jakarta.inject.Singleton;
import org.monarchinitiative.phenol.ontology.data.TermId;
import javax.persistence.AttributeConverter;

@Singleton
public class TermIdAttributeConverter implements AttributeConverter<TermId, String> {

	@Override
	public String convertToDatabaseColumn(TermId attribute) {
		return attribute.getValue();
	}

	@Override
	public TermId convertToEntityAttribute(String dbData) {
		return TermId.of(dbData);
	}
}
