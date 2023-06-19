package com.telentandtech.myaccount.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.telentandtech.myaccount.database.dao.AttendanceDao;
import com.telentandtech.myaccount.database.dao.FeesDao;
import com.telentandtech.myaccount.database.dao.GroupDao;
import com.telentandtech.myaccount.database.dao.PaymentDao;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.entityes.Attendance;
import com.telentandtech.myaccount.database.entityes.Fees;
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
    private TaskRunner taskRunner;
    private MutableLiveData<List<PaidUnpaidByMonth>> paidUnpaidByMonthLiveData;
    private MutableLiveData<PaidUnpaidCountResult> paidUnpaidCountMutableLiveData;
    private MutableLiveData<AttendanceCount> attendanceCountLiveData;





    public DashBoardRepo(Application application) {
        db = AccountDatabase.getInstance(application);
        paymentDao = db.paymentsDao();
        attendanceDao = db.attendanceDao();
        taskRunner = new TaskRunner();
        paidUnpaidByMonthLiveData = new MutableLiveData<>();
        paidUnpaidCountMutableLiveData = new MutableLiveData<>();
        attendanceCountLiveData = new MutableLiveData<>();

    }


    public void getPaidUnpaidCount(String uid) {
        taskRunner.executeAsync(new GetPaidUnpaidCount(uid), result ->
                paidUnpaidCountMutableLiveData.postValue(result));
    }

    public MutableLiveData<PaidUnpaidCountResult> getPaidUnpaidCountMutableLiveData() {
        return paidUnpaidCountMutableLiveData;
    }

    public void getAttendanceCount(String uid) {
        taskRunner.executeAsync(new GetAttendanceCount(uid), result ->{
                if(result.getTotal_count() != null)
                    Log.d("DashboardRepo", "getAttendanceCount: "+result.getTotal_count());
                attendanceCountLiveData.postValue(result);
        });
    }

    public MutableLiveData<AttendanceCount> getAttendanceCountLiveData() {
        return attendanceCountLiveData;
    }

    public void getPaidUnpaidByMonth(String uid) {
        taskRunner.executeAsync(new GetPaidUnpaidByMonth(uid), result ->
                paidUnpaidByMonthLiveData.postValue(result));
    }

    public MutableLiveData<List<PaidUnpaidByMonth>> getPaidUnpaidByMonthLiveData() {
        return paidUnpaidByMonthLiveData;
    }

    private class GetAttendanceCount implements Callable<AttendanceCount> {
        private String uid;

        public GetAttendanceCount(String uid) {
            this.uid = uid;
        }

        @Override
        public AttendanceCount call() throws Exception {
            return attendanceDao.getAttendanceCount(uid);
        }
    }

    private class GetPaidUnpaidByMonth implements Callable<List<PaidUnpaidByMonth>> {
        private String uid;

        public GetPaidUnpaidByMonth(String uid) {
            this.uid = uid;
        }

        @Override
        public List<PaidUnpaidByMonth> call() throws Exception {
            return paymentDao.getPaidDueCountByMonth(uid);
        }
    }


    private class GetPaidUnpaidCount implements Callable<PaidUnpaidCountResult> {
        private String uid;

        public GetPaidUnpaidCount(String uid) {
            this.uid = uid;
        }

        @Override
        public PaidUnpaidCountResult call() throws Exception {
            return paymentDao.getPaidUnpaidCount(uid);
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
