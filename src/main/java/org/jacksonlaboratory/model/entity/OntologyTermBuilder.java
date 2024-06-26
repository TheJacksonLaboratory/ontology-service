package org.jacksonlaboratory.model.entity;

import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.List;
import java.util.Objects;

public class OntologyTermBuilder {
	private TermId id;
	private String name;
	private String definition;
	private String comment;
	private String synonyms;
	private String xrefs;
	private int nDescendants;
	private List<Translation> translations;

	public OntologyTermBuilder(TermId id, String name) {
		this.id = id;
		this.name = name;
	}

	public OntologyTermBuilder setId(TermId id) {
		this.id = id;
		return this;
	}

	public OntologyTermBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public OntologyTermBuilder setDefinition(String definition) {
		this.definition = definition;
		return this;
	}

	public OntologyTermBuilder setComment(String comment) {
		this.comment = comment;
		return this;
	}

	public OntologyTermBuilder setSynonyms(String synonyms) {
		this.synonyms = synonyms;
		return this;
	}

	public OntologyTermBuilder setXrefs(String xrefs) {
		this.xrefs = xrefs;
		return this;
	}

	public OntologyTermBuilder setDescendantCount(int nDescendants) {
		this.nDescendants = nDescendants;
		return this;
	}

	public OntologyTermBuilder setTranslations(List<Translation> translations) {
		this.translations = translations;
		return this;
	}

	public OntologyTerm createOntologyTerm() {
		Objects.requireNonNull(id, "id for ontology term cannot be null!");
		Objects.requireNonNull(name, "name for ontology term cannot be null!");
		return new OntologyTerm(id, name, definition, comment, synonyms, xrefs, nDescendants, translations);
	}

	public OntologyTermBuilder extendFromOntologyTerm(OntologyTerm term) {
		this.definition = term.getDefinition();
		this.comment = term.getComment();
		this.synonyms = String.join(";", term.getSynonyms());
		this.xrefs = String.join(";", term.getXrefs());
		this.nDescendants = term.getDescendantCount();
		this.translations = term.getTranslations();
		return this;
	}
}
