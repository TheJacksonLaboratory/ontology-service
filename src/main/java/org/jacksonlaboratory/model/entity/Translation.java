package org.jacksonlaboratory.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.micronaut.serde.annotation.Serdeable;
import org.jacksonlaboratory.model.Language;
import org.jacksonlaboratory.model.TranslationStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Serdeable
public class Translation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@JsonIgnore
	private Long id;

	@ManyToOne(targetEntity = OntologyTerm.class)
	@JsonIgnore
	private OntologyTerm term;

	private Language language;

	private String name;

	@Column(columnDefinition = "text")
	private String definition;

	private TranslationStatus status;

	public Translation() {

	}

	public Translation(OntologyTerm term, Language language, String name, String definition, TranslationStatus status) {
		this.term = term;
		this.language = language;
		this.name = name;
		this.definition = definition;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setTerm(OntologyTerm term) {
		this.term = term;
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

	public OntologyTerm getTerm() {
		return term;
	}

	public Language getLanguage() {
		return language;
	}

	@Schema(maxLength = 255, type = "string", pattern = ".*")
	public String getName() {
		return name;
	}

	@Schema(maxLength = 1000, type = "string", pattern = ".*")
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
		return Objects.equals(id, that.id) && Objects.equals(term, that.term) && language == that.language && Objects.equals(name, that.name) && Objects.equals(definition, that.definition);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, term, language, name, definition);
	}
}
