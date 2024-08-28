package org.jacksonlaboratory.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.ingest.OntologyDataResolver;
import org.jacksonlaboratory.model.dto.SimpleOntologyTerm;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.OntologyTermBuilder;
import org.jacksonlaboratory.repository.TermRepository;
import org.jacksonlaboratory.repository.TranslationRepository;
import org.monarchinitiative.phenol.io.MinimalOntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Identified;
import org.monarchinitiative.phenol.ontology.data.MinimalOntology;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Stores a {@link MinimalOntology} and provides associated methods for traversal
 */
@Singleton
@Transactional
public class GraphService {

	private final static Logger log = LoggerFactory.getLogger(GraphService.class);
	private MinimalOntology ontology;
	private final TermRepository termRepository;
	private final TranslationRepository translationRepository;

	private OntologyDataResolver dataResolver;

	@Value("${ontology}") String ontologyName;
	@Value("${workingdir}") String directory;

	@Value("${international}")
	boolean international;

	public GraphService(TermRepository termRepository, TranslationRepository translationRepository) {
		this.termRepository = termRepository;
		this.translationRepository = translationRepository;
	}

	public Optional<TermId> getMostRecentTermId(TermId termId){
		Optional<Term> term = this.ontology.termForTermId(termId);
		return term.map(Identified::id);
	}


	public List<SimpleOntologyTerm> getParents(TermId termId) {
		Collection<TermId> termIdList = this.ontology.graph().extendWithParents(termId, false);
		return unpack(termIdList);
	}

	public List<SimpleOntologyTerm> getChildren(TermId termId) {
		Collection<TermId> termIdList = this.ontology.graph().extendWithChildren(termId, false);
		return unpack(termIdList);
	}

	public List<SimpleOntologyTerm> getDescendants(TermId termId){
		Collection<TermId> termIdList = this.ontology.graph().extendWithDescendants(termId, false);
		return unpack(termIdList);
	}

	public List<SimpleOntologyTerm> getAncestors(TermId termId){
		Collection<TermId> termIdList = this.ontology.graph().extendWithAncestors(termId, false);
		return unpack(termIdList);
	}

	List<SimpleOntologyTerm> unpack(Collection<TermId> termIdList){
		if (termIdList.isEmpty()){
			return Collections.emptyList();
		} else {
			Collection<OntologyTerm> termList = this.termRepository.findByTermIdIn(new ArrayList<>(termIdList));
			if (international){
				termList = addTranslations(termList);
			}
			return termList.stream().map(SimpleOntologyTerm::new).sorted(Comparator.comparing(SimpleOntologyTerm::getName)).collect(Collectors.toList());
		}
	}

	List<OntologyTerm> addTranslations(Collection<OntologyTerm> terms){
		return terms.stream().map(term ->
				new OntologyTermBuilder(term.getTermId(), term.getName()).extendFromOntologyTerm(term)
						.setTranslations(this.translationRepository.findAllByTerm(term)).createOntologyTerm()
		).collect(Collectors.toList());
	}

	@JsonIgnore
	public int getDescendantCount(TermId termId){
		return this.ontology.graph().extendWithDescendants(termId, false).size();
	}

	public MinimalOntology getOntology() {
		return ontology;
	}

	public OntologyDataResolver getDataResolver() {
		return dataResolver;
	}

	@PostConstruct
	public void initialize() {
		log.info(String.format("Loading %s ontology json..", ontologyName));
		this.dataResolver = OntologyDataResolver.of(Path.of(directory), this.ontologyName);
		File file = new File(dataResolver.ontologyJson().toUri());
		this.ontology = MinimalOntologyLoader.loadOntology(file, ontologyName.toUpperCase());
		log.info("Finished loading ontology json.");
	}
}
