package com.telentandtech.myaccount.ui.main.signUp;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telentandtech.myaccount.database.resultObjects.UserResult;
import com.telentandtech.myaccount.repository.UserRepo;


public class SignUpViewModel extends AndroidViewModel {

    private UserRepo userRepo;

    public SignUpViewModel(@NonNull Application application) {
        super(application);
        userRepo=new UserRepo(application);
    }
    public void insertUser(String fullName,String email,String password){
        userRepo.insertUser(fullName,email,password);
    }
    public LiveData<UserResult> getUserResultMutableLiveData() {
        return userRepo.getUserInsertLiveData();
    }
}