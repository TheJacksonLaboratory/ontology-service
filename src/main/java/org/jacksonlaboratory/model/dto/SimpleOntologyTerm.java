package org.jacksonlaboratory.model.dto;

import io.micronaut.serde.annotation.Serdeable;
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

	public SimpleOntologyTerm(OntologyTerm ontologyTerm) {
		this.id = ontologyTerm.getId();
		this.name = ontologyTerm.getName();
		this.translations = ontologyTerm.getTranslations();
		this.nDescendant = ontologyTerm.getDescendantCount();
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
