package org.interview.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class User {
    private final long id;
    private final String name;
    private final String screenName;
    private final ZonedDateTime createdAt;

    @JsonCreator
    public User(@JsonProperty("id") long id,
                @JsonProperty("name") String name,
                @JsonProperty("screen_name") String screenName,
                @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd HH:mm:ss Z yyyy", locale = "en")
                @JsonProperty("created_at") ZonedDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.screenName = screenName;
        this.createdAt = createdAt;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getScreenName() {
        return screenName;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("name", name)
                .add("screen_name", screenName)
                .add("created_at", createdAt)
                .toString();
    }
}
