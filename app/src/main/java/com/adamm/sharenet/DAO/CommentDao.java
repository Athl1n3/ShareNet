package com.adamm.sharenet.DAO;

import androidx.room.Dao;
import androidx.room.Query;

import com.adamm.sharenet.entities.Comment;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface CommentDao {
    @Query("SELECT * FROM comments WHERE postID LIKE :postID")
    List<Comment> getComments(String postID);
}
