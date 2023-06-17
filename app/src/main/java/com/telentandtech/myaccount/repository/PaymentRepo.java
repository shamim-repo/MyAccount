package com.telentandtech.myaccount.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.telentandtech.myaccount.database.dao.PaymentDao;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.entityes.Payments;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.resultObjects.GroupNameIDListResult;
import com.telentandtech.myaccount.database.resultObjects.PaymentsListResult;
import com.telentandtech.myaccount.database.resultObjects.PaymentsResult;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class PaymentRepo {
    private PaymentDao paymentDao;
    private AccountDatabase db;
    private TaskRunner taskRunner;

    private MutableLiveData<PaymentsResult> insertPaymentResultMutableLiveData;
    private MutableLiveData<PaymentsResult> updatePaymentResultMutableLiveData;
    private MutableLiveData<PaymentsResult> deletePaymentResultMutableLiveData;
    private MutableLiveData<PaymentsListResult> paymentsListResultMutableLiveData;
    private MutableLiveData<ClassNameIdListResult> classNameIdMutableLiveData;
    private MutableLiveData<GroupNameIDListResult> groupNameIDMutableLiveData;


    public PaymentRepo(Application application) {
        db = AccountDatabase.getInstance(application);
        paymentDao = db.paymentsDao();
        taskRunner = new TaskRunner();

        insertPaymentResultMutableLiveData = new MutableLiveData<>();
        updatePaymentResultMutableLiveData = new MutableLiveData<>();
        deletePaymentResultMutableLiveData = new MutableLiveData<>();
        paymentsListResultMutableLiveData = new MutableLiveData<>();
        classNameIdMutableLiveData = new MutableLiveData<>();
        groupNameIDMutableLiveData = new MutableLiveData<>();
    }

    public LiveData<ClassNameIdListResult> getClassNameIdMutableLiveData() {
        return classNameIdMutableLiveData;
    }

    public LiveData<GroupNameIDListResult> getGroupNameIDMutableLiveData() {
        return groupNameIDMutableLiveData;
    }
    public LiveData<PaymentsResult> getInsertPaymentResultMutableLiveData() {
        return insertPaymentResultMutableLiveData;
    }

    public LiveData<PaymentsResult> getUpdatePaymentResultMutableLiveData() {
        return updatePaymentResultMutableLiveData;
    }

    public LiveData<PaymentsResult> getDeletePaymentResultMutableLiveData() {
        return deletePaymentResultMutableLiveData;
    }

    public LiveData<PaymentsListResult> getPaymentsListResultMutableLiveData() {
        return paymentsListResultMutableLiveData;
    }


    public void insertPayment(Payments payments){
        taskRunner.executeAsync(new InsertPaymentCallable(payments), result -> {
            if (result != null) {
                insertPaymentResultMutableLiveData.postValue(new PaymentsResult(result,true,"Payment Inserted"));
            } else {
                insertPaymentResultMutableLiveData.postValue(new PaymentsResult(null,false,"Error"));
            }
        });
    }

    public void updatePayment(Payments payments){
        taskRunner.executeAsync(new UpdatePaymentCallable(payments), result -> {
            if (result != null) {
                updatePaymentResultMutableLiveData.postValue(new PaymentsResult(result,true,"Payment Updated"));
            } else {
                updatePaymentResultMutableLiveData.postValue(new PaymentsResult(null,false,"Error"));
            }
        });
    }

    public void deletePayment(Payments payments){
        taskRunner.executeAsync(new DeletePaymentCallable(payments), result -> {
            if (result != null) {
                deletePaymentResultMutableLiveData.postValue(new PaymentsResult(result,true,"Payment Deleted"));
            } else {
                deletePaymentResultMutableLiveData.postValue(new PaymentsResult(null,false,"Error"));
            }
        });
    }

    public void getPaymentsByClassIdGroupIdStatus(String uid,long group_id,
                                                  long payment_date,boolean payment_status){
        taskRunner.executeAsync(new GetPaymentListByClassIdGroupIdStatusCallable(
                uid,group_id,payment_date,payment_status), result -> {
            if (result != null) {
                paymentsListResultMutableLiveData.postValue(new PaymentsListResult(result,true,"Payment List"));
            } else {
                paymentsListResultMutableLiveData.postValue(new PaymentsListResult(null,false,"Error"));
            }
        });
    }

    public void getPaymentsByClassIdGroupId(String uid, long group_id,
                                            long payment_date){
        taskRunner.executeAsync(new  GetPaymentListByClassIdGroupIdCallable(
                uid,group_id,payment_date), result -> {
            if (result != null) {
                paymentsListResultMutableLiveData.postValue(new PaymentsListResult(result,true,"Payment List"));
            } else {
                paymentsListResultMutableLiveData.postValue(new PaymentsListResult(null,false,"Error"));
            }
        });
    }

    public void getClassIdName(String uid){
        taskRunner.executeAsync(new GetClassNameIdCallable(uid), result -> {
            if (result != null) {
                classNameIdMutableLiveData.postValue( new ClassNameIdListResult(result,true,"Class List"));
            } else {
                classNameIdMutableLiveData.postValue(new ClassNameIdListResult(null,false,"Error"));
            }
        });
    }

    public void getGroupIdName(String uid, long class_id){
        taskRunner.executeAsync(new GetGroupNameIdList(uid, class_id), result -> {
            if (result != null) {
                groupNameIDMutableLiveData.postValue(new GroupNameIDListResult(result,true,"Group List"));
            } else {
                groupNameIDMutableLiveData.postValue(null);
            }
        });
    }


    private class InsertPaymentCallable implements Callable<Payments> {
        private Payments payments;

        public InsertPaymentCallable(Payments payments) {
            this.payments = payments;
        }

        @Override
        public Payments call() throws Exception {
            paymentDao.insertPayment(payments);
            return payments;
        }
    }

    private class UpdatePaymentCallable implements Callable<Payments> {
        private Payments payments;

        public UpdatePaymentCallable(Payments payments) {
            this.payments = payments;
        }

        @Override
        public Payments call() throws Exception {
            paymentDao.updatePayment(payments);
            return payments;
        }
    }

    private class DeletePaymentCallable implements Callable<Payments> {
        private Payments payments;

        public DeletePaymentCallable(Payments payments) {
            this.payments = payments;
        }

        @Override
        public Payments call() throws Exception {
            paymentDao.deletePayment(payments);
            return payments;
        }
    }

    private class GetPaymentListByClassIdGroupIdStatusCallable implements Callable<List<Payments>> {

        private String uid;
        private long groupId;
        private boolean paymentStatus;
        private long paymentDate;

        public GetPaymentListByClassIdGroupIdStatusCallable(String uid, long groupId,
                                                            long paymentDate, boolean paymentStatus) {
            this.uid = uid;
            this.groupId = groupId;
            this.paymentStatus = paymentStatus;
            this.paymentDate = paymentDate;
        }

        @Override
        public List<Payments> call() throws Exception {
            return paymentDao.getPaymentsByPaymentMonthAndStatus(uid,groupId,paymentDate,paymentStatus);
        }
    }

    private class GetPaymentListByClassIdGroupIdCallable implements Callable<List<Payments>> {

        private String uid;
        private long groupId;
        private long paymentDate;

        public GetPaymentListByClassIdGroupIdCallable(String uid, long groupId,long paymentDate) {
            this.uid = uid;
            this.groupId = groupId;
            this.paymentDate = paymentDate;
        }

        @Override
        public List<Payments> call() throws Exception {
            return paymentDao.getPaymentsByPaymentMonth(uid,groupId,paymentDate);
        }
    }

    private class GetClassNameIdCallable implements Callable<List<ClassNameId>> {

        private String uid;

        public GetClassNameIdCallable(String uid) {
            this.uid = uid;
        }

        @Override
        public List<ClassNameId> call() throws Exception {
            return paymentDao.getClassNameIDList(uid);
        }
    }

    private class GetGroupNameIdList implements Callable<List<GroupNameID>> {
        private String uid;
        private long class_id;

        public GetGroupNameIdList(String uid, long class_id) {
            this.uid = uid;
            this.class_id = class_id;
        }

        @Override
        public List<GroupNameID> call() throws Exception {
            return paymentDao.getGroupNameIDList(uid,class_id);
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
