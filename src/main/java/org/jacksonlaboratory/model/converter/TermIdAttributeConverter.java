package org.jacksonlaboratory.model.converter;

import org.monarchinitiative.phenol.ontology.data.TermId;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

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
