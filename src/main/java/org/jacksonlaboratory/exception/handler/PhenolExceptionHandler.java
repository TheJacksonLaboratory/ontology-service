package org.jacksonlaboratory.exception.handler;

import io.micronaut.http.HttpResponse;

import io.micronaut.context.annotation.Requires;
import io.micronaut.http.HttpRequest;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.hateoas.JsonError;
import io.micronaut.http.server.exceptions.ExceptionHandler;
import io.micronaut.http.server.exceptions.response.ErrorContext;
import io.micronaut.http.server.exceptions.response.ErrorResponseProcessor;
import jakarta.inject.Singleton;
import org.monarchinitiative.phenol.base.PhenolRuntimeException;

@Produces
@Singleton
@Requires(classes = {PhenolRuntimeException.class, ExceptionHandler.class})
public class PhenolExceptionHandler implements ExceptionHandler<PhenolRuntimeException, HttpResponse> {

    private final ErrorResponseProcessor<?> errorResponseProcessor;

    public PhenolExceptionHandler(ErrorResponseProcessor<?> errorResponseProcessor) {
        this.errorResponseProcessor = errorResponseProcessor;
    }

    @Override
    public HttpResponse handle(HttpRequest request, PhenolRuntimeException exception) {

        return errorResponseProcessor.processResponse(ErrorContext.builder(request)
                .cause(exception)
                .errorMessage(exception.getMessage())
                .build(), HttpResponse.badRequest());
    }
}
