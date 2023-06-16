package com.telentandtech.myaccount.ui.main.drawer.classes.manageGroup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.resultObjects.ClassListResult;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupResult;
import com.telentandtech.myaccount.repository.ClassRepo;
import com.telentandtech.myaccount.repository.GroupRepo;

public class ManageGroupViewModel extends AndroidViewModel {
    private GroupRepo groupRepo;
    public ManageGroupViewModel(@NonNull Application application) {
        super(application);
        groupRepo =new GroupRepo(application);
    }
    public void getClassList(String uid){
        groupRepo.getClassNameIdList(uid);
    }
    public LiveData<ClassNameIdListResult> getClassListLiveData(){
        return groupRepo.getClassNameIdListLiveData();
    }
    public void  getGroupList(String uid, long class_id){
        groupRepo.getGroupsByClassID(uid,class_id);
    }
    public void getGroupByActive(String uid,long class_id,boolean active){
        groupRepo.getGroupByActive(uid,class_id,active);
    }
    public LiveData<GroupListResult> getGroupListLiveData(){
        return  groupRepo.getGroupListLiveData();
    }
    public void deleteGroup(Group group){
        groupRepo.deleteGroup(group);
    }
    public void updateGroup(Group group){
        groupRepo.updateGroup(group);
    }
    public LiveData<GroupResult> getGroupEditLiveData(){
        return groupRepo.getGroupUpdateLiveData();
    }
    public LiveData<GroupResult> getGroupDeleteLiveData(){
        return groupRepo.getGroupDeleteLiveData();
    }
    public void insertGroup(Group group){
        groupRepo.insertGroup(group);
    }
    public LiveData<GroupResult> getGroupInsertLiveData(){
        return groupRepo.getGroupInsertLiveData();
    }

}