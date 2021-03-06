package org.interview.client;

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
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TwitterClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterClient.class);

    private static final String RESOURCE_URL = "https://stream.twitter.com/1.1/statuses/filter.json";
    private static final int CONNECT_TIMEOUT = 60000;
    private static final int READ_TIMEOUT = 2 * 60000;
    private static final int SIZE_LIMIT = 100;
    private static final int NUM_RETRIES = 3;

    private final HttpRequestFactory requestFactory;
    private final ObjectMapper mapper;

    public TwitterClient(TwitterAuthenticator authenticator ) throws TwitterAuthenticationException {
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

    public Stream<Tweet> streamTweetsByWord(String word) {
        try {
            return getTweetsByWord(word).stream().map(this::mapToTweet);
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

    public List<String> getTweetsByWord(String word) throws IOException {
        LOGGER.info("Read tweets...");
        BufferedReader reader = new BufferedReader(new InputStreamReader(filterRawTweetsByWord(word)), 6000 * 100);
//        LOGGER.debug("Wait until buffer is ready");
//        while (!reader.ready());
//        LOGGER.debug("Buffer is ready");
        List<String> tweets = new ArrayList<>(SIZE_LIMIT);
        LOGGER.info("Read response, in takes time...");
        String startLine = readLine(reader);
        LOGGER.info("Reading has been finished");
        CompletableFuture<List<String>> future = CompletableFuture.supplyAsync(() -> {
            int tweetsCount = 0;
            LOGGER.info("Start to read incoming tweets");

            for (String line = startLine; line != null && tweetsCount < SIZE_LIMIT; line = readLine(reader)) {
                if (line.trim().isEmpty())
                    continue;

                tweets.add(line);
                ++tweetsCount;
            }
            return tweets;
        });
        List<String> strings = null;
        try {
            strings = future.get(30, TimeUnit.MINUTES);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("{}", e);
        }
        LOGGER.info("Extracted {} tweets", tweets.size());
        return tweets;
    }

    private String readLine(BufferedReader reader) {
        try {
            return reader.readLine();
        } catch (IOException e) {
            LOGGER.error("Can't read line {}", e);
            return null;
        }
    }


}
