package com.telentandtech.myaccount.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.telentandtech.myaccount.database.dao.AttendanceDao;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.entityes.Attendance;
import com.telentandtech.myaccount.database.resultObjects.AttendanceListResult;
import com.telentandtech.myaccount.database.resultObjects.AttendanceResult;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.resultObjects.GroupNameIDListResult;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AttendanceRepo {
    private AccountDatabase db;
    private AttendanceDao attendanceDao;
    private TaskRunner taskRunner;

    private MutableLiveData<AttendanceResult> attendanceMutableLiveData;
    private MutableLiveData<AttendanceResult> insertAttendanceMutableLiveData;
    private MutableLiveData<AttendanceResult> updateAttendanceMutableLiveData;
    private MutableLiveData<AttendanceResult> deleteAttendanceMutableLiveData;
    private MutableLiveData<AttendanceListResult> attendanceListLiveData;
    private MutableLiveData<ClassNameIdListResult> classNameIdListMutableLiveData;
    private MutableLiveData<GroupNameIDListResult> groupNameIDListMutableLiveData;


    public AttendanceRepo(Application application) {
        db = AccountDatabase.getInstance(application);
        attendanceDao = db.attendanceDao();
        attendanceMutableLiveData = new MutableLiveData<>();
        insertAttendanceMutableLiveData = new MutableLiveData<>();
        updateAttendanceMutableLiveData = new MutableLiveData<>();
        deleteAttendanceMutableLiveData = new MutableLiveData<>();
        attendanceListLiveData = new MutableLiveData<>();
        classNameIdListMutableLiveData = new MutableLiveData<>();
        groupNameIDListMutableLiveData = new MutableLiveData<>();
        taskRunner = new AttendanceRepo.TaskRunner();
    }

    public MutableLiveData<AttendanceResult> getAttendanceMutableLiveData() {
        return attendanceMutableLiveData;
    }

    public MutableLiveData<AttendanceResult> getInsertAttendanceMutableLiveData() {
        return insertAttendanceMutableLiveData;
    }

    public MutableLiveData<AttendanceResult> getUpdateAttendanceMutableLiveData() {
        return updateAttendanceMutableLiveData;
    }

    public MutableLiveData<AttendanceResult> getDeleteAttendanceMutableLiveData() {
        return deleteAttendanceMutableLiveData;
    }

    public MutableLiveData<AttendanceListResult> getAttendanceListMutableLiveData() {
        return attendanceListLiveData;
    }

    public MutableLiveData<ClassNameIdListResult> getClassNameIdListMutableLiveData() {
        return classNameIdListMutableLiveData;
    }

    public MutableLiveData<GroupNameIDListResult> getClassNameIdMutableLiveData() {
        return groupNameIDListMutableLiveData;
    }

    public void insertAttendance(Attendance attendance) {
        taskRunner.executeAsync(new InsertAttendance(attendance), result -> {
            if (result != null) {
                insertAttendanceMutableLiveData.postValue(new
                        AttendanceResult(result, true, "Add Attendance Successfully"));
            } else {
                insertAttendanceMutableLiveData.postValue(new
                        AttendanceResult(null, false, "Add Attendance Failed"));
            }
        });

    }

    public void insertAttendanceList(List<Attendance> attendanceList) {
        taskRunner.executeAsync(new InsertAttendanceList(attendanceList), result -> {
            if (result != null && result) {
                Attendance attendance=attendanceList.get(0);
                Log.d("AttendanceRepo", "insertAttendanceList: "+attendance.getClass_name());
                getAttendanceList(attendance.getUid(), attendance.getGroup_id()
                        ,attendance.getDate());
            } else {
                Log.d("AttendanceRepo", "Add Attendance List Failed: ");
                attendanceListLiveData.postValue(new
                        AttendanceListResult(null, false, "Add Attendance List Failed"));
            }
        });

    }

    public void updateAttendance(Attendance attendance) {
        taskRunner.executeAsync(new AttendanceRepo.UpdateAttendance(attendance), result -> {
            if (result != null) {
                updateAttendanceMutableLiveData.postValue(new
                        AttendanceResult(result, true, "Update Successful"));
            } else {
                updateAttendanceMutableLiveData.postValue(new
                        AttendanceResult(null, false, "Update Failed"));
            }
        });
    }

    public void deleteAttendance(Attendance attendance) {
        taskRunner.executeAsync(new AttendanceRepo.DeleteAttendance(attendance), result -> {
            if (result != null) {
                deleteAttendanceMutableLiveData.postValue(new
                        AttendanceResult(result, true, "Delete Successful"));
            } else {
                deleteAttendanceMutableLiveData.postValue(new
                        AttendanceResult(null, false, "Delete Failed"));
            }
        });
    }

    public void getAttendanceList(String uid, long group_id,long date) {
        taskRunner.executeAsync(new GetAttendanceList(uid,group_id,date), result -> {
            if (result != null && result.size() > 0) {
                 attendanceListLiveData.postValue(new
                        AttendanceListResult(result, true, "Getting Attendance List Successful"));
            } else if (result != null && result.size() == 0) {
                attendanceListLiveData.postValue(new
                        AttendanceListResult(null, true, "No Attendance Found"));
            }else {
                 attendanceListLiveData.postValue(new
                        AttendanceListResult(null, false, "Getting Attendance List Failed"));
            }
        });
    }

    public void getClassNameIdList(String uid) {
        taskRunner.executeAsync(new AttendanceRepo.GetClassNameIdList(uid), result -> {
            if (result != null && result.size() > 0) {
                classNameIdListMutableLiveData.postValue(new
                        ClassNameIdListResult(result, true, "Getting Class List Successful"));
            } else {
                classNameIdListMutableLiveData.postValue(new
                        ClassNameIdListResult(null, false, "Getting Class List Failed"));
            }
        });
    }

    public void getAllAttendanceList(String uid) {
        taskRunner.executeAsync(new GetAllAttendanceList(uid), result -> {
            if (result != null && result.size() > 0) {
                attendanceListLiveData.postValue(new
                        AttendanceListResult(result, true, "Getting Attendance List Successful"));
            } else {
                attendanceListLiveData.postValue(new
                        AttendanceListResult(null, false, "Getting Attendance List Failed"));
            }
        });
    }

    public void getGroupNameIdList(String uid, long class_id) {
        taskRunner.executeAsync(new AttendanceRepo.GetGroupNameIdList(uid, class_id), result -> {
            if (result != null && result.size() > 0) {
                groupNameIDListMutableLiveData.postValue(new
                        GroupNameIDListResult(result, true, "Getting Group List Successful"));
            } else {
                groupNameIDListMutableLiveData.postValue(new
                        GroupNameIDListResult(null, false, "Getting Group List Failed"));
            }
        });
    }


    private class GetAllAttendanceList implements Callable<List<Attendance>> {
        private String uid;

        public GetAllAttendanceList(String uid) {
            this.uid = uid;
        }

        @Override
        public List<Attendance> call() throws Exception {
            return attendanceDao.getAllAttendance(uid);
        }
    }

    private class InsertAttendance implements Callable<Attendance> {
        private Attendance attendance;

        public InsertAttendance(Attendance attendance) {
            this.attendance = attendance;
        }

        @Override
        public Attendance call() throws Exception {
            attendanceDao.insertAttendance(attendance);
            return attendance;
        }
    }

    private class InsertAttendanceList implements Callable<Boolean> {
        private List<Attendance> attendanceList;

        public InsertAttendanceList(List<Attendance> attendance) {
            this.attendanceList = attendance;
        }

        @Override
        public Boolean call() throws Exception {
            attendanceDao.insertAttendanceList(attendanceList);
            return true;
        }
    }
    private class UpdateAttendance implements Callable<Attendance> {

        private Attendance attendance;

        public UpdateAttendance(Attendance attendance) {
            this.attendance = attendance;
        }

        @Override
        public Attendance call() throws Exception {
            attendanceDao.updateAttendance(attendance);
            return attendance;
        }
    }

    private class DeleteAttendance implements Callable<Attendance> {
        private Attendance attendance;

        public DeleteAttendance(Attendance attendance) {
            this.attendance = attendance;
        }

        @Override
        public Attendance call() throws Exception {
            attendanceDao.deleteAttendance(attendance);
            return attendance;
        }
    }

    private class GetAttendanceList implements Callable<List<Attendance>> {
        private String uid;
        private long group_id;
        private long date;

        public GetAttendanceList(String uid, long group_id,long date) {
            this.uid = uid;
            this.group_id = group_id;
            this.date=date;
        }

        @Override
        public List<Attendance> call() throws Exception {
            return attendanceDao.getAttendanceByDate(uid,group_id,date);
        }
    }

    private class GetClassNameIdList implements Callable<List<ClassNameId>> {
        private String uid;

        public GetClassNameIdList(String uid) {
            this.uid = uid;
        }

        @Override
        public List<ClassNameId> call() throws Exception {
            return attendanceDao.getClassNameIDList(uid);
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
            return attendanceDao.getGroupNameIDList(uid, class_id);
        }
    }

    private class TaskRunner {
        private final Executor executor = Executors.newSingleThreadExecutor(); // change according to your requirements
        private final Handler handler = new Handler(Looper.getMainLooper());

        public interface Callback<R> {
            void onComplete(R result);
        }

        public <R> void executeAsync(Callable<R> callable, AttendanceRepo.TaskRunner.Callback<R> callback) {
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
