package org.jacksonlaboratory.service;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.Translation;
import org.jacksonlaboratory.repository.TermRepository;
import org.jacksonlaboratory.repository.TranslationRepository;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class TermService {
	private TermRepository termRepository;
	private TranslationRepository translationRepository;

	@Value("${international}")
	boolean international;

	public TermService(TermRepository termRepository, TranslationRepository translationRepository){
		this.termRepository = termRepository;
		this.translationRepository = translationRepository;
	}

	public Optional<OntologyTerm> getOntologyTermByTermId(TermId id){
		Optional<OntologyTerm> termOptional = this.termRepository.findByTermId(id);
		if(termOptional.isPresent()){
			OntologyTerm term = termOptional.get();
			if (international){
				List<Translation> translations = this.translationRepository.findAllByTerm(term);
				if (translations != null && translations.size() > 0){
					term.setTranslation(translations);
				}
				return Optional.of(term);
			}
			return termOptional;
		}
		return Optional.empty();
	}

	public List<OntologyTerm> searchOntologyTerm(String q){
		return this.termRepository.search(q).stream().sorted(Comparator.comparing((OntologyTerm term) -> !term.getName().startsWith(q))
						.thenComparing(OntologyTerm::getName)).collect(Collectors.toList());
	}
}
