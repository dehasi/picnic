package org.interview.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Tweet {
    /*
    The message ID
The creation date of the message
The text of the message
The author of the message
     */


    public final String id;

    public final String idStr;

    public final String text;
    public final long timestamp_ms;
    public final User user;


    @JsonCreator
    public Tweet(@JsonProperty("id") String id,
                 @JsonProperty("id_str") String idStr,
                 @JsonProperty("text") String text,
                 @JsonProperty("timestamp_ms") long timestamp_ms,
                 @JsonProperty("user") User user) {
        this.id = id;
        this.idStr = idStr;
        this.text = text;
        this.timestamp_ms = timestamp_ms;
        this.user = user;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("id_str", idStr)
                .add("text", text)
                .add("timestamp_ms", timestamp_ms)
                .add("user", user)
                .toString();

    }
}
