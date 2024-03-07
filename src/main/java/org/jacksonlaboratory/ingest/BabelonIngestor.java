package org.jacksonlaboratory.ingest;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.GZIPInputStream;


public interface BabelonIngestor {

	public static BabelonIngestor of() {
		return new BabelonIngestorDefault();
	}

	BabelonNavigator load(InputStream is) throws IOException;

	default BabelonNavigator load(Path path) throws IOException {
		try (InputStream is = openForReading(path)) {
			return load(is);}
	}

	private static BufferedInputStream openForReading(Path path) throws IOException {
		InputStream is = Files.newInputStream(path);
		if (path.toFile().getName().endsWith("gz"))
			is = new GZIPInputStream(is);
		return new BufferedInputStream(is);
	}
}
