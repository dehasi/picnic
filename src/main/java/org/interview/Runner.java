package org.interview;

import org.interview.client.TwitterClient;
import org.interview.domain.Tweet;
import org.interview.domain.User;
import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Runner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);

    public static void main(String[] args) throws TwitterAuthenticationException {
        TwitterClient gateway = new TwitterClient();

        Map<User, List<Tweet>> userTweets = gateway.streamTweetsByWord("bieber").collect(Collectors.groupingBy(Tweet::getUser));
        userTweets.values().forEach(tweets -> tweets.sort(Comparator.comparing(Tweet::getCreatedAt)));
        userTweets.keySet().stream()
                .sorted(Comparator.comparing(User::getCreatedAt))
                .forEach(user ->
                        LOGGER.info("User {} has tweets {}", user, userTweets.get(user))
                );
    }
}
