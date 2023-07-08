package pl.joboffers.infrastructure.security.jwt.offer.http;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import pl.joboffers.domain.offer.OfferFetchable;

import java.io.IOException;
import java.io.Serial;
import java.time.Duration;

import static org.springframework.http.HttpStatus.*;

public class RestTemplateResponseErrorHandler extends DefaultResponseErrorHandler {
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        final HttpStatus statusCode = httpResponse.getStatusCode();
        final Series series = statusCode.series();
        if (series == Series.SERVER_ERROR) {
            throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "Error while using http client");
        } else if (series == Series.CLIENT_ERROR) {
            if (statusCode == NOT_FOUND) {
                throw new ResponseStatusException(NOT_FOUND);
            } else if (statusCode == UNAUTHORIZED) {
                throw new ResponseStatusException(UNAUTHORIZED);
            }
        }
    }
}