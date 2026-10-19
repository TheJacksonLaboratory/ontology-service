package org.jacksonlaboratory.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import org.jacksonlaboratory.model.entity.OntologyTerm;
import org.jacksonlaboratory.model.entity.Translation;

import java.util.List;
import java.util.Objects;
@Serdeable
public class SimpleOntologyTerm {

	private final String id;
	private final String name;

	private final int nDescendant;

	private final List<Translation> translations;

	private final List<String> parents;

	public SimpleOntologyTerm(OntologyTerm ontologyTerm) {
		this(ontologyTerm, null);
	}

	/**
	 * Create a term that carries the ids of its immediate parents
	 * @param ontologyTerm The term to copy
	 * @param parents Ids of the immediate parents, or null when they were not requested
	 */
	public SimpleOntologyTerm(OntologyTerm ontologyTerm, List<String> parents) {
		this.id = ontologyTerm.getId();
		this.name = ontologyTerm.getName();
		this.translations = ontologyTerm.getTranslations();
		this.nDescendant = ontologyTerm.getDescendantCount();
		this.parents = parents;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public List<Translation> getTranslations() {
		return translations;
	}

	public int getDescendantCount() {
		return nDescendant;
	}

	@JsonInclude(JsonInclude.Include.NON_NULL)
	@Schema(nullable = true, description = "Ids of the immediate parents, present only on the ancestors route")
	public List<String> getParents() {
		return parents;
	}

	@Override
	public String toString() {
		return "SimpleOntologyTerm{" +
				"id='" + id + '\'' +
				", name='" + name + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		SimpleOntologyTerm that = (SimpleOntologyTerm) o;
		return Objects.equals(id, that.id) && Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}
}
