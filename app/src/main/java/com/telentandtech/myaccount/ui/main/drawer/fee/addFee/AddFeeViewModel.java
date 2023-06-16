package com.telentandtech.myaccount.ui.main.drawer.fee.addFee;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.telentandtech.myaccount.database.dao.FeesDao;
import com.telentandtech.myaccount.database.entityes.Fees;
import com.telentandtech.myaccount.database.resultObjects.ClassListResult;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.FeesResult;
import com.telentandtech.myaccount.database.resultObjects.GroupListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.resultObjects.GroupNameIDListResult;
import com.telentandtech.myaccount.repository.ClassRepo;
import com.telentandtech.myaccount.repository.FeesRepo;
import com.telentandtech.myaccount.repository.GroupRepo;

public class AddFeeViewModel extends AndroidViewModel {

    private FeesRepo feesRepo;
    private ClassRepo classRepo;
    private GroupRepo groupRepo;

    public AddFeeViewModel(@NonNull Application application) {
        super(application);
        feesRepo = new FeesRepo(application);
        classRepo = new ClassRepo(application);
        groupRepo = new GroupRepo(application);
    }

    public void insertFees(Fees fees) {
        feesRepo.insertFees(fees);
    }
    public LiveData<FeesResult> getInsertFeesLiveData() {
        return feesRepo.getFeesInsertLiveData();
    }

    public void getClassList(String uid) {
        classRepo.getAllClassesList(uid);
    }

    public LiveData<ClassListResult> getClassListLiveData() {
        return classRepo.getClassListLiveData();
    }
    public void getGroupList(String uid, long class_id) {
        groupRepo.getGroupByActive(uid, class_id, true);
    }
    public LiveData<GroupListResult> getGroupListLiveData() {
        return groupRepo.getGroupListLiveData();
    }
}