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

	public List<OntologyTerm> getAllOntologyTerms(List<TermId> termIdList){
		if (termIdList.isEmpty()){
			return this.termRepository.findAll();
		}
		return this.termRepository.findByTermIdIn(termIdList);
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
			// Bring synonym matches to the top
			boolean  aStarts = a.getSynonyms().stream().anyMatch(s -> s.toLowerCase().startsWith(q.toLowerCase()));
			boolean  bStarts = b.getSynonyms().stream().anyMatch(s -> s.toLowerCase().startsWith(q.toLowerCase()));

			if (aStarts && !bStarts) {
				return -1;
			} else if (!aStarts && bStarts) {
				return 1;
			}
			return 0;
		}).sorted((a, b) -> {
			// Bring synonym matches to the top
			boolean  aStarts = a.getName().toLowerCase().contains(q.toLowerCase());
			boolean  bStarts = b.getName().toLowerCase().contains(q.toLowerCase());

			if (aStarts && !bStarts) {
				return -1;
			} else if (!aStarts && bStarts) {
				return 1;
			}
			return 0;
		}).sorted((a, b) -> {
			// Then bring synonym matches to the top
			boolean  aStarts = a.getName().toLowerCase().startsWith(q.toLowerCase());
			boolean  bStarts = b.getName().toLowerCase().startsWith(q.toLowerCase());

			if (aStarts && !bStarts) {
				return -1;
			} else if (!aStarts && bStarts) {
				return 1;
			}
			return 0;
		}).sorted((a, b) -> {
			// Then bring exact matches to the top
			boolean  aStarts = a.getName().toLowerCase().equalsIgnoreCase(q);
			boolean  bStarts = b.getName().toLowerCase().equalsIgnoreCase(q);

			if (aStarts && !bStarts) {
				return -1;
			} else if (!aStarts && bStarts) {
				return 1;
			}
			return 0;
		}).collect(Collectors.toList());
	}
}
