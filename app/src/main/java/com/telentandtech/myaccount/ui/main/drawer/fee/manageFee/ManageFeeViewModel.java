package com.telentandtech.myaccount.ui.main.drawer.fee.manageFee;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.telentandtech.myaccount.database.dao.FeesDao;
import com.telentandtech.myaccount.database.entityes.Fees;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.FeesListResult;
import com.telentandtech.myaccount.database.resultObjects.FeesResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameIDListResult;
import com.telentandtech.myaccount.repository.FeesRepo;

public class ManageFeeViewModel extends AndroidViewModel {


    private FeesRepo feesRepo;
    public ManageFeeViewModel(@NonNull Application application) {
        super(application);
        feesRepo=new FeesRepo(application);
    }
    public void getClassNameIDList(String uid){
        feesRepo.getClassNameIdList(uid);
    }
    public LiveData<ClassNameIdListResult> getClassNameIDListLiveData(){
        return feesRepo.getClassNameIdListLiveData();
    }

    public void getGroupNameIDList(String uid,long class_id){
        feesRepo.getGroupNameIdList(uid,class_id);
    }
    public LiveData<GroupNameIDListResult> getGroupNameIDListLiveData(){
        return feesRepo.getGroupNameIdListLiveData();
    }
    public void getFeesList(String uid,long class_id,long group_id){
        feesRepo.getFeesList(uid,class_id,group_id);
    }

    public LiveData<FeesListResult> getFeesListLiveData(){
        return feesRepo.getFeesListLiveData();
    }

    public void deleteFee(Fees fees){
        feesRepo.deleteFees(fees);
    }
    public LiveData<FeesResult> getDeleteLiveData(){
        return feesRepo.getFeesDeleteLiveData();
    }
    public void updateFee(Fees fees){
        feesRepo.updateFees(fees);
    }
    public LiveData<FeesResult> getUpdateLiveData(){
        return feesRepo.getFeesUpdateLiveData();
    }
    public void insertFee(Fees fees){
        feesRepo.insertFees(fees);
    }
    public LiveData<FeesResult> getInsertLiveData(){
        return feesRepo.getFeesInsertLiveData();
    }

}