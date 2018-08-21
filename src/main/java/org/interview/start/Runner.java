package org.interview.start;

import org.interview.TwitterGateway;
import org.interview.domain.Tweet;
import org.interview.domain.User;
import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Runner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) throws TwitterAuthenticationException, IOException {
        TwitterGateway gateway = new TwitterGateway();

        Map<User, List<Tweet>> userTweets2 = gateway.streamTweetsByWord("bieber").collect(Collectors.groupingBy(tweet -> tweet.user));
        userTweets2.values().forEach(tweets -> tweets.sort(Comparator.comparing(t -> t.createdAt)));
        userTweets2.keySet().stream()
                .sorted(Comparator.comparing(user -> user.createdAt))
                .forEach(user ->
                        LOGGER.info("User {} has tweets {}", user, userTweets2.get(user))
                );
    }
}
