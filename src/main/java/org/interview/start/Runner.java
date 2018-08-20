package org.interview.start;

import org.interview.TwitterGateway;
import org.interview.oauth.twitter.TwitterAuthenticationException;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class Runner {



    public static void main(String[] args) throws TwitterAuthenticationException {

        TwitterGateway gateway = new TwitterGateway();

        ZonedDateTime dateTime = ZonedDateTime.now(ZoneId.of("UTC")).minusSeconds(30L);

        gateway.filterTweetsByWord("biber")
//                .filter(tweet -> tweet.createdAt.isAfter(dateTime))
                .limit(10)
                .forEach(System.out::println);
    }
}
