package com.telentandtech.myaccount.ui.main.login;


import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.telentandtech.myaccount.database.resultObjects.UserResult;
import com.telentandtech.myaccount.repository.UserRepo;

public class LogInFragmentViewModel extends AndroidViewModel {
    private UserRepo userRepo;

    public LogInFragmentViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepo(application);
    }

    public LiveData<UserResult> getAuthResult() {
        return userRepo.getUserLiveData();
    }
    public void authenticateUser(String email, String password){
        userRepo.authenticateUser(email, password);
    }
}