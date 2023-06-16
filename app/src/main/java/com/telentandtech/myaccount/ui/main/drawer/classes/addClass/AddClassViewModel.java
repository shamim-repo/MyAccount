package com.telentandtech.myaccount.ui.main.drawer.classes.addClass;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.resultObjects.ClassResult;
import com.telentandtech.myaccount.repository.ClassRepo;

public class AddClassViewModel extends AndroidViewModel {
    private ClassRepo classRepo;

    public AddClassViewModel(@NonNull Application application) {
        super(application);
        classRepo = new ClassRepo(application);
    }

    public void insertClass(Classe classe){
        classRepo.insertClass(classe);
    }

    public LiveData<ClassResult> getInsertClassLiveData(){
        return classRepo.getClassInsertLiveData();
    }
    public LiveData<ClassResult> getClassLiveData(){
        return classRepo.getClassDeleteLiveData();
    }

}