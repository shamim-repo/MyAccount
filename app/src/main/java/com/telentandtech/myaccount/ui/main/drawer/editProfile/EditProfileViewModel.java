package com.telentandtech.myaccount.ui.main.drawer.editProfile;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.database.resultObjects.UserResult;
import com.telentandtech.myaccount.repository.UserRepo;

public class EditProfileViewModel extends AndroidViewModel {

    private UserRepo userRepo;

    public EditProfileViewModel(@NonNull Application application) {
        super(application);
        userRepo = new UserRepo(application);
    }

    public void updateProfile(User user, String new_full_name, String new_password) {
        userRepo.updateProfile(user, new_full_name, new_password);
    }

    public LiveData<UserResult> getProfileUpdateLiveData() {
        return userRepo.getUserLiveData();
    }

    public void getUser(String uid) {
        userRepo.getUserByUid(uid);
    }
    public LiveData<UserResult> getUserLiveData() {
        return userRepo.getUserLiveData();
    }
}