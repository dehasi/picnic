package org.interview;

import com.google.common.annotations.VisibleForTesting;
import org.interview.client.TwitterClient;
import org.interview.domain.Tweet;
import org.interview.domain.User;
import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Runner {
    private static final Logger LOGGER = LoggerFactory.getLogger(Runner.class);
    private static final String CONSUMER_KEY = "vp8qXAMoZzy6jowJdtouPLUUb";
    private static final String CONSUMER_SECRET = "IMx3eIRfXXbRimoIz7cNpZCl0dr9dYEdRuDVTr2C4LdResXjN7";

    public static void main(String[] args) throws TwitterAuthenticationException {
        TwitterAuthenticator authenticator = new TwitterAuthenticator(System.out, CONSUMER_KEY, CONSUMER_SECRET);

        TwitterClient gateway = new TwitterClient(authenticator);

        Map<User, List<Tweet>> userTweets = gateway.streamTweetsByWord("bieber").collect(Collectors.groupingBy(Tweet::getUser));
        sortTweets(userTweets);
        sortUsers(userTweets)
                .forEach(user ->
                        LOGGER.info("User {} has tweets {}", user, userTweets.get(user))
                );
    }

    @VisibleForTesting
    static void sortTweets(Map<User, List<Tweet>> userTweets) {
        userTweets.values().forEach(tweets -> tweets.sort(Comparator.comparing(Tweet::getCreatedAt)));
    }

    @VisibleForTesting
    static Stream<User> sortUsers(Map<User, List<Tweet>> userTweets) {
        return userTweets.keySet().stream()
                .sorted(Comparator.comparing(User::getCreatedAt));
    }


}
