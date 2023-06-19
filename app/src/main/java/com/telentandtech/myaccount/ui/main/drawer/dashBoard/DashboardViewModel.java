package com.telentandtech.myaccount.ui.main.drawer.dashBoard;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.telentandtech.myaccount.database.resultObjects.AttendanceCount;
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
    public void getAttendanceCount(String uid) {
        dashBoardRepo.getAttendanceCount(uid);
    }
    public void getDuePaidCount(String uid) {
        dashBoardRepo.getPaidUnpaidCount(uid);
    }

    public void paidDueCountByMonth(String uid) {
        dashBoardRepo.getPaidUnpaidByMonth(uid);
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
}