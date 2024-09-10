package com.dominikcebula.samples.aws.serverless.product.catalog.shared.http.error;

import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.dominikcebula.samples.aws.serverless.product.catalog.shared.json.JsonConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.assertj.core.api.Assertions.assertThat;

@Component
@RequiredArgsConstructor
public class ErrorDTOAssertions {
    @Autowired
    private JsonConverter jsonConverter;

    public void assertErrorMessageInResponseBody(APIGatewayProxyResponseEvent response, String expectedErrorMessage) {
        ErrorDTO errorDTO = jsonConverter.fromJson(response.getBody(), ErrorDTO.class);

        assertThat(errorDTO.error().message()).isEqualTo(expectedErrorMessage);
    }
}