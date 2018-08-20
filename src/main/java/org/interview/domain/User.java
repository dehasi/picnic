package org.interview.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

public final class User {
    /*
    The user ID
The creation date of the user
The screen name of the user
     */

    public final String id;
    public final String idStr;
    public final String name;
    public final String screenName;
    public final String createdAt; /* Chech if it The creation date of the user */

    @JsonCreator
    public User(@JsonProperty("id") String id,
                @JsonProperty("id_str") String idStr,
                @JsonProperty("name") String name,
                @JsonProperty("screen_name") String screenName,
                @JsonProperty("created_at") String createdAt) {
        this.id = id;
        this.idStr = idStr;
        this.name = name;
        this.screenName = screenName;
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("id_str", idStr)
                .add("name", name)
                .add("screen_name", screenName)
                .add("created_at", createdAt)
                .toString();
    }
}
