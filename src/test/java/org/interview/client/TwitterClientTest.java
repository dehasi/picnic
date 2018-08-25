package org.interview.client;

import com.google.api.client.http.HttpRequestFactory;
import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class TwitterClientTest {


    @Mock
    private TwitterAuthenticator authenticator;
    @Mock
    private HttpRequestFactory requestFactory;

    private TwitterClient client;

    @Before
    public void createClient() throws TwitterAuthenticationException {
        when(authenticator.getAuthorizedHttpRequestFactory()).thenReturn(requestFactory);
        client = new TwitterClient(authenticator);
    }

    @Test
    public void ete() {

    }


}
