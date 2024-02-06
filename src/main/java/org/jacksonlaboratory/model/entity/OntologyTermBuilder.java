package org.jacksonlaboratory.model.entity;

import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.List;

public class OntologyTermBuilder {
	private TermId id;
	private String name;
	private String definition;
	private String comment;
	private String synonyms;
	private String xrefs;
	private int nDescendants;
	private List<Translation> translations;

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
		return new OntologyTerm(id, name, definition, comment, synonyms, xrefs, nDescendants, translations);
	}

	public OntologyTermBuilder fromOntologyTerm(OntologyTerm term) {
		this.id = term.getTermId();
		this.name = term.getName();
		this.definition = term.getDefinition();
		this.comment = term.getComment();
		this.synonyms = String.join(";", term.getSynonyms());
		this.xrefs = String.join(";", term.getXrefs());
		this.nDescendants = term.getDescendantCount();
		this.translations = term.getTranslations();
		return this;
	}
}
