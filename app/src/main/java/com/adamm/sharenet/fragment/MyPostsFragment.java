package com.adamm.sharenet.fragment;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.entities.Post;

import java.util.List;

public class MyPostsFragment extends PostListFragment {

    public MyPostsFragment() {}
//QUERY FOR POSTS HERE
    @Override
    public LiveData<List<Post>> getQuery(AppDatabase mDatabase) {
        return mDatabase.postDao().getMyPosts(AppDatabase.curr_user.uid);
    }
}
