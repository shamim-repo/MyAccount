package com.telentandtech.myaccount.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;

import com.telentandtech.myaccount.database.dao.FeesDao;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.entityes.Fees;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.FeesListResult;
import com.telentandtech.myaccount.database.resultObjects.FeesResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.resultObjects.GroupNameIDListResult;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class FeesRepo {
    private AccountDatabase db;
    private FeesDao feesDao;
    private MutableLiveData<ClassNameIdListResult> classNameIdListLiveData;
    private MutableLiveData<GroupNameIDListResult> groupNameIdListLiveData;
    private MutableLiveData<FeesResult> feesLiveData;
    private MutableLiveData<FeesResult> feesUpdateLiveData;
    private MutableLiveData<FeesResult> feesDeleteLiveData;
    private MutableLiveData<FeesResult> feesInsertLiveData;
    private MutableLiveData<FeesListResult> feesListLiveData;
    private TaskRunner taskRunner;

    public FeesRepo(Application application) {
        this.db = AccountDatabase.getInstance(application);
        this.feesDao = db.feesDao();
        this.classNameIdListLiveData = new MutableLiveData<>();
        this.groupNameIdListLiveData = new MutableLiveData<>();
        this.feesLiveData = new MutableLiveData<>();
        this.feesUpdateLiveData = new MutableLiveData<>();
        this.feesDeleteLiveData = new MutableLiveData<>();
        this.feesInsertLiveData = new MutableLiveData<>();
        this.feesListLiveData = new MutableLiveData<>();
        this.taskRunner = new TaskRunner();
    }

    public MutableLiveData<ClassNameIdListResult> getClassNameIdListLiveData() {
        return classNameIdListLiveData;
    }
    public MutableLiveData<GroupNameIDListResult> getGroupNameIdListLiveData() {
        return groupNameIdListLiveData;
    }
    public MutableLiveData<FeesResult> getFeesLiveData() {
        return feesLiveData;
    }
    public MutableLiveData<FeesResult> getFeesUpdateLiveData() {
        return feesUpdateLiveData;
    }
    public MutableLiveData<FeesResult> getFeesDeleteLiveData() {
        return feesDeleteLiveData;
    }
    public MutableLiveData<FeesResult> getFeesInsertLiveData() {
        return feesInsertLiveData;
    }
    public MutableLiveData<FeesListResult> getFeesListLiveData() {
        return feesListLiveData;
    }

    public void getClassNameIdList(String uid){
        taskRunner.executeAsync(new ClassNameIDListCallable(uid), result -> {
            if (result != null && result.size() > 0) {
                classNameIdListLiveData.postValue(new ClassNameIdListResult(result, true, "Success"));
            } else {
                classNameIdListLiveData.postValue(new ClassNameIdListResult(null, false, "No Data Found"));
            }
        });
    }

    public void getGroupNameIdList(String uid,long class_id){
        taskRunner.executeAsync(new GroupNameIDListCallable(uid,class_id), result -> {
            if (result != null && result.size() > 0) {
                groupNameIdListLiveData.postValue(new GroupNameIDListResult(result, true, "Success"));
            } else {
                groupNameIdListLiveData.postValue(new GroupNameIDListResult(null, false, "No Data Found"));
            }
        });
    }

    public void getFeesList(String uid,long class_id,long group_id){
        taskRunner.executeAsync(new FeesListCallable(uid,class_id,group_id), result -> {
            if (result != null && result.size() > 0) {
                feesListLiveData.postValue(new FeesListResult(result, true, "Success"));
            } else {
                feesListLiveData.postValue(new FeesListResult(null, false, "No Data Found"));
            }
        });
    }


    public void insertFees(Fees fees) {
        taskRunner.executeAsync(new InsertFeesCallable(fees), result -> {
            if (result != null) {
                feesInsertLiveData.postValue(new FeesResult(result, true, "Success"));
            } else {
                feesInsertLiveData.postValue(new FeesResult(null, false, "No Data Found"));
            }
        });
    }

    public void updateFees(Fees fees) {
        taskRunner.executeAsync(new UpdateFeesCallable(fees), result -> {
            if (result != null) {
                feesUpdateLiveData.postValue(new FeesResult(result, true, "Success"));
            } else {
                feesUpdateLiveData.postValue(new FeesResult(null, false, "No Data Found"));
            }
        });
    }

    public void deleteFees(Fees fees) {
        taskRunner.executeAsync(new DeleteFeesCallable(fees), result -> {
            if (result != null) {
                feesDeleteLiveData.postValue(new FeesResult(result, true, "Success"));
            } else {
                feesDeleteLiveData.postValue(new FeesResult(null, false, "No Data Found"));
            }
        });
    }


    private class ClassNameIDListCallable implements Callable<List<ClassNameId>> {
        private String uid;

        public ClassNameIDListCallable(String uid) {
            this.uid = uid;
        }

        @Override
        public List<ClassNameId> call() throws Exception {
            return feesDao.getDistinctClassNameId(uid);
        }
    }

    private class GroupNameIDListCallable implements Callable<List<GroupNameID>> {
        private String uid;
        private long class_id;

        public GroupNameIDListCallable(String uid,long class_id) {
            this.uid = uid;
            this.class_id = class_id;
        }

        @Override
        public List<GroupNameID> call() throws Exception {
            return feesDao.getDistinctGroupNameId(uid,class_id );
        }
    }

    private class FeesListCallable implements Callable<List<Fees>> {
        private String uid;
        private long class_id;
        private long group_id;

        public FeesListCallable(String uid,long class_id,long group_id) {
            this.uid = uid;
            this.class_id = class_id;
            this.group_id = group_id;
        }

        @Override
        public List<Fees> call() throws Exception {
            return feesDao.getFeesByGroupId(uid,class_id,group_id);
        }
    }

    private class InsertFeesCallable implements Callable<Fees> {
        private Fees fees;

        public InsertFeesCallable(Fees fees) {
            this.fees = fees;
        }

        @Override
        public Fees call() throws Exception {
            feesDao.insertFees(fees);
            return fees;
        }
    }

    private class UpdateFeesCallable implements Callable<Fees> {
        private Fees fees;

        public UpdateFeesCallable(Fees fees) {
            this.fees = fees;
        }

        @Override
        public Fees call() throws Exception {
            feesDao.updateFees(fees);
            return fees;
        }
    }

    private class DeleteFeesCallable implements Callable<Fees> {
        private Fees fees;

        public DeleteFeesCallable(Fees fees) {
            this.fees = fees;
        }

        @Override
        public Fees call() throws Exception {
            feesDao.deleteFees(fees);
            return fees;
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
