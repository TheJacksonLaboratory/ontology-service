package org.jacksonlaboratory.model.entity;

import io.micronaut.serde.annotation.Serdeable;
import org.jacksonlaboratory.ingest.BabelonLine;
import org.jacksonlaboratory.model.Language;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Serdeable
public class Translation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;

	@ManyToOne(targetEntity = OntologyTerm.class)
	private Long term_uid;

	private Language language;

	private String name;

	private String definition;

	public Translation() {

	}

	public Translation(Long term_uid, Language language, String name, String definition) {
		this.term_uid = term_uid;
		this.language = language;
		this.name = name;
		this.definition = definition;
	}

	public Long getTerm_uid() {
		return term_uid;
	}

	public Language getLanguage() {
		return language;
	}

	public String getName() {
		return name;
	}

	public String getDefinition() {
		return definition;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Translation that = (Translation) o;
		return Objects.equals(uid, that.uid) && Objects.equals(term_uid, that.term_uid) && language == that.language && Objects.equals(name, that.name) && Objects.equals(definition, that.definition);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uid, term_uid, language, name, definition);
	}
}
