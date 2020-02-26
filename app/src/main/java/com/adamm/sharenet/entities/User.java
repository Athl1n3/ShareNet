package com.adamm.sharenet.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    public int uid;
    @ColumnInfo(name = "username")
    public String username;
    @ColumnInfo(name = "email")
    public String email;
    @ColumnInfo(name = "first-name")
    public String firstName;
    @ColumnInfo(name = "last-name")
    public String lastName;
    @ColumnInfo(name = "password")
    public String password;

    @Ignore
    public User() {
    }

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
        firstName = "";
        lastName = "";
    }
}
