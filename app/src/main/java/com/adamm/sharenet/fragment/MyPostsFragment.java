package com.adamm.sharenet.fragment;

import android.os.Bundle;

import androidx.lifecycle.LiveData;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.ViewModel.PostViewModel;
import com.adamm.sharenet.entities.Post;

import java.util.List;

public class MyPostsFragment extends PostListFragment {

    public MyPostsFragment() {}
//QUERY FOR POSTS HERE
    @Override
    public LiveData<List<Post>> getQuery(AppDatabase mDatabase, PostViewModel postViewModel) {
        return postViewModel.getMyPosts();
       // return mDatabase.postDao().getMyPosts(AppDatabase.curr_user.uid);
    }
}
