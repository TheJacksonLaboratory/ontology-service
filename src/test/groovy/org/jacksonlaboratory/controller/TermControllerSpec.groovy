package org.jacksonlaboratory.controller

import io.micronaut.context.annotation.Property
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.jacksonlaboratory.model.dto.SimpleOntologyTerm
import org.jacksonlaboratory.model.entity.OntologyTerm
import org.jacksonlaboratory.service.GraphService
import org.jacksonlaboratory.service.TermService
import org.monarchinitiative.phenol.ontology.data.TermId
import spock.lang.Specification

@MicronautTest
@Property(name = "api-url.prefix", value = "api")
@Property(name = "ontology", value = "hp")
class TermControllerSpec extends Specification {

    @Inject
    GraphService graphService

    @Inject
    TermService termService

    @Inject
    @Client('/')
    HttpClient client

    void "should return all terms"() {
        when:
        def response = client.toBlocking().exchange(HttpRequest.GET('/api/hp/terms/'), Argument.listOf(Map.class))
        then:
        1 * termService.getAllOntologyTerms() >> res
        response.body().size() == res.size()
        response.status().getCode().toInteger() == 200
        where:
        res  = [new OntologyTerm(TermId.of("HP:000003"), "fake name", "fake def", "comment"), new OntologyTerm(TermId.of("HP:000023"), "fake name 2", "fake def 2", "comment 1")]
    }

    void "should return single term"() {
        when:
        def response = client.toBlocking().exchange(HttpRequest.GET('/api/hp/terms/' + q), Map.class)
        then:
        1 * termService.getOntologyTermByTermId(TermId.of(q)) >> res
        response.body().get("id") == "HP:000003"
        response.body().get("name") == "fake name"
        response.body().get("definition") == "fake def"
        response.body().get("comment") == "comment"
        response.status().getCode().toInteger() == 200
        where:
        q | res
        "HP:000003"  | Optional.of(new OntologyTerm(TermId.of("HP:000003"), "fake name", "fake def", "comment"))
    }

    void "should return parents"() {
        when:
            def response = client.toBlocking().exchange(HttpRequest.GET('/api/hp/terms/' + q), Map.class)
        then:
            response.status().getCode().toInteger() == 400
        where:
        q | res
        "tan"  | "TermId has no prefix"
    }

    void "should return parents"() {
        when:
        def response = client.toBlocking().exchange(HttpRequest.GET('/api/hp/terms/' + q + '/children'), Argument.listOf(Map.class))
        then:
        1 * graphService.getChildren(TermId.of(q)) >> res
        response.body().size() == res.size()
        response.status().getCode().toInteger() == 200
        where:
        q | res
        "HP:000003"  |[new SimpleOntologyTerm(new OntologyTerm(TermId.of("HP:000003"), "fake name", "fake def", "comment")), new SimpleOntologyTerm(new OntologyTerm(TermId.of("HP:000023"), "fake name 2", "fake def 2", "comment 1"))]
    }

    void "should return bad request"(){
        when:
            def response = client.toBlocking().exchange(HttpRequest.GET('/api/hp/terms/' + q), Argument.listOf(Map.class))
        then:
            def e = thrown(HttpClientResponseException)
            e.status.getCode() == 400
        where:
        q | res
        "diseases"  | "Is not a valid term"
    }


    @MockBean(TermService)
    TermService termService() {
        Mock(TermService)
    }

    @MockBean(GraphService)
    GraphService graphService() {
        Mock(GraphService)
    }
}
