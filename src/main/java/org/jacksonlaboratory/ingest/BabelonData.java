package org.jacksonlaboratory.ingest;

import org.monarchinitiative.phenol.ontology.data.TermId;

import java.util.List;

/**
 *  A container of term and its babelon (translation) lines
 */
public class BabelonData {
	private final TermId id;
	private final List<BabelonLine> babelonLines;
	public BabelonData(TermId id, List<BabelonLine> babelonLines) {
		this.id = id;
		this.babelonLines = babelonLines;
	}

	public TermId id() {
		return id;
	}

	public List<BabelonLine> babelonLines() {
		return babelonLines;
	}
}
