package org.jacksonlaboratory.ingest;

import org.jacksonlaboratory.model.Language;
import org.jacksonlaboratory.model.PredicateType;
import org.jacksonlaboratory.model.TranslationStatus;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.Translation;
import java.util.List;

public class TranslationProcessor {

	/**
	 * Process a language translation for a term. Multiple lines combine to form one translation.
	 * @param language - the language being translated
	 * @param lines - the lines relative to the id and translation language
	 * @return a translation entity
	 */
	public static Translation processTranslation(OntologyTerm term, Language language, List<BabelonLine> lines){
		// We expect a size of 1 or 2. At least the label and sometimes definition being translated.
		if (lines.size() > 2 || lines.size() == 0){
			throw new RuntimeException();
		}
		String label = "";
		String definition = "";
		TranslationStatus status = TranslationStatus.CANDIDATE;
		for (BabelonLine line : lines) {
			if(line.type().equals(PredicateType.LABEL)){
				label = line.translation_text();
				if (!line.status().equals(status)){
					status = line.status();
				}
			} else if(line.type().equals(PredicateType.DEFINITION)){
				definition = line.translation_text();
			}
		}
		return new Translation(term.uid(), language, label, definition, status);
	}
}
