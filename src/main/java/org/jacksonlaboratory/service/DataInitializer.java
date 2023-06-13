package org.jacksonlaboratory.service;

import io.micronaut.context.annotation.Value;
import io.micronaut.context.event.ApplicationEventListener;
import io.micronaut.core.io.ResourceResolver;
import io.micronaut.core.io.scan.ClassPathResourceLoader;
import io.micronaut.runtime.event.ApplicationStartupEvent;
import io.micronaut.transaction.SynchronousTransactionManager;
import io.micronaut.transaction.TransactionDefinition;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.model.OntologyTerm;
import org.jacksonlaboratory.repository.TermRepository;
import org.monarchinitiative.phenol.io.OntologyLoader;
import org.monarchinitiative.phenol.ontology.data.Ontology;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.Connection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class DataInitializer implements ApplicationEventListener<ApplicationStartupEvent> {

	private final static Logger log = LoggerFactory.getLogger(DataInitializer.class);
	private final SynchronousTransactionManager<Connection> transactionManager;
	private final ClassPathResourceLoader loader;
	private final TermRepository termRepository;
	@Value("${load}")
	boolean shouldLoad;
	@Value("${ontology}")
	String ontology;

	public DataInitializer(SynchronousTransactionManager<Connection> transactionManager, TermRepository termRepository) {
		this.loader = new ResourceResolver().getLoader(ClassPathResourceLoader.class).orElseThrow();
		this.transactionManager = transactionManager;
		this.termRepository = termRepository;
	}

	@Override
	@Transactional
	public void onApplicationEvent(ApplicationStartupEvent event) {
		if(shouldLoad && !ontology.isBlank()){
			log.info("Initializing sample data...");
				File file = new File(String.format("/Users/gargam/Develop/ontology-service/data/%s-simple-non-classified.json", ontology));
				if (file.exists()){
					Ontology ontology = OntologyLoader.loadOntology(file);
					this.termRepository.configure();
					List<OntologyTerm> terms = ontology.getTerms().stream().distinct().map(OntologyTerm::new).collect(Collectors.toList());
					this.termRepository.saveAll(terms);
				} else {
					throw new RuntimeException();
				}

		} else {
			log.info("Skipping initializing data...");
		}
	}
}
