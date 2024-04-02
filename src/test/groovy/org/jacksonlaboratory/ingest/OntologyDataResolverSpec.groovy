package org.jacksonlaboratory.ingest

import spock.lang.Specification

import java.nio.file.Path

class OntologyDataResolverSpec extends Specification {

    void 'test of ontology data resolver'(){
        given:
            Path dataDirectory = Path.of("src/test/resources");
            OntologyDataResolver dataResolver = OntologyDataResolver.of(dataDirectory, "hp");
        expect:
            dataResolver.ontologyJson() == dataDirectory.resolve("hp-base.json")
            dataDirectory == dataResolver.dataDirectory()
    }
}
