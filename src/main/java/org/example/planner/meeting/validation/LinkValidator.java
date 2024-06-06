package org.example.planner.meeting.validation;

import jakarta.annotation.PostConstruct;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.validator.routines.UrlValidator;
import org.example.planner.meeting.exception.InvalidLinkException;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class LinkValidator {

    public final String[] schemes = {"http", "https"};
    private final UrlValidator urlValidator = new UrlValidator(schemes);

    private OkHttpClient httpClient;

    @PostConstruct
    public void init() {
        httpClient = new OkHttpClient().newBuilder()
                .followRedirects(false)
                .followSslRedirects(false)
                .build();
    }

    public void validateLink(String link) {
        if (!urlValidator.isValid(link) || !isConnectionSuccessful(link)) {
            throw new InvalidLinkException();
        }
    }

    private boolean isConnectionSuccessful(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = httpClient.newCall(request).execute()) {
            return response.isSuccessful() || response.isRedirect();
        } catch (IOException ex) {
            return false;
        }
    }
}
