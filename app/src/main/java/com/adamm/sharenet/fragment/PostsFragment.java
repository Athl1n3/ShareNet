package com.adamm.sharenet.fragment;

import androidx.lifecycle.LiveData;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.entities.Post;

import java.util.List;

public class PostsFragment extends PostListFragment {

    public PostsFragment() {}

    @Override
    public LiveData<List<Post>> getQuery(AppDatabase mDatabase) {
        return mDatabase.postDao().getPosts();
    }
}
