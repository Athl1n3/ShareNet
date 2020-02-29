package com.adamm.sharenet.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.adamm.sharenet.DAO.PostDao;
import com.adamm.sharenet.DAO.UserDao;
import com.adamm.sharenet.Services.PostService;
import com.adamm.sharenet.entities.Post;
import com.adamm.sharenet.entities.User;

@Database(entities = {User.class, Post.class}, version =  1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;
    public static User curr_user;
    public static PostService postService;



    private static int notificationId = 1;

    public abstract UserDao userDao();
    public abstract PostDao postDao();

    public static AppDatabase getAppDatabase(Context context)
    {
        if(INSTANCE == null) {
            notificationId = 1;
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "ShareNet").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }

    public static void destroyInstance()
    { INSTANCE = null; curr_user = null;}

    public static User getCurr_user() {
        return curr_user;
    }

    public static void setCurr_user(User curr_user) {
        AppDatabase.curr_user = curr_user;
    }

    public static int getNotificationId() {
        return notificationId;
    }

    public static void setNotificationId(int notificationId) {
        AppDatabase.notificationId = notificationId;
    }
}
