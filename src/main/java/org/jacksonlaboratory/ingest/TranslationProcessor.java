package org.jacksonlaboratory.ingest;

import org.jacksonlaboratory.model.Language;
import org.jacksonlaboratory.model.PredicateType;
import org.jacksonlaboratory.model.TranslationStatus;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.Translation;

import java.util.*;
import java.util.stream.Collectors;

public class TranslationProcessor {

	/**
	 * Process a language translation for a term. We filter for a single definition line
	 * @param language - the language being translated
	 * @param lines - the lines relative to the id and translation language
	 * @return a translation entity
	 */
	public static List<Translation> processTranslation(OntologyTerm term, Language language, List<BabelonLine> lines){
		// We expect a size of 1 or 2. At least the label and sometimes definition being translated.
		if (lines.size() == 0){
			throw new RuntimeException();
		}

		// Build lines to map for a term
		// then process
		List<Translation> translations = new ArrayList<>();
		Optional<BabelonLine> definitionLine = lines.stream().distinct().filter(line -> line.type().equals(PredicateType.DEFINITION)).findFirst();
		String definition = "";
		if(definitionLine.isPresent()){
			definition = definitionLine.get().translation_text();
		}
		TranslationStatus status = TranslationStatus.CANDIDATE;
		for (BabelonLine line : lines.stream().filter(line -> line.type().equals(PredicateType.LABEL)).distinct().collect(Collectors.toList())) {
				String label = line.translation_text();
				if (!line.status().equals(status)){
					status = line.status();
				}
				translations.add( new Translation(term, language, label, definition, status));
		}
		return translations;
	}
}
