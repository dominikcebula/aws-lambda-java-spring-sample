package com.dominikcebula.samples.aws.serverless.product.catalog.shared.http.error;

public record ErrorDTO(Error error) {
    public record Error(String message) {
    }

    public static ErrorDTO errorWithMessage(String message) {
        return new ErrorDTO(new Error(message));
    }
}
