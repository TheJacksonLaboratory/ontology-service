package org.jacksonlaboratory.model

import org.jacksonlaboratory.model.entity.OntologyTerm
import org.jacksonlaboratory.model.entity.Translation
import org.monarchinitiative.phenol.ontology.data.TermId
import spock.lang.Specification

class TranslationSpec extends Specification {

    void "test empty translation constructor"(){
        given:
        def translation = new Translation()
        expect:
        translation != null
    }

    void "test full translation constructor"(){
        given:
        def translation = new Translation(inputTerm, inputLanguage, inputName, inputDefinition, inputStatus)

        expect:
        translation.getTerm() == inputTerm
        translation.getLanguage() == inputLanguage
        translation.getName() == inputName
        translation.getDefinition() == inputDefinition
        translation.getStatus() == inputStatus

        where:
        inputTerm | inputLanguage | inputName | inputDefinition | inputStatus
        new OntologyTerm(TermId.of("HP:0000014"), "fake Term", "fake def", "fake comment", "", "", 0) | Language.EN | "fake translation" | "fake input definition" | TranslationStatus.OFFICIAL

    }
}
