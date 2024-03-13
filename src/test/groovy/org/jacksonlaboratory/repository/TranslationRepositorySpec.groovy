package org.jacksonlaboratory.repository

import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.jacksonlaboratory.model.Language
import org.jacksonlaboratory.service.DataInitializer
import org.jacksonlaboratory.service.TermService
import spock.lang.Specification

@MicronautTest(environments=["test"])
@Property(name = "international", value = "true")
@Property(name = "load", value = "true")
class TranslationRepositorySpec extends  Specification {

    @Inject
    TranslationRepository translationRepository;

    @Inject
    TermService termService

    @Inject
    DataInitializer dataInitializer

    void "test find all by term"() {
        when:
            def term = termService.getAllOntologyTerms().find {it -> it.id == "HP:0000002"}.collect()
            def res = translationRepository.findAllByTerm(term[0])
        then:
        res.size() == 1
        res[0].language == Language.NL
    }
}
