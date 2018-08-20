package org.interview.domain;

import com.google.common.base.MoreObjects;

public final class Tweet {
    /*
    The message ID
The creation date of the message
The text of the message
The author of the message
     */

    public final String id;
    public final String id_str;
    public final String created_at;
    public final String text;
    public final String timestamp_ms;
    public final User user;


    public Tweet(String id, String id_str, String created_at, String text, String timestamp_ms, User user) {
        this.id = id;
        this.id_str = id_str;
        this.created_at = created_at;
        this.text = text;
        this.timestamp_ms = timestamp_ms;
        this.user = user;
    }

    @Override
    public String toString() {
        return  MoreObjects.toStringHelper(this)
                .add("id",id)
                .add("id_str",id_str)
                .add("created_at",created_at)
                .add("text",text)
                .add("timestamp_ms",timestamp_ms)
                .add("user",user)
                .toString();

    }
}
