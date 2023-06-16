package com.telentandtech.myaccount.ui.main.admin;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.telentandtech.myaccount.database.dao.UserDao;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.database.resultObjects.UserListResult;
import com.telentandtech.myaccount.database.resultObjects.UserResult;
import com.telentandtech.myaccount.repository.UserRepo;

public class AdminViewModel extends AndroidViewModel {

    private UserRepo userRepo;
    public AdminViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepo(application);
    }
    public void getUsers(){
        userRepo.getUserList();
    }
    public LiveData<UserListResult> getUserListLiveData(){
        return userRepo.getUserListLiveData();
    }
    public void deleteUser(User user){
        userRepo.updateUser(user);
    }
    public void updateUser(User user){
        userRepo.updateUser(user);
    }
    public LiveData<UserResult> getUserUpdateLiveData(){
        return userRepo.getUserLiveData();
    }
}