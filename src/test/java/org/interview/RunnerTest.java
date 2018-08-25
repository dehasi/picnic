package org.interview;

import org.interview.domain.Tweet;
import org.interview.domain.User;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by Ravil on 26/08/2018.
 */
public class RunnerTest {
    private static final ZonedDateTime NOW = ZonedDateTime.now();
    private static final User USER_1 = new User(1L, "Na", "ololo", NOW.minusDays(10));
    private static final User USER_2 = new User(2L, "Na", "ololo", NOW.minusDays(1));

    private static final Tweet TWEET_1_FROM_USER_1 = new Tweet(1L, NOW.minusHours(3), "tests", USER_1);
    private static final Tweet TWEET_2_FROM_USER_1 = new Tweet(2L, NOW.minusHours(2), "tests", USER_1);
    private static final Tweet TWEET_3_FROM_USER_1 = new Tweet(3L, NOW.minusHours(1), "tests", USER_1);
    private static final Tweet TWEET_1_FROM_USER_2 = new Tweet(4L, NOW.minusHours(3), "tests", USER_2);
    private static final Tweet TWEET_2_FROM_USER_2 = new Tweet(5L, NOW.minusHours(2), "tests", USER_2);
    private static final Tweet TWEET_3_FROM_USER_2 = new Tweet(6L, NOW.minusHours(1), "tests", USER_2);

    private Map<User, List<Tweet>> userTweet;

    @Before
    public void initTweetMap() {
        userTweet = new HashMap<>();
        userTweet.put(USER_2, asList(TWEET_2_FROM_USER_2, TWEET_3_FROM_USER_2, TWEET_1_FROM_USER_2));
        userTweet.put(USER_1, asList(TWEET_2_FROM_USER_1, TWEET_3_FROM_USER_1, TWEET_1_FROM_USER_1));
    }

    @Test
    public void sortTweets_sortsChronologicallyAscending() {
        Runner.sortTweets(userTweet);

        assertThat(userTweet.get(USER_1)).containsExactly(TWEET_1_FROM_USER_1,TWEET_2_FROM_USER_1,TWEET_3_FROM_USER_1);
        assertThat(userTweet.get(USER_2)).containsExactly(TWEET_1_FROM_USER_2,TWEET_2_FROM_USER_2,TWEET_3_FROM_USER_2);
    }

    @Test
    public void sortUsers_sortsChronologicallyAscending(){
        List<User> users = Runner.sortUsers(userTweet).collect(Collectors.toList());

        assertThat(users).containsExactly(USER_1, USER_2);
    }


}
