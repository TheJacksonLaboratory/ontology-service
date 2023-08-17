package org.jacksonlaboratory.model.converter

import org.monarchinitiative.phenol.ontology.data.TermId
import spock.lang.Specification
import spock.lang.Unroll

class TermIdAttributeConverterSpec extends Specification {

    @Unroll
    void "test convert to database column"(){
        given:
        def converter = new TermIdAttributeConverter()
        expect:
        converter.convertToDatabaseColumn(inputColumn) == inputColumn.toString()
        where:
        inputColumn << [TermId.of("HP:000013"), TermId.of("MONDO:000913"), TermId.of("OMIM:0092323")   ]
    }

    @Unroll
    void "test convert to entity attribute"(){
        given:
        def converter = new TermIdAttributeConverter()
        expect:
        converter.convertToEntityAttribute(inputEntity) == TermId.of(inputEntity)
        where:
        inputEntity << ["HP:000013", "MONDO:000913", "OMIM:0092323"]
    }
}
