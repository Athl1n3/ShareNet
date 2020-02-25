package com.adamm.sharenet.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.adamm.sharenet.DAO.PostDao;
import com.adamm.sharenet.Database.AppDatabase;
import com.adamm.sharenet.entities.Post;

import java.util.List;
import java.util.concurrent.ExecutorService;

public class PostViewModel extends AndroidViewModel {

    private PostDao postDao;

    public PostViewModel(@NonNull Application application) {
        super(application);
        postDao = AppDatabase.getAppDatabase(application).postDao();
   //     executorService = Executors.newSingleThreadExecutor();
    }

    public LiveData<List<Post>> getAllPosts() {
        return postDao.getPosts();
    }

    public LiveData<List<Post>> getMyPosts() { return postDao.getMyPosts(AppDatabase.curr_user.uid); }

  /*  public void savePost(Post post) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    postDao.addPost(post);
                } catch (Exception e) {
                }
            }
        }).start();
    }

    public void deletePost(Post post) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    postDao.DeletePost(post);
                } catch (Exception e) {
                }
            }
        }).start();
    }

   */
}