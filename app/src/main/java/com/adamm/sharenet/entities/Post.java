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
    public int uid;//Poster ID
    public String author;// PosterName
    public String title;// Post title
    public String body;// Post content

    @Ignore
    public Post() {}

    public Post(int uid, String author, String title, String body) {
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

        return result;
    }
}