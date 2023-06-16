package com.telentandtech.myaccount.ui.main.drawer.classes.manageClass;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.resultObjects.ClassListResult;
import com.telentandtech.myaccount.database.resultObjects.ClassResult;
import com.telentandtech.myaccount.repository.ClassRepo;

public class ManageClassViewModel extends AndroidViewModel {
    private ClassRepo classRepo;
    public ManageClassViewModel(@NonNull Application application) {
        super(application);
        classRepo = new ClassRepo(application);
    }

    public void getAllClassesList(String uid){
        classRepo.getAllClassesList(uid);
    }
    public LiveData<ClassListResult> getClassListLiveData() {
        return classRepo.getClassListLiveData();
    }
    public void deleteClass(Classe classe){
        classRepo.deleteClass(classe);
    }
    public LiveData<ClassResult> getDeleteClassLiveData() {
        return classRepo.getClassDeleteLiveData();
    }

    public void insertClass(Classe classe){
        classRepo.insertClass(classe);
    }
    public LiveData<ClassResult> getInsertClassLiveData() {
        return classRepo.getClassInsertLiveData();
    }
}