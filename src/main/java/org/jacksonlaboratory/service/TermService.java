package org.jacksonlaboratory.service;

import io.micronaut.context.annotation.Value;
import jakarta.inject.Singleton;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.OntologyTermBuilder;
import org.jacksonlaboratory.model.entity.Translation;
import org.jacksonlaboratory.repository.TermRepository;
import org.jacksonlaboratory.repository.TranslationRepository;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class TermService {
	private TermRepository termRepository;
	private TranslationRepository translationRepository;

	@Value("${international}")
	boolean international;

	@Value("${ontology}")
	String ontologyName;

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
				if (translations != null && !translations.isEmpty()){
					return Optional.of(new OntologyTermBuilder(term.getTermId(), term.getName()).extendFromOntologyTerm(term)
							.setTranslations(translations).createOntologyTerm());
				}
				return Optional.of(term);
			}
			return termOptional;
		}
		return Optional.empty();
	}

	public List<OntologyTerm> getAllOntologyTerms(){
		return this.termRepository.findAll();
	}

	public List<OntologyTerm> searchOntologyTerm(String q){
		if (q.toUpperCase().startsWith(String.format("%s:", ontologyName.toUpperCase()))){
			return this.termRepository.search(q, true).stream()
					.sorted((a, b) -> {
						int c = Integer.parseInt(a.getTermId().getId());
						int d = Integer.parseInt(b.getTermId().getId());
						return Integer.compare(c, d);
					}).collect(Collectors.toList());
		}
		return this.termRepository.search(q, false).stream().sorted((a, b) -> {
				boolean  aStarts = a.getName().toLowerCase().startsWith(q);
				boolean  bStarts = b.getName().toLowerCase().startsWith(q);

				// Sort objects based on whether the name starts with the prefix
				if (aStarts && !bStarts) {
					return -1; // o1 comes before o2
				} else if (!aStarts && bStarts) {
					return 1; // o2 comes before o1
				} else {
					boolean  aContains = a.getName().toLowerCase().contains(q);
					boolean  bContains = b.getName().toLowerCase().contains(q);
					return aContains && !bContains ? -1 : !aContains && bContains ? 1 : 0;
				}
			}).collect(Collectors.toList());
	}
}
