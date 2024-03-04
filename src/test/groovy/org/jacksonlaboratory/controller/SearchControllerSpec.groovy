package org.jacksonlaboratory.controller

import io.micronaut.context.annotation.Property
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.HttpClient
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.jacksonlaboratory.model.entity.OntologyTerm
import io.micronaut.core.type.Argument
import org.jacksonlaboratory.service.TermService
import spock.lang.Specification
import org.monarchinitiative.phenol.ontology.data.TermId

@MicronautTest
@Property(name = "api-url.prefix", value = "api")
@Property(name = "ontology", value = "hp")
class SearchControllerSpec extends Specification {

    @Inject
    TermService termService

    @Inject
    @Client('/')
    HttpClient client

    void "should search #q and return the fake object"() {
        when:
        def response = client.toBlocking().retrieve(HttpRequest.GET('/api/hp/search?q='+ q), Map.class)
        then:
        termService.searchOntologyTerm(q) >> res
        response.get("terms").size() == res.size();
        response.get("terms").get(0).get("id").equals(res.get(0).getId())
        response.get("terms").get(0).get("name").equals(res.get(0).getName())
        response.get("terms").get(0).get("definition").equals(res.get(0).getDefinition())
        response.get("terms").get(0).get("comment").equals(res.get(0).getComment())
        where:
        q | res
        "arach" | [new OntologyTerm(TermId.of("HP:000003"), "fake name", "fake def", "comment", "", "", 0)]
    }

    @MockBean(TermService)
    TermService termService() {
        Mock(TermService)
    }
}
