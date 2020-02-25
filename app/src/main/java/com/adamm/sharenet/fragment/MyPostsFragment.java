package com.adamm.sharenet.fragment;

import android.os.Bundle;

import androidx.lifecycle.LiveData;

import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.ViewModel.PostViewModel;
import com.adamm.sharenet.entities.Post;

import java.util.List;

public class MyPostsFragment extends PostListFragment {

    private PostViewModel postViewModel;


    public MyPostsFragment() {}
//QUERY FOR POSTS HERE
    @Override
    public LiveData<List<Post>> getQuery(AppDatabase mDatabase) {
        return mDatabase.postDao().getMyPosts(AppDatabase.curr_user.uid);
    }

    public int getWho() {
        return 0;
    }

}
