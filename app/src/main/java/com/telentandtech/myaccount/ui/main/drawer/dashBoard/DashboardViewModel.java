package com.telentandtech.myaccount.ui.main.drawer.dashBoard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telentandtech.myaccount.database.resultObjects.AttendanceCount;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.resultObjects.PaidUnpaidByMonth;
import com.telentandtech.myaccount.database.resultObjects.PaidUnpaidCountResult;
import com.telentandtech.myaccount.repository.DashBoardRepo;

import java.util.List;

public class DashboardViewModel extends AndroidViewModel {

    private DashBoardRepo dashBoardRepo;
    public DashboardViewModel(@NonNull Application application) {
        super(application);
        dashBoardRepo = new DashBoardRepo(application);
    }
    public void getAttendanceCount(String uid,long year,long group_id,boolean all) {
        dashBoardRepo.getAttendanceCount(uid,year,group_id,all);
    }
    public void getDuePaidCount(String uid,long year,long group_id,boolean all) {
        dashBoardRepo.getPaidUnpaidCount(uid,year,group_id,all);
    }

    public void paidDueCountByMonth(String uid,long year,long group_id,boolean all) {
        dashBoardRepo.getPaidUnpaidByMonth(uid,year,group_id,all);
    }
    public LiveData<List<PaidUnpaidByMonth>> getPaidUnpaidByMonthLiveData() {
        return dashBoardRepo.getPaidUnpaidByMonthLiveData();
    }

    public LiveData<AttendanceCount> getAttendanceCountLiveData() {
        return dashBoardRepo.getAttendanceCountLiveData();
    }
    public LiveData<PaidUnpaidCountResult> getPaidUnpaidCountLiveData() {
        return dashBoardRepo.getPaidUnpaidCountMutableLiveData();
    }

    public void getGroupListAll(String uid,long year) {
        dashBoardRepo.getGroupListAll(uid,year);
    }
    public LiveData<List<GroupNameID>> getGroupListAllLiveData() {
        return dashBoardRepo.getGroupListAllLiveData();
    }

}