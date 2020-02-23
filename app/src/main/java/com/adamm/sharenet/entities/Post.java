package com.adamm.sharenet.entities;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.HashMap;
import java.util.Map;

@Entity(tableName = "posts")
public class Post {

    @PrimaryKey(autoGenerate = true)
    public int postID;//PostID
    public String uid;//Poster ID
    public String author;// PosterName
    public String title;// Post title
    public String body;// Post content
    @Ignore
    public int starCount = 0;// post likes
    @Ignore
    public Map<String, Boolean> stars = new HashMap<>();//MOST LIKELY TO DEPRECATE THIS FEATURE

    @Ignore
    public Post() {}

    public Post(String uid, String author, String title, String body) {
        this.uid = uid;
        this.author = author;
        this.title = title;
        this.body = body;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", uid);
        result.put("author", author);
        result.put("title", title);
        result.put("body", body);
        result.put("starCount", starCount);
        result.put("stars", stars);

        return result;
    }
}