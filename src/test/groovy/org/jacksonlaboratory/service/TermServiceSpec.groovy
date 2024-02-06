package org.jacksonlaboratory.service

import io.micronaut.context.annotation.Property;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.jacksonlaboratory.model.entity.OntologyTermBuilder;
import org.jacksonlaboratory.repository.TermRepository
import org.monarchinitiative.phenol.ontology.data.TermId;
import spock.lang.Specification;

@MicronautTest(environments=["test"])
@Property(name = "international", value = "false")
class TermServiceSpec extends Specification {

    @Inject
    TermService termService

    @Inject
    TermRepository termRepository

    void "test search ontology term"() {
        when:
            def response = termService.searchOntologyTerm(q as String)
        then:
            1 * termRepository.search(_,_) >> getSearchResponse(false)
            response == getSearchResponse(true)
        where:
        q  << ["arach",  "UNKNOWN:00"]
    }

    def getSearchResponse(sorted) {
        if(sorted){
            return [
                    new OntologyTermBuilder().setId(TermId.of("UNKNOWN:000001")).setName("Arachnodactyly").createOntologyTerm(),
                    new OntologyTermBuilder().setId(TermId.of("UNKNOWN:000002")).setName("Insane Arachnodactyly").createOntologyTerm(),
                    new OntologyTermBuilder().setId(TermId.of("UNKNOWN:000003")).setName("Some other term").createOntologyTerm()
            ]
        }
        return [
                new OntologyTermBuilder().setId(TermId.of("UNKNOWN:000003")).setName("Some other term").createOntologyTerm(),
                new OntologyTermBuilder().setId(TermId.of("UNKNOWN:000002")).setName("Insane Arachnodactyly").createOntologyTerm(),
                new OntologyTermBuilder().setId(TermId.of("UNKNOWN:000001")).setName("Arachnodactyly").createOntologyTerm()
        ]
    }


    @MockBean(TermRepository)
    TermRepository termRepository() {
        Mock(TermRepository)
    }
}
