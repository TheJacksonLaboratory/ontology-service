package org.jacksonlaboratory.service

import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.jacksonlaboratory.model.Language
import org.jacksonlaboratory.model.TranslationStatus
import org.jacksonlaboratory.model.entity.OntologyTerm
import org.jacksonlaboratory.model.entity.OntologyTermBuilder
import org.jacksonlaboratory.model.entity.Translation
import org.jacksonlaboratory.repository.TermRepository
import org.jacksonlaboratory.repository.TranslationRepository
import org.monarchinitiative.phenol.ontology.data.TermId
import spock.lang.Specification

@MicronautTest(environments=["test"])
class GraphServiceSpec extends Specification {

    @Inject
    GraphService graphService

    @Inject
    TermRepository termRepository

    @Inject
    TranslationRepository translationRepository;


    void 'test graph service get ontology'() {
        when:
        def ontology = graphService.getOntology()
        then:
        ontology.getTerms().size() == 5
    }


    void 'test graph service get parents'() {
        when:
            def terms = graphService.getParents(TermId.of("HP:0000004"))
        then:
            1 * termRepository.findByTermIdIn(_) >> Optional.of([new OntologyTermBuilder().setId(TermId.of("HP:000001")).createOntologyTerm()]);
            terms.size() == 1
    }

    void 'test graph service get children'() {
        when:
        def terms = graphService.getChildren(TermId.of("HP:0000004"))
        then:
        1 * termRepository.findByTermIdIn(_) >> Optional.of([]);
        terms.size() == 0
    }

    void 'test graph service descendant count'() {
        when:
        def count = graphService.getDescendantCount(TermId.of("HP:0000001"))
        then:
        count == 4
    }

    void 'test graph service get descendants'() {
        when:
        def terms = graphService.getDescendants(TermId.of("HP:0000001"))
        then:
        1 * termRepository.findByTermIdIn(_) >> Optional.of([new OntologyTermBuilder().setId(TermId.of("HP:000001")).createOntologyTerm(), new OntologyTermBuilder().setId(TermId.of("HP:000002")).createOntologyTerm(), new OntologyTermBuilder().setId(TermId.of("HP:000003")).createOntologyTerm()]);
        terms.size() == 3
    }


    void 'test graph service add unpack'() {
        when:
        def u = graphService.unpack(l)
        then:
        _ * termRepository.findByTermIdIn(_) >> Optional.of([]);
        u.size() == 0
        where:
        l << [["ONTOLOGY"], []]
    }

    void 'test graph service add translation'() {
        when:
        def u = graphService.addTranslations(l)
        then:
        1 * translationRepository.findAllByTerm(_) >> [new Translation(l[0], Language.EN, "Bad things", "", TranslationStatus.OFFICIAL)]
        u.size() == 1
        u.get(0).getTranslations().size() == 1
        u.get(0).getTranslations()[0].name == "Bad things"
        where:
        l = [new OntologyTermBuilder().setId(TermId.of("HP:000001")).createOntologyTerm()]
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
