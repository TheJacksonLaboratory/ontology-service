package org.jacksonlaboratory.model.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonView;
import io.micronaut.core.annotation.Creator;
import io.micronaut.serde.annotation.Serdeable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import jakarta.persistence.*;
import org.jacksonlaboratory.model.converter.TermIdAttributeConverter;
import org.monarchinitiative.phenol.ontology.data.Dbxref;
import org.monarchinitiative.phenol.ontology.data.Term;
import org.monarchinitiative.phenol.ontology.data.TermId;
import org.monarchinitiative.phenol.ontology.data.TermSynonym;
import org.jacksonlaboratory.view.Views;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@NamedNativeQuery(
		name="searchQuery",
		query = "SELECT ONTOLOGY_TERM.* FROM FTL_SEARCH_DATA(:param1, 0, 0) AS FT LEFT JOIN ONTOLOGY_TERM ON ONTOLOGY_TERM.uid = FT.KEYS[1];",
		resultClass = OntologyTerm.class)
@Serdeable
@Entity
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
	@Column(columnDefinition = "text")
	private String xrefs;

	@Transient
	private List<Translation> translations;

	@Creator
	public OntologyTerm(TermId id, String name, String definition, String comment) {
		this.id = id;
		this.name = name;
		this.definition = definition;
		this.comment = comment;
		this.synonyms = null;
		this.xrefs = null;
	}

	public OntologyTerm(Term term) {
		this.id = term.id();
		this.name = term.getName();
		this.definition = term.getDefinition();
		this.comment = term.getComment();
		this.synonyms = term.getSynonyms().stream().filter(Predicate.not(TermSynonym::isObsoleteSynonym)).map(TermSynonym::getValue).collect(Collectors.joining(";"));
		this.xrefs = term.getXrefs().stream().map(Dbxref::getName).collect(Collectors.joining(";"));
	}

	public OntologyTerm() {

	}

	public Long uid() {
		return uid;
	}

	@Schema(maxLength = 10, type = "string", pattern = ".*")
	public String getId() {
		return id.toString();
	}

	@Schema(maxLength = 255, type = "string", pattern = ".*")
	public String getName() {
		return name;
	}

	@Schema(maxLength = 1000, type = "string", pattern = ".*")
	public String getDefinition() {
		return definition;
	}

	@Schema(maxLength = 1000, type = "string", pattern = ".*")
	public String getComment() {
		return comment;
	}

	@ArraySchema(maxItems = 25)
	public List<String> getSynonyms() {
		if(this.synonyms == null || this.synonyms.isBlank()){
			return Collections.emptyList();
		} else {
			return Arrays.asList(synonyms.split(";"));
		}
	}

	@ArraySchema(maxItems = 25)
	public List<String> getXrefs() {
		if(xrefs == null || xrefs.isBlank()){
			return Collections.emptyList();
		} else {
			return Arrays.asList(xrefs.split(";"));
		}
	}

	@ArraySchema(maxItems = 25)
	@Transient
	public List<Translation> getTranslations() {
		return translations;
	}

	public void setTranslation(List<Translation> translations) {
		this.translations = translations;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		OntologyTerm that = (OntologyTerm) o;
		return Objects.equals(uid, that.uid) && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(definition, that.definition) && Objects.equals(comment, that.comment) && Objects.equals(synonyms, that.synonyms) && Objects.equals(xrefs, that.xrefs) && Objects.equals(translations, that.translations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(uid, id, name, definition, comment, synonyms, xrefs, translations);
	}
}
