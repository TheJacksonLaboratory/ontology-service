package org.jacksonlaboratory.ingest;

import org.monarchinitiative.phenol.ontology.data.TermId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BabelonIngestorDefault implements BabelonIngestor {
	private static final Logger LOGGER = LoggerFactory.getLogger(BabelonIngestorDefault.class);

	@Override
	public BabelonNavigator load(InputStream is) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
		reader.readLine();
		Map<TermId, List<BabelonLine>> translationsByTermId = new HashMap<>();
		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			BabelonLine babelonLine;
			try {
				babelonLine = BabelonLine.of(line);
				translationsByTermId.computeIfAbsent(babelonLine.id(), k -> new ArrayList<>())
						.add(babelonLine);
			} catch (RuntimeException e) {
				LOGGER.warn("Error {} while parsing line: {}", e.getMessage(), line);
			}
		}
		List<BabelonData> babelonData = translationsByTermId.entrySet().stream().map(processAnnotationEntry()).collect(Collectors.toList());
		return new BabelonNavigator(babelonData);
	}

	private static Function<Map.Entry<TermId, List<BabelonLine>>, BabelonData> processAnnotationEntry() {
		return e -> new BabelonData(e.getKey(), e.getValue());
	}

}

