package org.jacksonlaboratory.model.converter;

import io.micronaut.core.type.Argument;
import io.micronaut.serde.Encoder;
import io.micronaut.serde.Serializer;
import jakarta.inject.Singleton;
import org.monarchinitiative.phenol.ontology.data.TermId;

import java.io.IOException;
import java.util.Objects;

@Singleton
public class TermIdSerializer implements Serializer<TermId> {
	@Override
	public void serialize(Encoder encoder, EncoderContext context, Argument<? extends TermId> type, TermId value) throws IOException {
		Objects.requireNonNull(value, "TermId cannot be null.");
		encoder.encodeString(value.toString());
	}
}
