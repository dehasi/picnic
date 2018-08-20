package org.interview.start;

import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.gson.Gson;
import org.interview.domain.Tweet;
import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Runner {

    private static final String CONSUMER_KEY = "vp8qXAMoZzy6jowJdtouPLUUb";
    private static final String CONSUMER_SECRET = "IMx3eIRfXXbRimoIz7cNpZCl0dr9dYEdRuDVTr2C4LdResXjN7";

    public static void main(String[] args) throws TwitterAuthenticationException, IOException {
        TwitterAuthenticator authenticator = new TwitterAuthenticator(System.out, CONSUMER_KEY, CONSUMER_SECRET);
        HttpRequestFactory requestFactory = authenticator.getAuthorizedHttpRequestFactory();

        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl("https://stream.twitter.com/1.1/statuses/filter.json".concat("?track=Bieber")));

        HttpResponse response = request.execute();
        InputStream inputStream = response.getContent();

        Gson gson = new Gson();

        /* java.io.UncheckedIOException: java.net.SocketTimeoutException: Read timed out*/

        new BufferedReader(new InputStreamReader(inputStream))
                .lines()
                .map(line -> gson.fromJson(line, Tweet.class))
                .limit(2)
                .forEach(System.out::println);
    }
}
