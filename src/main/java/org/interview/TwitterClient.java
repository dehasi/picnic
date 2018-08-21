package org.interview;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.google.common.annotations.VisibleForTesting;
import org.interview.domain.Tweet;
import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterClient.class);

    private static final String CONSUMER_KEY = "vp8qXAMoZzy6jowJdtouPLUUb";
    private static final String CONSUMER_SECRET = "IMx3eIRfXXbRimoIz7cNpZCl0dr9dYEdRuDVTr2C4LdResXjN7";
    private static final String RESOURCE_URL = "https://stream.twitter.com/1.1/statuses/filter.json";
    private static final int CONNECT_TIMEOUT = 60000;
    private static final int READ_TIMEOUT = 2 * 60000;
    private static final long TIME_LIMIT_MS = 30L * 1000L;
    private static final int SIZE_LIMIT = 100;
    private static final int NUM_RETRIES = 3;


    private final TwitterAuthenticator authenticator;
    private final HttpRequestFactory requestFactory;

    private final ObjectMapper mapper;

    public TwitterClient() throws TwitterAuthenticationException {
        authenticator = new TwitterAuthenticator(System.out, CONSUMER_KEY, CONSUMER_SECRET);

        LOGGER.debug("Authorizing http client");
        requestFactory = authenticator.getAuthorizedHttpRequestFactory();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public InputStream filterRawTweetsByWord(String word) throws IOException {
        HttpRequest request = prepareRequest(word);

        LOGGER.info("Executing request: {}", request.getUrl());

        HttpResponse response = request.execute();

        LOGGER.debug("The request has finished with status: {}", response.getStatusCode());
        return response.getContent();
    }

    private HttpRequest prepareRequest(String word) throws IOException {
        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl(RESOURCE_URL.concat("?track=").concat(word))); //.concat("&count=100")

        request.setConnectTimeout(CONNECT_TIMEOUT);
        request.setReadTimeout(READ_TIMEOUT);
        request.setNumberOfRetries(NUM_RETRIES);
        return request;
    }

    public Stream<String> streamRawTweetsByWord(String word) {
        try {
            InputStream inputStream = filterRawTweetsByWord(word);
            return new BufferedReader(new InputStreamReader(inputStream))
                    .lines()
                    .filter(line -> line != null && !line.isEmpty());
        } catch (IOException e) {
            LOGGER.error("{Can't connect to twitter}",e);
            return Stream.empty();
        }
    }

    public Stream<Tweet> streamTweetsByWord(String word) {
//        return streamRawTweetsByWord(word).map(this::mapToTweet);

        try {
            return getTweetsByWordForLast(word).stream().map(this::mapToTweet);
        } catch (IOException e) {
            LOGGER.error("Error during request tweets", e);
            return Stream.empty();
        }
    }

    @VisibleForTesting
    Tweet mapToTweet(String line) {
        try {
            return mapper.readValue(line, Tweet.class);
        } catch (IOException e) {
            throw new RuntimeException(String.format("Can't parse line [%s] to tweet", line), e);
        }
    }

    public List<String> getTweetsByWordForLast(String word) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(filterRawTweetsByWord(word)), 6000 * 100);

        List<String> tweets = new ArrayList<>(SIZE_LIMIT);

        String line = reader.readLine();
        int tweetsCount = 0;
        long startTime = System.currentTimeMillis();
        LOGGER.info("Start to read incoming tweets");

        for (; line != null && tweetsCount < SIZE_LIMIT && (System.currentTimeMillis() - startTime < TIME_LIMIT_MS);
             line = reader.readLine().trim()) {
            if (line.isEmpty())
                continue;

            tweets.add(line);
            ++tweetsCount;
        }
        LOGGER.info("Extracted {} tweets for {} secs", tweetsCount, (System.currentTimeMillis() - startTime)/1000L );
        return tweets;
    }


}
