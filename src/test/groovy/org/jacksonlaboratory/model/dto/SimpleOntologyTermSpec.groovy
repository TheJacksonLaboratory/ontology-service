package org.jacksonlaboratory.model.dto

import org.jacksonlaboratory.model.entity.OntologyTerm
import org.monarchinitiative.phenol.ontology.data.TermId
import spock.lang.Specification

/*
	Other test methods for this class are covered elsewhere
 */
class SimpleOntologyTermSpec extends Specification {

	void 'test methods, equal, hashcode should pass'() {
		given:
        def o = new SimpleOntologyTerm(new OntologyTerm(TermId.of("HP:000003"), "fake name", "fake def", "comment", "", "", 0))
        def o2 = new SimpleOntologyTerm(new OntologyTerm(TermId.of("HP:000004"), "fake name 2", "fake def", "comment", "", "", 0))
        def o3 = new SimpleOntologyTerm(new OntologyTerm(TermId.of("HP:000003"), "fake name", "fake def", "comment", "", "", 0))
        expect:
        o.hashCode() != o2.hashCode()
        o.hashCode() == o3.hashCode()
        o != o2
        o == o3

	}

}

