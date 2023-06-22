package com.telentandtech.myaccount.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.telentandtech.myaccount.database.entityes.User;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertUser(User user);
    @Delete
    void deleteUser(User user);
    @Update
    void updateUser(User user);
    @Query("SELECT *FROM 'user'")
    List<User> getUsers();
    @Query("UPDATE 'user' SET full_name=:full_name, password = :new_password " +
            "WHERE uid = :uid AND password = :password AND email = :email")
    
    void updateProfile(String uid, String email, String password,String full_name,String new_password);

    @Query("SELECT  *FROM 'user' WHERE  email= :email and password = :password LIMIT 1")
    User authenticateUser(String email, String password);
    @Query("SELECT  *FROM 'user' WHERE uid = :uid LIMIT 1")
    User getUserByUid(String uid);
    @Query("SELECT  *FROM 'user' WHERE email = :email LIMIT 1")
    User checkEmail(String email);

}
