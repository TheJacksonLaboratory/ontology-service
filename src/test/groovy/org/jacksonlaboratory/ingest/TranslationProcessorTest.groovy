package org.jacksonlaboratory.ingest

import org.jacksonlaboratory.model.Language
import org.jacksonlaboratory.model.TranslationStatus
import org.jacksonlaboratory.model.entity.OntologyTerm
import org.monarchinitiative.phenol.ontology.data.TermId
import spock.lang.Specification

import java.nio.file.Path

class TranslationProcessorTest extends Specification {

    void "test babelon line to translation"(){
        given:
        def term = TermId.of("HP:0000002")
        def babelonNavigator = BabelonIngestor.of().load(Path.of("src/test/resources/hp-test.babelon.tsv"))
        def languages = babelonNavigator.getAggregatedLanguageLinesById(term)
        def language = Language.NL
        def ontology_term = new OntologyTerm(term, "Abnormality of body height", "", "")
        def translation = TranslationProcessor.processTranslation(ontology_term, language, languages.get().get(language)).get(0)
        expect:
        translation.language.equals(Language.NL)
        translation.name.equals("Afwijking van de lichaamslengte")
        translation.definition.equals("Afwijking van de norm van hoogte met betrekking tot wat de verwachting is volgens leeftijds en geslachtsnormen")
        translation.status.equals(TranslationStatus.CANDIDATE)
    }
}
