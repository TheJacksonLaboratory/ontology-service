package org.jacksonlaboratory.service;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.context.annotation.Property;
import io.micronaut.context.annotation.Requires;
import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.ingest.OntologyDataResolver;
import org.jacksonlaboratory.model.dto.SimpleOntologyTerm;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.OntologyTermBuilder;
import org.jacksonlaboratory.repository.TermRepository;
import org.jacksonlaboratory.repository.TranslationRepository;
import org.monarchinitiative.phenol.io.MinimalOntologyLoader;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.MinimalOntology;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.nio.file.Path;
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
	private final TranslationRepository translationRepository;

	private OntologyDataResolver dataResolver;

	@Property(name = "ontology") String ontologyName;
	@Property(name = "workingdir") String directory;

	@Value("${international}")
	boolean international;

	public GraphService(TermRepository termRepository, TranslationRepository translationRepository) {
		this.termRepository = termRepository;
		this.translationRepository = translationRepository;
	}

	public List<SimpleOntologyTerm> getParents(TermId termId) {
		List<TermId> termIdList = this.ontology.graph().getParentsStream(termId, false).collect(Collectors.toList());
		return unpack(termIdList);
	}

	public List<SimpleOntologyTerm> getChildren(TermId termId) {
		List<TermId> termIdList = this.ontology.graph().getChildrenStream(termId, false).collect(Collectors.toList());
		return unpack(termIdList);
	}

	public List<SimpleOntologyTerm> getDescendants(TermId termId){
		List<TermId> termIdList = this.ontology.graph().getDescendantsStream(termId, false).collect(Collectors.toList());
		return unpack(termIdList);
	}

	List<SimpleOntologyTerm> unpack(List<TermId> termIdList){
		if (termIdList.isEmpty()){
			return Collections.emptyList();
		} else {
			List<OntologyTerm> termList = this.termRepository.findByTermIdIn(termIdList).orElse(Collections.emptyList());
			if (international){
				termList = addTranslations(termList);
			}
			return termList.stream().map(SimpleOntologyTerm::new).collect(Collectors.toList());
		}
	}

	List<OntologyTerm> addTranslations(List<OntologyTerm> terms){
		return terms.stream().map(term ->
				new OntologyTermBuilder().fromOntologyTerm(term)
						.setTranslations(this.translationRepository.findAllByTerm(term)).createOntologyTerm()
		).collect(Collectors.toList());
	}

	@JsonIgnore
	public int getDescendantCount(TermId termId){
		return (int) this.ontology.graph().getDescendantsStream(termId, false).count();
	}

	public MinimalOntology getOntology() {
		return ontology;
	}

	public OntologyDataResolver getDataResolver() {
		return dataResolver;
	}

	@PostConstruct
	public void initialize() {
		log.info("Loading ontology json..");
		this.dataResolver = OntologyDataResolver.of(Path.of(directory), this.ontologyName);
		File file = new File(dataResolver.ontologyJson().toUri());
		this.ontology = MinimalOntologyLoader.loadOntology(file, ontologyName.toUpperCase());
		log.info("Finished loading ontology json.");
	}
}
