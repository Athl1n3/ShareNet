package com.adamm.sharenet.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity (tableName = "comments")
public class Comment {
    @PrimaryKey(autoGenerate = true)
    public int CommentID;
    public String uid;//Commenter ID
    public String postID;//Post ID
    public String author;//Commenter name
    public String text;//Commenter text

    @Ignore
    public Comment() {
        // Default constructor required for calls to DataSnapshot.getValue(Comment.class)
    }

    public Comment(String uid, String author, String text) {
        this.uid = uid;
        this.author = author;
        this.text = text;
    }
}
