package org.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import org.interview.domain.Tweet;
import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Stream;

public class TwitterGateway {
    private static final String CONSUMER_KEY = "vp8qXAMoZzy6jowJdtouPLUUb";
    private static final String CONSUMER_SECRET = "IMx3eIRfXXbRimoIz7cNpZCl0dr9dYEdRuDVTr2C4LdResXjN7";
    private static final String RESOURCE_URL = "https://stream.twitter.com/1.1/statuses/filter.json";

    private final TwitterAuthenticator authenticator;
    private final HttpRequestFactory requestFactory;

    private final ObjectMapper mapper;

    public TwitterGateway() throws TwitterAuthenticationException {
        authenticator = new TwitterAuthenticator(System.out, CONSUMER_KEY, CONSUMER_SECRET);

        requestFactory = authenticator.getAuthorizedHttpRequestFactory();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public Stream<String> filterRawTweetsByWord(String word) {
        try {
            HttpRequest request = requestFactory.buildGetRequest(
                    new GenericUrl(RESOURCE_URL.concat("?track=").concat(word)));

            request.setConnectTimeout(3 * 60000);  // 3 minutes connect timeout
            request.setReadTimeout(3 * 60000);  // 3 minutes read timeout

            HttpResponse response = request.execute();
            InputStream inputStream = response.getContent();
            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .filter(line -> line != null && !line.isEmpty());
        } catch (IOException e) {
            e.printStackTrace();
            return Stream.empty(); //?
        }
    }

    public Stream<Tweet> filterTweetsByWord(String word) {
        return filterRawTweetsByWord(word).map(this::mapToTweet);
    }

    private Tweet mapToTweet(String line) {
        try {
            return mapper.readValue(line, Tweet.class);
        } catch (IOException e) {
            throw new RuntimeException(String.format("can't parse line [%s] to tweet", line), e);
        }
    }
}
