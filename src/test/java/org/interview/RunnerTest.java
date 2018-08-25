package org.interview;

import org.interview.domain.Tweet;
import org.interview.domain.User;
import org.junit.Test;

import java.time.ZonedDateTime;

/**
 * Created by Ravil on 26/08/2018.
 */
public class RunnerTest {
    private static final ZonedDateTime NOW = ZonedDateTime.now();
    private static final User USER_1 = new User(1L, "Na", "ololo", NOW.minusDays(10));
    private static final User USER_2 = new User(2L, "Na", "ololo", NOW.minusDays(1));

//    Tweet tweet = new
    @Test
    public void sortTweets_sortsChronologicallyAscending() {
    }
}
