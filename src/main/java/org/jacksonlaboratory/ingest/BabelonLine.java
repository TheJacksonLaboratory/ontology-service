package org.jacksonlaboratory.ingest;

import org.jacksonlaboratory.model.Language;
import org.jacksonlaboratory.model.PredicateType;
import org.jacksonlaboratory.model.TranslationStatus;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.Objects;

public class BabelonLine {

	private final Language source_language;

	private final Language translation_language;

	private final TermId id;
	private final PredicateType type;

	private final String source_text;

	private final String translation_text;

	private final TranslationStatus status;


	public static BabelonLine of(Language source_language,
								 Language translation_language,
								 TermId id,
								 PredicateType type,
								 String source_text, String translation_text, TranslationStatus status){
		return new BabelonLine(source_language, translation_language, id, type, source_text, translation_text, status);
	}

	public static BabelonLine of(String line) throws RuntimeException {
		try {
			String[] fields = line.split("\t");
			Language source_language = Language.valueOf(fields[0].toUpperCase());
			Language translation_language = Language.valueOf(fields[1].toUpperCase());
			TermId id = TermId.of(fields[2]);
			PredicateType predicate_type = PredicateType.fromTermId(TermId.of(fields[3]));
			String source_text = fields[4];
			String translation_text = fields[5];
			TranslationStatus translationStatus = TranslationStatus.valueOf(fields[6].toUpperCase());
			return of(source_language, translation_language, id, predicate_type, source_text, translation_text, translationStatus);
		} catch (RuntimeException e){
			throw e;
		}
	}

	private BabelonLine(Language source_language, Language translation_language, TermId id, PredicateType type, String source, String translation, TranslationStatus status) {
		this.source_language = source_language;
		this.translation_language = translation_language;
		this.id = id;
		this.type = type;
		this.source_text = source;
		this.translation_text = translation;
		this.status = status;
	}

	public TermId id(){
		return id;
	}

	public Language source_language() {
		return source_language;
	}

	public Language translation_language() {
		return translation_language;
	}

	public PredicateType type() {
		return type;
	}

	public String source_text() {
		return source_text;
	}

	public String translation_text() {
		return translation_text;
	}

	public TranslationStatus status() {
		return status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		BabelonLine that = (BabelonLine) o;
		return source_language == that.source_language && translation_language == that.translation_language && Objects.equals(id, that.id) && type == that.type && Objects.equals(source_text, that.source_text) && Objects.equals(translation_text, that.translation_text) && status == that.status;
	}

	@Override
	public int hashCode() {
		return Objects.hash(source_language, translation_language, id, type, source_text, translation_text, status);
	}
}
