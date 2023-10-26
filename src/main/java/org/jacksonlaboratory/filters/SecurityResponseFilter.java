package org.jacksonlaboratory.filters;

import io.micronaut.core.async.publisher.Publishers;
import io.micronaut.http.*;
import io.micronaut.http.annotation.Filter;
import io.micronaut.http.filter.HttpServerFilter;
import io.micronaut.http.filter.ServerFilterChain;
import org.reactivestreams.Publisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Filter("/**")
public class SecurityResponseFilter implements HttpServerFilter {

	private static final Logger logger = LoggerFactory.getLogger(SecurityResponseFilter.class);

	@Override
	public Publisher<MutableHttpResponse<?>> doFilter(HttpRequest<?> request, ServerFilterChain chain) {
		logger.debug("Adding X-Frame Options");
		return Publishers.map(chain.proceed(request), mutableHttpResponse -> mutableHttpResponse.header("X-Frame-Options", "DENY")
				.header("X-Content-Type-Options", "nosniff").header("Strict-Transport-Security", "max-age=31536000"));
	}


}
