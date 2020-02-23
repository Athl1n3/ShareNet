package com.adamm.sharenet.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.adamm.sharenet.DAO.CommentDao;
import com.adamm.sharenet.DAO.PostDao;
import com.adamm.sharenet.DAO.UserDao;
import com.adamm.sharenet.entities.Comment;
import com.adamm.sharenet.entities.Post;
import com.adamm.sharenet.entities.User;

@Database(entities = {User.class, Post.class, Comment.class}, version =  1)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract UserDao userDao();
    public abstract PostDao postDao();
    public  abstract CommentDao commentDao();

    public static AppDatabase getAppDatabase(Context context)
    {
        if(INSTANCE == null)
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "ShareNet").allowMainThreadQueries().build();
        return INSTANCE;
    }

    public static void destroyInstance()
    { INSTANCE = null;}
}
