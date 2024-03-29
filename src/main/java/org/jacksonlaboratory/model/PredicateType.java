package org.jacksonlaboratory.model;

import org.monarchinitiative.phenol.ontology.data.TermId;

public enum PredicateType {
	LABEL, DEFINITION, UNKNOWN;

	private static final TermId label = TermId.of("rdfs:label");
	private static final TermId definition = TermId.of("IAO:0000115");
	public static PredicateType fromTermId(TermId id) {
		if (id.equals(label)) {
			return PredicateType.LABEL;
		} else if (id.equals(definition)) {
			return PredicateType.DEFINITION;
		} else {
			return PredicateType.UNKNOWN;
		}
	}
}
