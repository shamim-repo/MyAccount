package com.telentandtech.myaccount.ui.main.drawer.classes.addGroup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.resultObjects.ClassListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupResult;
import com.telentandtech.myaccount.repository.ClassRepo;
import com.telentandtech.myaccount.repository.GroupRepo;

public class AddGroupViewModel extends AndroidViewModel {
    private GroupRepo groupRepo;
    private ClassRepo classRepo;
    public AddGroupViewModel(@NonNull Application application) {
        super(application);
        groupRepo= new GroupRepo(application);
        classRepo = new ClassRepo(application);
    }

    public void getClassList(String uid){
        classRepo.getAllClassesList(uid);
    }
    public LiveData<ClassListResult> getClassListLivedata(){
        return classRepo.getClassListLiveData();
    }
    public void insertGroup(Group group){
        groupRepo.insertGroup(group);
    }
    public LiveData<GroupResult> getGroupLivedata(){
        return groupRepo.getGroupInsertLiveData();
    }

}