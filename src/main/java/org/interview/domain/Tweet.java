package org.interview.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

import java.time.ZonedDateTime;

public final class Tweet {
    /*
    The message ID
The creation date of the message
The text of the message
The author of the message
     */


    public final String id;

    public final String idStr;

    public final ZonedDateTime created_at;
    public final String text;
    public final String timestamp_ms;
    public final User user;


    @JsonCreator
    public Tweet( @JsonProperty("id") String id,
                  @JsonProperty("id_str") String idStr,
                  @JsonProperty("created_at") @JsonFormat(pattern = "EEE MMM dd HH:mm:ss Z yyyy")ZonedDateTime created_at,
                  @JsonProperty("text") String text,
                  @JsonProperty("timestamp_ms") String timestamp_ms,
                  @JsonProperty("user") User user) {
        this.id = id;
        this.idStr = idStr;
        this.created_at = created_at;
        this.text = text;
        this.timestamp_ms = timestamp_ms;
        this.user = user;
    }

    @Override
    public String toString() {
        return  MoreObjects.toStringHelper(this)
                .add("id",id)
                .add("id_str",idStr)
                .add("created_at",created_at)
                .add("text",text)
                .add("timestamp_ms",timestamp_ms)
                .add("user",user)
                .toString();

    }
}
