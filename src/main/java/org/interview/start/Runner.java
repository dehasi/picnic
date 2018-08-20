package org.interview.start;

import com.google.api.client.http.HttpRequestFactory;
import org.interview.oauth.twitter.TwitterAuthenticationException;
import org.interview.oauth.twitter.TwitterAuthenticator;

public class Runner {

    private static final String CONSUMER_KEY =  "vp8qXAMoZzy6jowJdtouPLUUb";
    private static final String CONSUMER_SECRET =  "IMx3eIRfXXbRimoIz7cNpZCl0dr9dYEdRuDVTr2C4LdResXjN7";

    public static void main(String[] args) throws TwitterAuthenticationException {
        TwitterAuthenticator authenticator = new TwitterAuthenticator(System.out, CONSUMER_KEY, CONSUMER_SECRET);
        HttpRequestFactory requestFactory = authenticator.getAuthorizedHttpRequestFactory();
        System.out.println(requestFactory);

    }
}
