package org.jacksonlaboratory.repository

import io.micronaut.context.annotation.Property
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import jakarta.inject.Inject
import org.jacksonlaboratory.service.DataInitializer
import org.monarchinitiative.phenol.ontology.data.TermId
import spock.lang.Specification

@MicronautTest(environments=["test"], rollback = false)
@Property(name = "international", value = "true")
@Property(name = "load", value = "true")
class TermRepositorySpec extends Specification {

    @Inject
    TermRepository termRepository

    @Inject
    DataInitializer dataInitializer


    void "test find all"(){
        given:
        def res = termRepository.findAll()
        expect:
        res.isPresent()
        res.get().size() == 5
    }

    void "test find by term id"(){
        given:
        def res = termRepository.findByTermId(TermId.of("HP:0000002"))
        expect:
        res.isPresent()
        res.get().id == "HP:0000002"
    }

    void "test find by multiple term ids"(){
        given:
        def q = [TermId.of("HP:0000002"), TermId.of("HP:0000001")]
        def res = termRepository.findByTermIdIn(q)
        expect:
        res.isPresent()
        res.get().size() == 2
    }

    void "test search"(){
        given:
        def res = termRepository.search(t, prefixSearch)
        expect:
        res.size() == expectedSize
        where:
        t | prefixSearch | expectedSize
        "HP:00000" | true | 5
        "test" | false | 1
    }
}
