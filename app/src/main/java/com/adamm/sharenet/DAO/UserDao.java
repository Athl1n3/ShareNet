package com.adamm.sharenet.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.adamm.sharenet.entities.User;

@Dao
public interface UserDao {

    @Transaction
    @Query("SELECT * FROM users WHERE email LIKE  :email")
    User getLoginDetails(String email);

    @Query("SELECT * FROM users WHERE email LIKE :email")
    User getUser(String email);

    @Update
    void editUser(User user);

    @Insert
    void addUser(User user);
}
