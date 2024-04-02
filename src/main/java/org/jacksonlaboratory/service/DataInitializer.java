package org.jacksonlaboratory.service;

import io.micronaut.context.annotation.Value;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.ingest.*;
import org.jacksonlaboratory.model.Language;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.OntologyTermBuilder;
import org.jacksonlaboratory.model.entity.Translation;
import org.jacksonlaboratory.repository.TermRepository;
import org.jacksonlaboratory.repository.TranslationRepository;
import org.monarchinitiative.phenol.ontology.data.Dbxref;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.monarchinitiative.phenol.ontology.data.TermSynonym;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Singleton
public class DataInitializer {

	private final static Logger log = LoggerFactory.getLogger(DataInitializer.class);
	private final TermRepository termRepository;
	private final TranslationRepository translationRepository;
	private final GraphService graphService;

	@Value("${load}")
	boolean shouldLoad;

	@Value("${international}")
	boolean international;

	@Value("${workingdir}")
	String workingdir;

	public DataInitializer(TermRepository termRepository,
						   TranslationRepository translationRepository, GraphService graphService) {
		this.termRepository = termRepository;
		this.translationRepository = translationRepository;
		this.graphService = graphService;
	}

	@PostConstruct
	@Transactional
	public void init() {
		if(shouldLoad){
			log.info(String.format("Loading %s ontology terms..", graphService.ontologyName));
			try {
				this.termRepository.configure();
				List<OntologyTerm> terms = graphService.getOntology().getTerms().stream().distinct().map(term ->
					 new OntologyTermBuilder().setId(term.id()).setName(term.getName())
							.setDefinition(term.getDefinition()).setComment(term.getComment())
							.setSynonyms( term.getSynonyms().stream()
									.filter(Predicate.not(TermSynonym::isObsoleteSynonym)).map(TermSynonym::getValue).collect(Collectors.joining(";")))
							.setXrefs(term.getXrefs().stream().map(Dbxref::getName).collect(Collectors.joining(";")))
							.setDescendantCount(graphService.getDescendantCount(term.id())).createOntologyTerm()
				).collect(Collectors.toList());
				this.termRepository.saveAll(terms);
				log.info("Finished loading ontology terms..");
				if (international){
					log.info("Internationalization enabled & loading..");
					BabelonNavigator navigator = BabelonIngestor.of().load(graphService.getDataResolver().babelonFile());
					terms.forEach(t -> {
						List<Translation> translationsByTerm = new ArrayList<>();
						Optional<Map<Language, List<BabelonLine>>> linesByLanguage = navigator.getAggregatedLanguageLinesById(TermId.of(t.getId()));
						linesByLanguage.ifPresent(languageListMap -> languageListMap.forEach((key, value) -> {
							List<Translation> translation = TranslationProcessor.processTranslation(t, key, value);
							translationsByTerm.addAll(translation);
						}));
						this.translationRepository.saveAll(translationsByTerm);
					});
					log.info("Internationalization finished..");
				}
			} catch(IOException e){
				throw new RuntimeException(e);
			}
		} else {
			log.info("Skipping initializing data...");
		}
	}
}
