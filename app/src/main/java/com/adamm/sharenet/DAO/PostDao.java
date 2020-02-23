package com.adamm.sharenet.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.adamm.sharenet.entities.Post;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface PostDao {
    @Query("SELECT * FROM posts")
    LiveData<List<Post>> getPosts();

    @Query("SELECT * FROM posts WHERE uid LIKE :myUID")
    LiveData<List<Post>> getMyPosts(int myUID);

    @Insert
    void addPost(Post post);

    @Delete
    void DeletePost(Post post);
}
