package org.jacksonlaboratory.ingest

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.jacksonlaboratory.model.Language
import org.monarchinitiative.phenol.ontology.data.TermId
import spock.lang.Specification

import java.nio.file.Path

class BabelonIngestorTest extends Specification {

    void "test ingest simple babelon file"(){
        given:
        def babelonNavigator = BabelonIngestor.of().load(Path.of("src/test/resources/hp-test.babelon.tsv"))
        expect:
        babelonNavigator.numberTermsTranslated() == 3
        def translation = babelonNavigator.getAggregatedLanguageLinesById(TermId.of("HP:0000002"))
        !translation.isEmpty()
        translation.get().get(Language.NL).size() == 2
    }
}
