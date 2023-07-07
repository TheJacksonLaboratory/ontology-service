package org.jacksonlaboratory.model.entity;

import io.micronaut.serde.annotation.Serdeable;
import org.jacksonlaboratory.model.Language;
import org.jacksonlaboratory.model.TranslationStatus;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Serdeable
public class Translation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private UUID id;

	@ManyToOne(targetEntity = OntologyTerm.class)
	private Long term_uuid;

	private Language language;

	private String name;

	private String definition;

	private TranslationStatus status;

	public Translation() {

	}

	public Translation(Long term_uuid, Language language, String name, String definition, TranslationStatus status) {
		this.term_uuid = term_uuid;
		this.language = language;
		this.name = name;
		this.definition = definition;
		this.status = status;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID uuid) {
		this.id = uuid;
	}

	public void setTerm_uuid(Long term_uuid) {
		this.term_uuid = term_uuid;
	}

	public void setLanguage(Language language) {
		this.language = language;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public Long getTerm_uuid() {
		return term_uuid;
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

	public TranslationStatus getStatus() {
		return status;
	}

	public void setStatus(TranslationStatus status) {
		this.status = status;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Translation that = (Translation) o;
		return Objects.equals(id, that.id) && Objects.equals(term_uuid, that.term_uuid) && language == that.language && Objects.equals(name, that.name) && Objects.equals(definition, that.definition);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, term_uuid, language, name, definition);
	}
}
