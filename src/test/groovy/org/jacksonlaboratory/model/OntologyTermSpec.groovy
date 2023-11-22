package org.jacksonlaboratory.model

import com.fasterxml.jackson.databind.json.JsonMapper
import org.jacksonlaboratory.model.entity.OntologyTerm
import org.jacksonlaboratory.view.Views
import org.monarchinitiative.phenol.ontology.data.TermId
import spock.lang.Specification
import com.fasterxml.jackson.databind.MapperFeature
class OntologyTermSpec extends Specification {

    void 'test user view works'() {
        given:
        def term = new OntologyTerm(TermId.of("HP:000001"), 'my name', 'def', null)
        expect:
        JsonMapper mapper = JsonMapper.builder().disable(MapperFeature.DEFAULT_VIEW_INCLUSION).build()
        String result = mapper
                .writeValueAsString(term);

        result.contains("my name")
        result.contains("def")
        result == "{\"id\":\"HP:000001\",\"name\":\"my name\",\"definition\":\"def\",\"comment\":null,\"synonyms\":[],\"xrefs\":[],\"translations\":null}"
    }
}
