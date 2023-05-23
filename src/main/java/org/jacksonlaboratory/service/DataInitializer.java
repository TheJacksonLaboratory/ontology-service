package org.jacksonlaboratory.service;

import io.micronaut.context.annotation.Value;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import io.micronaut.transaction.SynchronousTransactionManager;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.model.OntologyTerm;
import org.jacksonlaboratory.repository.TermRepository;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.InputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class DataInitializer implements ApplicationEventListener<ApplicationStartupEvent> {

	private final static Logger log = LoggerFactory.getLogger(DataInitializer.class);
	private final TermRepository termRepository;
	private final SynchronousTransactionManager<Connection> transactionManager;
	private final ClassPathResourceLoader loader;
	@Value("${load}")
	boolean shouldLoad;
	@Value("${ontology}")
	String ontology;

	DataInitializer(TermRepository termRepository, SynchronousTransactionManager<Connection> transactionManager) {
		this.termRepository = termRepository;
		this.loader = new ResourceResolver().getLoader(ClassPathResourceLoader.class).orElseThrow();
		this.transactionManager = transactionManager;
	}

	@Override
	public void onApplicationEvent(ApplicationStartupEvent event) {
		if(shouldLoad && !ontology.isBlank()){
			log.info("Initializing sample data...");
			Optional<InputStream> stream = loader.getResourceAsStream("classpath:${ontology}.json");
			if(stream.isPresent()){
				Ontology ontology = OntologyLoader.loadOntology(stream.get());
				List<OntologyTerm> terms = ontology.getTerms().stream().map(OntologyTerm::new).collect(Collectors.toList());
				// Build Terms
				transactionManager.executeWrite(status -> {
					this.termRepository.deleteAll();
					this.termRepository.saveAll(terms);
					return null;
				});
				//Build Parents
				//Build Children
			} else {
				throw new RuntimeException();
			}
		}
		log.info("Data initialization is done...");
	}
}
