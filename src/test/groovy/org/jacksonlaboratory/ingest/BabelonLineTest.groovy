package org.jacksonlaboratory.ingest

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.jacksonlaboratory.ingest.BabelonLine
import org.jacksonlaboratory.model.Language
import org.jacksonlaboratory.model.PredicateType
import org.jacksonlaboratory.model.TranslationStatus
import spock.lang.Specification
import spock.lang.Unroll
import org.monarchinitiative.phenol.ontology.data.TermId;

class BabelonLineTest extends Specification {

    @Unroll
    void "test babelon line constructor with line"(){
        given:
        def babelon_line = BabelonLine.of(testBabelonLine())
        def babelon_line2 = BabelonLine.of(expectedSourceLanguage, expectedTranslationLanguage, expectedId, expectedType, expectedSourceText, expectedLanguageText, expectedStatus)
        expect:
        babelon_line.id() == expectedId
        babelon_line.source_language() == expectedSourceLanguage
        babelon_line.translation_language() == expectedTranslationLanguage
        babelon_line.type() == expectedType
        babelon_line.source_text() == expectedSourceText
        babelon_line.translation_text() == expectedLanguageText
        babelon_line.status() == expectedStatus
        babelon_line.equals(babelon_line2)
        where:
        expectedId  | expectedSourceLanguage | expectedTranslationLanguage | expectedType | expectedSourceText | expectedLanguageText | expectedStatus
        TermId.of("HP:0000005") | Language.EN | Language.NL | PredicateType.LABEL  |   "Mode of inheritance" | "Overervingspatroon" | TranslationStatus.CANDIDATE
    }

    def testBabelonLine(){
        return "en\tnl\tHP:0000005\trdfs:label\tMode of inheritance\tOverervingspatroon\tCANDIDATE"
    }
}
