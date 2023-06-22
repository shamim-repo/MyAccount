package com.telentandtech.myaccount.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.telentandtech.myaccount.database.dao.AttendanceDao;
import com.telentandtech.myaccount.database.dao.GroupDao;
import com.telentandtech.myaccount.database.dao.PaymentDao;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.resultObjects.AttendanceCount;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.resultObjects.PaidUnpaidByMonth;
import com.telentandtech.myaccount.database.resultObjects.PaidUnpaidCountResult;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class DashBoardRepo {

    private AccountDatabase db;
    private PaymentDao paymentDao;
    private AttendanceDao attendanceDao;
    private GroupDao groupDao;
    private TaskRunner taskRunner;
    private MutableLiveData<List<PaidUnpaidByMonth>> paidUnpaidByMonthLiveData;
    private MutableLiveData<PaidUnpaidCountResult> paidUnpaidCountMutableLiveData;
    private MutableLiveData<AttendanceCount> attendanceCountLiveData;
    private MutableLiveData<List<GroupNameID>> groupListAllLiveData;





    public DashBoardRepo(Application application) {
        db = AccountDatabase.getInstance(application);
        paymentDao = db.paymentsDao();
        attendanceDao = db.attendanceDao();
        taskRunner = new TaskRunner();
        paidUnpaidByMonthLiveData = new MutableLiveData<>();
        paidUnpaidCountMutableLiveData = new MutableLiveData<>();
        attendanceCountLiveData = new MutableLiveData<>();
        groupDao = db.groupDao();
        groupListAllLiveData = new MutableLiveData<>();

    }


    public void getPaidUnpaidCount(String uid, long year, long group_id, boolean all) {
        taskRunner.executeAsync(new GetPaidUnpaidCount(uid,year,group_id,all), result ->
                paidUnpaidCountMutableLiveData.postValue(result));
    }
    public void getGroupListAll(String uid, long year) {
        taskRunner.executeAsync(new GetGroupListAll(uid,year), result ->
                groupListAllLiveData.postValue(result));
    }
    public MutableLiveData<List<GroupNameID>> getGroupListAllLiveData() {
        return groupListAllLiveData;
    }
    public MutableLiveData<PaidUnpaidCountResult> getPaidUnpaidCountMutableLiveData() {
        return paidUnpaidCountMutableLiveData;
    }



    public void getAttendanceCount(String uid, long year, long group_id, boolean all) {
        taskRunner.executeAsync(new GetAttendanceCount(uid,year,group_id,all), result ->{
                if(result.getTotal_count() != null)
                    Log.d("DashboardRepo", "getAttendanceCount: "+result.getTotal_count());
                attendanceCountLiveData.postValue(result);
        });
    }

    public MutableLiveData<AttendanceCount> getAttendanceCountLiveData() {
        return attendanceCountLiveData;
    }

    public void getPaidUnpaidByMonth(String uid, long year, long group_id, boolean all) {
        taskRunner.executeAsync(new GetPaidUnpaidByMonth(uid,year,group_id,all), result ->{
                Log.d("DashboardRepo", "getAttendanceCount: ");
                paidUnpaidByMonthLiveData.postValue(result);});
    }

    public MutableLiveData<List<PaidUnpaidByMonth>> getPaidUnpaidByMonthLiveData() {
        return paidUnpaidByMonthLiveData;
    }

    private class GetAttendanceCount implements Callable<AttendanceCount> {
        private String uid;
        private long year;
        private long group_id;
        private boolean all;

        public GetAttendanceCount(String uid, long year, long group_id, boolean all) {
            this.uid = uid;
            this.year = year;
            this.group_id = group_id;
            this.all = all;
        }

        @Override
        public AttendanceCount call() throws Exception {
            if (all)
                return attendanceDao.getAttendanceCountAll(uid,year);
            else
                return attendanceDao.getAttendanceCount(uid,group_id,year);
        }
    }

    private class GetPaidUnpaidByMonth implements Callable<List<PaidUnpaidByMonth>> {
        private String uid;
        private long year;
        private long group_id;
        private boolean all;

        public GetPaidUnpaidByMonth(String uid, long year, long group_id, boolean all) {
            this.uid = uid;
            this.year = year;
            this.group_id = group_id;
            this.all = all;
        }

        @Override
        public List<PaidUnpaidByMonth> call() throws Exception {
            if (all)
                return paymentDao.getPaidDueCountByMonthAll(uid,year);
            else
                return paymentDao.getPaidDueCountByMonth(uid,group_id,year);
        }
    }


    private class GetPaidUnpaidCount implements Callable<PaidUnpaidCountResult> {
        private String uid;
        private long year;
        private long group_id;
        private boolean all;

        public GetPaidUnpaidCount(String uid, long year, long group_id, boolean all) {
            this.uid = uid;
            this.year = year;
            this.group_id = group_id;
            this.all = all;
        }

        @Override
        public PaidUnpaidCountResult call() throws Exception {
            Log.d("DashboardRepo", "call: "+uid+" "+year);
            if (all)
                return paymentDao.getPaidUnpaidCountAll(uid,year);
            else
                return paymentDao.getPaidUnpaidCount(uid,group_id,year);
        }
    }

    private class GetGroupListAll implements Callable<List<GroupNameID>> {
        private String uid;
        private long year;

        public GetGroupListAll(String uid, long year) {
            this.uid = uid;
            this.year = year;
        }

        @Override
        public List<GroupNameID> call() throws Exception {
            return groupDao.getGroupAllNameIDList(uid,year);
        }
    }

    private class TaskRunner {
        private final Executor executor = Executors.newSingleThreadExecutor(); // change according to your requirements
        private final Handler handler = new Handler(Looper.getMainLooper());

        public interface Callback<R> {
            void onComplete(R result);
        }

        public <R> void executeAsync(Callable<R> callable, TaskRunner.Callback<R> callback) {
            executor.execute(() -> {
                final R result;
                try {
                    result = callable.call();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                handler.post(() -> {
                    callback.onComplete(result);
                });

            });
        }
    }

}
