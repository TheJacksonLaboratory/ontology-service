package org.jacksonlaboratory.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.micronaut.serde.annotation.Serdeable;
import org.jacksonlaboratory.model.converter.TermIdAttributeConverter;
import org.monarchinitiative.phenol.ontology.data.Dbxref;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.monarchinitiative.phenol.ontology.data.TermSynonym;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Entity
@NamedNativeQuery(
		name="searchQuery",
		query = "SELECT ONTOLOGY_TERM.* FROM FTL_SEARCH_DATA(:param1, 0, 0) AS FT LEFT JOIN ONTOLOGY_TERM ON ONTOLOGY_TERM.uid = FT.KEYS[1];",
		resultClass = OntologyTerm.class)
@Serdeable
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

	@Transient
	private List<Translation> translations;

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

	public String getId() {
		return id.toString();
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

	public List<String> getSynonyms() {
		if(this.synonyms.isBlank()){
			return Collections.emptyList();
		} else {
			return Arrays.asList(synonyms.split(","));
		}
	}

	public List<String> getXrefs() {
		if(xrefs.isBlank()){
			return Collections.emptyList();
		} else {
			return Arrays.asList(xrefs.split(","));
		}
	}

	@Transient
	@JsonGetter(value = "translations")
	public List<Translation> getTranslations() {
		return translations;
	}

	public void setTranslation(List<Translation> translations) {
		this.translations = translations;
	}
}