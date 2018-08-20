package org.interview.domain;

import com.google.common.base.MoreObjects;

public final class User {
    /*
    The user ID
The creation date of the user
The screen name of the user
     */

    public final String id;
    public final String id_str;
    public final String name;
    public final String screen_name;
    public final String created_at; /* Chech if it The creation date of the user */

    public User(String id, String id_str, String name, String screen_name, String created_at) {
        this.id = id;
        this.id_str = id_str;
        this.name = name;
        this.screen_name = screen_name;
        this.created_at = created_at;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("id_str", id_str)
                .add("name", name)
                .add("screen_name", screen_name)
                .add("created_at", created_at)
                .toString();
    }
}
