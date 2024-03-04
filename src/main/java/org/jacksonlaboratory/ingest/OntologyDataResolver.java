package org.jacksonlaboratory.ingest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

public class OntologyDataResolver {
		private static final Logger LOGGER = LoggerFactory.getLogger(OntologyDataResolver.class);

		private final Path dataDirectory;
		private final String ontology;

		public static OntologyDataResolver of(Path dataDirectory, String ontology) throws RuntimeException {
			return new OntologyDataResolver(dataDirectory, ontology);
		}

		private OntologyDataResolver(Path dataDirectory, String ontology) throws RuntimeException {
			Objects.requireNonNull(dataDirectory, "Data directory must not be null!");
			Objects.requireNonNull(ontology, "Ontology must be defined.");
			this.dataDirectory = dataDirectory;
			this.ontology = ontology;
			validateHpoFiles();
		}

		private void validateHpoFiles() throws RuntimeException  {
			boolean error = false;
			List<Path> requiredFiles = List.of(ontologyJson());
			for (Path file : requiredFiles) {
				if (!Files.isRegularFile(file)) {
					LOGGER.error("Missing required file `{}` in `{}`.", file.toFile().getName(), dataDirectory.toAbsolutePath());
					error = true;
				}
			}
			if (error) {
				throw new RuntimeException("Missing one or more required files in OntologyAnnotationNetwork data directory!");
			}
		}


		public Path dataDirectory() {
			return dataDirectory;
		}

		public Path ontologyJson(){
			return dataDirectory.resolve(String.format("%s-base.json", ontology));
		}

		public Path babelonFile(){
		return dataDirectory.resolve(String.format("%s-all.babelon.tsv", ontology));
	}
}

