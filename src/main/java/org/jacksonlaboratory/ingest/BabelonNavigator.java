package org.jacksonlaboratory.ingest;

import org.jacksonlaboratory.model.Language;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.*;
import java.util.stream.Collectors;

public class BabelonNavigator {

	private final List<BabelonData> babelonData;

	public BabelonNavigator(List<BabelonData> babelonData) {
		this.babelonData = babelonData;
	}

	public Optional<Map<Language, List<BabelonLine>>> getAggregatedLanguageLinesById(TermId id) {
		Optional<BabelonData> targetData =  babelonData.stream().filter(data -> data.id().equals(id)).findFirst();
		return targetData.map(data -> data.babelonLines().stream().collect(Collectors.groupingBy(BabelonLine::translation_language)));
	}
}
