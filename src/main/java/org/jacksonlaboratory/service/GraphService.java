package org.jacksonlaboratory.service;

import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.repository.TermRepository;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.MinimalOntology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Singleton
@Requires(property = "ontology")
@Transactional
public class GraphService {

	private final static Logger log = LoggerFactory.getLogger(GraphService.class);
	private MinimalOntology ontology;
	private final TermRepository termRepository;

	@Property(name = "ontology") String ontologyName;

	public GraphService(TermRepository termRepository) {
		this.termRepository = termRepository;
	}

	public List<OntologyTerm> getParents(TermId termId) {
		List<TermId> termIdList = this.ontology.graph().getParentsStream(termId, false).collect(Collectors.toList());
		return this.termRepository.findByTermIdIn(termIdList).orElse(Collections.emptyList());
	}

	public List<OntologyTerm> getChildren(TermId termId) {
		List<TermId> termIdList = this.ontology.graph().getChildrenStream(termId, false).collect(Collectors.toList());
		return this.termRepository.findByTermIdIn(termIdList).orElse(Collections.emptyList());
	}

	public List<OntologyTerm> getAncestors(TermId termId) {
		List<TermId> termIdList = this.ontology.graph().getAncestorsStream(termId, false).collect(Collectors.toList());
		return this.termRepository.findByTermIdIn(termIdList).orElse(Collections.emptyList());
	}

	public MinimalOntology getOntology() {
		return ontology;
	}

	@PostConstruct
	public void initialize() {
		log.info("Loading ontology json..");
		File file = new File(String.format("data/%s-simple-non-classified.json", ontologyName));
		this.ontology = OntologyLoader.loadOntology(file);
		log.info("Finished loading ontology json..");
	}
}
