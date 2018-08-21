package org.interview.start;

import org.interview.TwitterGateway;
import org.interview.domain.Tweet;
import org.interview.domain.User;
import org.interview.oauth.twitter.TwitterAuthenticationException;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Runner {


    public static void main(String[] args) throws TwitterAuthenticationException {

        TwitterGateway gateway = new TwitterGateway();

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("UTC")).minusSeconds(30L);

        Map<User, List<Tweet>> userTweets = gateway.streamTweetsByWord("biber")
//                .filter(tweet -> tweet.createdAt.isAfter(dateTime))
                .limit(100)
                .collect(Collectors.groupingBy(tweet -> tweet.user));

        userTweets.values().forEach(values -> values.sort(Comparator.comparing(t -> t.createdAt)));

        System.out.println(userTweets);
    }
}
