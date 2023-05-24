package org.jacksonlaboratory.model;

import io.micronaut.serde.annotation.Serdeable;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;


@Entity
@Serdeable
public class OntologyTerm {
	@Id
	private TermId id;

	private String name;

	@Column(columnDefinition = "text")
	private String definition;
	@Column(columnDefinition = "text")
	private String comment;

	public OntologyTerm() {

	}
	public OntologyTerm(TermId id, String name, String definition, String comment) {
		this.id = id;
		this.name = name;
		this.definition = definition;
		this.comment = comment;
	}

	public OntologyTerm(Term term) {
		this.id = term.id();
		this.name = term.getName();
		this.definition = term.getDefinition();
		this.comment = term.getComment();
	}

	public TermId getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getDefinition() {
		return definition;
	}

	public String getComment() {
		return comment;
	}

	public void setId(TermId id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDefinition(String definition) {
		this.definition = definition;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
}