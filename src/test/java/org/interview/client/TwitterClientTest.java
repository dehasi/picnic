package org.interview.client;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import org.interview.domain.Tweet;
import org.interview.domain.User;
import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterClientTest {

    private static final String RAW_JSON = "{\"created_at\":\"Mon Aug 20 13:28:07 +0000 2018\",\"id\":1031533339793129472,\"id_str\":\"1031533339793129472\",\"text\":\"juntar dinheiro pra ir na prox tour do justin bieber que eu amo\",\"source\":\"\\u003ca href=\\\"http:\\/\\/twitter.com\\/download\\/iphone\\\" rel=\\\"nofollow\\\"\\u003eTwitter for iPhone\\u003c\\/a\\u003e\",\"truncated\":false,\"in_reply_to_status_id\":null,\"in_reply_to_status_id_str\":null,\"in_reply_to_user_id\":null,\"in_reply_to_user_id_str\":null,\"in_reply_to_screen_name\":null,\"user\":{\"id\":1239680360,\"id_str\":\"1239680360\",\"name\":\"ste\",\"screen_name\":\"flowxrst\",\"location\":\"Manaus, Brasil\",\"url\":null,\"description\":null,\"translator_type\":\"none\",\"protected\":false,\"verified\":false,\"followers_count\":1444,\"friends_count\":378,\"listed_count\":5,\"favourites_count\":13566,\"statuses_count\":40091,\"created_at\":\"Sun Mar 03 19:38:25 +0000 2013\",\"utc_offset\":null,\"time_zone\":null,\"geo_enabled\":true,\"lang\":\"pt\",\"contributors_enabled\":false,\"is_translator\":false,\"profile_background_color\":\"FFFFFF\",\"profile_background_image_url\":\"http:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_image_url_https\":\"https:\\/\\/abs.twimg.com\\/images\\/themes\\/theme1\\/bg.png\",\"profile_background_tile\":false,\"profile_link_color\":\"000000\",\"profile_sidebar_border_color\":\"FFFFFF\",\"profile_sidebar_fill_color\":\"DDEEF6\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":false,\"profile_image_url\":\"http:\\/\\/pbs.twimg.com\\/profile_images\\/1030497329999237121\\/MLVfvoFy_normal.jpg\",\"profile_image_url_https\":\"https:\\/\\/pbs.twimg.com\\/profile_images\\/1030497329999237121\\/MLVfvoFy_normal.jpg\",\"profile_banner_url\":\"https:\\/\\/pbs.twimg.com\\/profile_banners\\/1239680360\\/1533144133\",\"default_profile\":false,\"default_profile_image\":false,\"following\":null,\"follow_request_sent\":null,\"notifications\":null},\"geo\":null,\"coordinates\":null,\"place\":null,\"contributors\":null,\"is_quote_status\":false,\"quote_count\":0,\"reply_count\":0,\"retweet_count\":0,\"favorite_count\":0,\"entities\":{\"hashtags\":[],\"urls\":[],\"user_mentions\":[],\"symbols\":[]},\"favorited\":false,\"retweeted\":false,\"filter_level\":\"low\",\"lang\":\"pt\",\"timestamp_ms\":\"1534771687826\"}";

    @Mock
    private TwitterAuthenticator authenticator;
    @Mock
    private HttpRequestFactory requestFactory;
    @Mock
    private HttpRequest request;
    @Mock
    private HttpResponse response;

    private TwitterClient client;

    @Before
    public void createClient() throws TwitterAuthenticationException {
        when(authenticator.getAuthorizedHttpRequestFactory()).thenReturn(requestFactory);
        client = new TwitterClient(authenticator);
    }

    @Test
    public void mapToTweet_returnsCorrectTweet() {
        // when
        Tweet tweet = client.mapToTweet(RAW_JSON);

        //then
        assertThat(tweet.getId()).isEqualTo(1031533339793129472L);
        assertThat(tweet.getText()).isEqualTo("juntar dinheiro pra ir na prox tour do justin bieber que eu amo");
        assertThat(tweet.getCreatedAt())
                .isEqualTo(ZonedDateTime.of(2018, 8, 20, 13, 28, 7, 0, ZoneId.of("UTC")));
        //and
        User user = tweet.getUser();
        assertThat(user.getId()).isEqualTo(1239680360L);
        assertThat(user.getName()).isEqualTo("ste");
        assertThat(user.getScreenName()).isEqualTo("flowxrst");
        assertThat(user.getCreatedAt())
                .isEqualTo(ZonedDateTime.of(2013, 3, 3, 19, 38, 25, 0, ZoneId.of("UTC")));
    }

    @Test
    public void getTweetsByWord_returnsTweets() throws IOException {
        when(requestFactory.buildGetRequest(any())).thenReturn(request);
        InputStream is = new ByteArrayInputStream(RAW_JSON.getBytes());
        when(request.execute()).thenReturn(response);
        when(response.getContent()).thenReturn(is);

        List<String> rawJSONs = client.getTweetsByWord("aWord");

        assertThat(rawJSONs).containsExactly(RAW_JSON);
    }

}
