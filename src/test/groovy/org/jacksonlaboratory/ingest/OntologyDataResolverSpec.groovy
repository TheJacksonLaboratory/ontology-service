package org.jacksonlaboratory.ingest

import spock.lang.Specification

import java.nio.file.Path

class OntologyDataResolverSpec extends Specification {

    void 'test of'(){
        Path dataDirectory = Path.of("src/test/resources");
        OntologyDataResolver dataResolver = HpoDataResolver.of(dataDirectory, "hp");
        assertEquals(dataResolver.ontologyJson(), dataDirectory.resolve("hp-base.json"));
        assertEquals(dataDirectory, dataResolver.dataDirectory());
    }
}
