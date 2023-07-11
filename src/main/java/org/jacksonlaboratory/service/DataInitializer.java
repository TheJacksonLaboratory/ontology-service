package org.jacksonlaboratory.service;

import io.micronaut.context.annotation.Value;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.runtime.event.ApplicationStartupEvent;
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
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class DataInitializer implements ApplicationEventListener<ApplicationStartupEvent> {

	private final static Logger log = LoggerFactory.getLogger(DataInitializer.class);
	private final TermRepository termRepository;
	private final TranslationRepository translationRepository;

	@Value("${load}")
	boolean shouldLoad;
	@Value("${ontology}")
	String ontology;
	@Value("${international}")
	boolean international;

	public DataInitializer(TermRepository termRepository,
						   TranslationRepository translationRepository) {
		this.termRepository = termRepository;
		this.translationRepository = translationRepository;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationStartupEvent event) {
		if(shouldLoad && !ontology.isBlank()){
			log.info("Initializing ontology data..");
			File file = new File(String.format("data/%s-simple-non-classified.json", ontology));
			if (file.exists()){
				try {
					Ontology ontology = OntologyLoader.loadOntology(file);
					this.termRepository.create_index();
					List<OntologyTerm> terms = ontology.getTerms().stream().distinct().map(OntologyTerm::new).collect(Collectors.toList());
					this.termRepository.saveAll(terms);
					log.info("Finished loading ontology data..");
					if (international){
						log.info("Internationalization enabled..");
						log.info("Loading Translations..");
						BabelonNavigator navigator = BabelonIngestor.of().load(new File("data/hp-all.babelon.tsv").toPath());
						terms.forEach(t -> {
							List<Translation> translationsByTerm = new ArrayList<>();
							Optional<Map<Language, List<BabelonLine>>> linesByLanguage = navigator.getAggregatedLanguageLinesById(t.getTermId());
							linesByLanguage.ifPresent(languageListMap -> languageListMap.forEach((key, value) -> {
								List<Translation> translation = TranslationProcessor.processTranslation(t, key, value);
								translationsByTerm.addAll(translation);
							}));
							this.translationRepository.saveAll(translationsByTerm);
						});
						log.info("Translations finished..");
					}
				} catch(IOException e){
					throw new RuntimeException(e);
				}
			} else {
				throw new RuntimeException();
			}

		} else {
			log.info("Skipping initializing data...");
		}
	}
}
