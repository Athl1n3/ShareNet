package com.adamm.sharenet.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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
}