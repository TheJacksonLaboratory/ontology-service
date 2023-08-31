package org.jacksonlaboratory.service;

import io.micronaut.context.annotation.Value;
import io.micronaut.context.event.ApplicationEventListener;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.ingest.BabelonIngestor;
import org.jacksonlaboratory.ingest.BabelonLine;
import org.jacksonlaboratory.ingest.BabelonNavigator;
import org.jacksonlaboratory.ingest.TranslationProcessor;
import org.jacksonlaboratory.model.Language;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.Translation;
import org.jacksonlaboratory.repository.TermRepository;
import org.jacksonlaboratory.repository.TranslationRepository;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
			log.info("Loading ontology terms..");
			try {
				this.termRepository.configure();
				List<OntologyTerm> terms = graphService.getOntology().getTerms().stream().distinct().map(OntologyTerm::new).collect(Collectors.toList());
				this.termRepository.saveAll(terms);
				log.info("Finished loading ontology terms..");
				if (international){
					log.info("Internationalization enabled & loading..");
					BabelonNavigator navigator = BabelonIngestor.of().load(
							new File(String.format("data/%s-all.babelon.tsv", graphService.ontologyName)).toPath());
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
