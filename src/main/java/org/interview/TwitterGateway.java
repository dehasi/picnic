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

public class TwitterGateway {

    private static final Logger LOGGER = LoggerFactory.getLogger(TwitterGateway.class);

    private static final String CONSUMER_KEY = "vp8qXAMoZzy6jowJdtouPLUUb";
    private static final String CONSUMER_SECRET = "IMx3eIRfXXbRimoIz7cNpZCl0dr9dYEdRuDVTr2C4LdResXjN7";
    private static final String RESOURCE_URL = "https://stream.twitter.com/1.1/statuses/filter.json";
    private static final int CONNECT_TIMEOUT = 60000;
    private static final int READ_TIMEOUT = 2 * 60000;

    private final TwitterAuthenticator authenticator;
    private final HttpRequestFactory requestFactory;

    private final ObjectMapper mapper;

    public TwitterGateway() throws TwitterAuthenticationException {
        authenticator = new TwitterAuthenticator(System.out, CONSUMER_KEY, CONSUMER_SECRET);

        LOGGER.debug("Authorizing http client");
        requestFactory = authenticator.getAuthorizedHttpRequestFactory();

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
    }

    public InputStream filterRawTweetsByWord(String word) throws IOException {
        HttpRequest request = requestFactory.buildGetRequest(
                new GenericUrl(RESOURCE_URL.concat("?track=").concat(word))); //.concat("&count=100")

        request.setConnectTimeout(CONNECT_TIMEOUT);
        request.setReadTimeout(READ_TIMEOUT);

        LOGGER.info("Executing request to twitter");

        HttpResponse response = request.execute();
        return response.getContent();
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
        return streamRawTweetsByWord(word).map(this::mapToTweet);
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
        long time_limit_ms = 30L * 1000L;
        int size_limit = 100;

        BufferedReader reader = new BufferedReader(new InputStreamReader(filterRawTweetsByWord(word)));

        List<String> tweets = new ArrayList<>(size_limit);

        String line = reader.readLine();
        int countTweets = 0;
        long startTime = System.currentTimeMillis();
        LOGGER.info("Start to read incoming tweets");

        for (; line != null && countTweets < size_limit && (System.currentTimeMillis() - startTime < time_limit_ms);
             line = reader.readLine().trim()) {
            if (line.isEmpty()) continue;

            tweets.add(line);

            countTweets++;
        }
        LOGGER.info("Extracted {} tweets for {} secs", countTweets, (System.currentTimeMillis() - startTime)/1000L );
        return tweets;
    }
}
