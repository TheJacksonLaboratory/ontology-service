package org.jacksonlaboratory.model.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.monarchinitiative.phenol.ontology.data.TermId;



@Converter
public class TermIdAttributeConverter implements AttributeConverter<TermId, String> {

	@Override
	public String convertToDatabaseColumn(TermId attribute) {
		return attribute.toString();
	}

	@Override
	public TermId convertToEntityAttribute(String dbData) {
		return TermId.of(dbData);
	}
}
