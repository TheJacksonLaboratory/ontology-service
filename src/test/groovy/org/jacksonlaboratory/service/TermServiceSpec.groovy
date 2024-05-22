package org.jacksonlaboratory.service

import io.micronaut.context.annotation.Property;
import io.micronaut.test.annotation.MockBean;
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.jacksonlaboratory.model.Language
import org.jacksonlaboratory.model.TranslationStatus
import org.jacksonlaboratory.model.entity.OntologyTermBuilder
import org.jacksonlaboratory.model.entity.Translation;
import org.jacksonlaboratory.repository.TermRepository
import org.jacksonlaboratory.repository.TranslationRepository
import org.monarchinitiative.phenol.ontology.data.TermId;
import spock.lang.Specification;

@MicronautTest(environments=["test"], rebuildContext = true)
class TermServiceSpec extends Specification {

    @Inject
    TermService termService

    @Inject
    TermRepository termRepository

    @Inject
    TranslationRepository translationRepository

    void "test search ontology term"() {
        when:
            def response = termService.searchOntologyTerm(q as String)
        then:
            1 * termRepository.search(_,_) >> getSearchResponse(false)
            response == getSearchResponse(true)
        where:
        q  << ["arach",  "HP:00"]
    }

    void "test ontology term by termid no translations"() {
        when:
        def response = termService.getOntologyTermByTermId(id)
        then:
        1 * termRepository.findByTermId(_) >> termResponse
        if (expected) {
            response.isPresent()
            response.get().equals(termResponse.get())
        } else {
            response.isEmpty()
        }
        where:
        id | termResponse | translationResponse | expected
        TermId.of("HP:00001") | Optional.of(new OntologyTermBuilder(id, "Big Term").createOntologyTerm()) | [] | true
        TermId.of("HP:00001") | Optional.empty() |[] | false
    }

    @Property(name = "international", value = "true")
    void "test ontology term by termid with translations"() {
        when:
        System.setProperty("international", "true")
        def response = termService.getOntologyTermByTermId(id)
        then:
        1 * termRepository.findByTermId(_) >> termResponse
        1 * translationRepository.findAllByTerm(_) >> translationResponse
        response.isPresent()
        response.get().getTranslations().size() == 1
        response.get().getTranslations()[0].name == "Bad things"
        where:
        id | termResponse | translationResponse
        TermId.of("HP:00001") | Optional.of(new OntologyTermBuilder(id,"Fake Term").createOntologyTerm()) | [new Translation(termResponse.get(), Language.EN, "Bad things", "", TranslationStatus.OFFICIAL)]
    }

    def getSearchResponse(sorted) {
        if(sorted){
            return [
                    new OntologyTermBuilder(TermId.of("HP:000001"), "Arachnodactyly").createOntologyTerm(),
                    new OntologyTermBuilder(TermId.of("HP:000002"),"Insane Arachnodactyly").createOntologyTerm(),
                    new OntologyTermBuilder(TermId.of("HP:000003"),"Some other term").createOntologyTerm()
            ]
        }
        return [
                new OntologyTermBuilder(TermId.of("HP:000003"),"Some other term").createOntologyTerm(),
                new OntologyTermBuilder(TermId.of("HP:000002"),"Insane Arachnodactyly").createOntologyTerm(),
                new OntologyTermBuilder(TermId.of("HP:000001"),"Arachnodactyly").createOntologyTerm()
        ]
    }


    @MockBean(TermRepository)
    TermRepository termRepository() {
        Mock(TermRepository)
    }

    @MockBean(TranslationRepository)
    TranslationRepository translationRepository() {
        Mock(TranslationRepository)
    }
}
