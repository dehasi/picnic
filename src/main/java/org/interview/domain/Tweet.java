package org.interview.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import java.time.ZonedDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
public final class Tweet {

    private final long id;
    private final ZonedDateTime createdAt;
    private final String text;
    private final long timestampMs;
    private final User user;


    @JsonCreator
    public Tweet(@JsonProperty("id") long id,
                 @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "EEE MMM dd HH:mm:ss Z yyyy", locale = "en")
                 @JsonProperty("created_at") ZonedDateTime createdAt,
                 @JsonProperty("text") String text,
                 @JsonProperty("timestamp_ms") long timestampMs,
                 @JsonProperty("user") User user) {
        this.id = id;
        this.createdAt = createdAt;
        this.text = text;
        this.timestampMs = timestampMs;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public String getText() {
        return text;
    }

    public long getTimestampMs() {
        return timestampMs;
    }

    public User getUser() {
        return user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tweet tweet = (Tweet) o;
        return id == tweet.id;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("created_at", createdAt)
                .add("text", text)
                .add("timestamp_ms", timestampMs)
                .toString();
    }
}
