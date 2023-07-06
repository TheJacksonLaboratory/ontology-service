package org.jacksonlaboratory.model.entity;

import io.micronaut.serde.annotation.Serdeable;
import org.jacksonlaboratory.model.converter.TermIdAttributeConverter;
import org.monarchinitiative.phenol.ontology.data.Dbxref;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.monarchinitiative.phenol.ontology.data.TermSynonym;

import javax.persistence.*;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Entity
@Serdeable
@NamedNativeQuery(
		name="searchQuery",
		query = "SELECT ONTOLOGY_TERM.* FROM FTL_SEARCH_DATA(:param1, 0, 0) AS FT LEFT JOIN ONTOLOGY_TERM ON ONTOLOGY_TERM.uid = FT.KEYS[1];",
		resultClass = OntologyTerm.class)
public class OntologyTerm {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long uid;

	@Column(columnDefinition = "varchar")
	@Convert(converter = TermIdAttributeConverter.class)
	private TermId id;

	private String name;

	@Column(columnDefinition = "text")
	private String definition;
	@Column(columnDefinition = "text")
	private String comment;

	@Column(columnDefinition = "text")
	private String synonyms;

	private String xrefs;

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
		this.synonyms = term.getSynonyms().stream().filter(Predicate.not(TermSynonym::isObsoleteSynonym)).map(TermSynonym::getValue).collect(Collectors.joining(","));
		this.xrefs = term.getXrefs().stream().map(Dbxref::getName).collect(Collectors.joining(","));
	}

	public Long uid() {
		return uid;
	}

	public String id() {
		return id.toString();
	}

	public String name() {
		return name;
	}

	public String definition() {
		return definition;
	}

	public String comment() {
		return comment;
	}

	public List<String> synonyms() {
		return Arrays.asList(synonyms.split(","));
	}

	public List<String> xrefs() {
		return Arrays.asList(xrefs.split(","));
	}
}