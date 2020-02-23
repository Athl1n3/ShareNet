package com.adamm.sharenet.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import com.adamm.sharenet.entities.User;

@Dao
public interface UserDao {

    @Transaction
    @Query("SELECT * FROM users WHERE email LIKE  :email AND password LIKE :password")
    User getLoginDetails(String email, String password);

    @Query("SELECT * FROM users WHERE email LIKE :email")
    User getUser(String email);

    @Insert
    void addUser(User user);
}
