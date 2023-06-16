package com.telentandtech.myaccount.ui.main.drawer.attentence;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telentandtech.myaccount.database.entityes.Attendance;
import com.telentandtech.myaccount.database.resultObjects.AttendanceListResult;
import com.telentandtech.myaccount.database.resultObjects.AttendanceResult;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameIDListResult;
import com.telentandtech.myaccount.database.resultObjects.StudentListResult;
import com.telentandtech.myaccount.repository.AttendanceRepo;
import com.telentandtech.myaccount.repository.ClassRepo;
import com.telentandtech.myaccount.repository.GroupRepo;
import com.telentandtech.myaccount.repository.StudentRepo;

import java.util.List;

public class MangeAttendanceViewModel extends AndroidViewModel {
    private AttendanceRepo attendanceRepo;
    private ClassRepo classRepo;
    private GroupRepo groupRepo;
    private StudentRepo studentRepo;

    public MangeAttendanceViewModel(@NonNull Application application) {
        super(application);
        attendanceRepo = new AttendanceRepo(application);
        classRepo = new ClassRepo(application);
        groupRepo = new GroupRepo(application);
        studentRepo = new StudentRepo(application);
    }

    public void insert(Attendance attendance) {
        attendanceRepo.insertAttendance(attendance);
    }
    public void update(Attendance attendance) {
        attendanceRepo.updateAttendance(attendance);
    }
    public LiveData<AttendanceResult> getUpdateLiveData() {
        return attendanceRepo.getUpdateAttendanceMutableLiveData();
    }
    public void insertAttendanceList(List<Attendance> attendances) {
        attendanceRepo.insertAttendanceList(attendances);
    }
    public void getAttendanceList(String uid,long group_id, long date) {
        Log.d("MangeAttendanceViewModel", "getAttendanceList: " + uid + " " + group_id + " " + date);
        attendanceRepo.getAttendanceList(uid, group_id, date);
    }
    public LiveData<AttendanceListResult> getAttendanceListLiveData() {
        Log.d("MangeAttendanceViewModel", "getAttendanceListLiveData: ");
        return attendanceRepo.getAttendanceListMutableLiveData();
    }
    public void getClassList(String uid) {
        classRepo.getClassNameId(uid);
    }
    public LiveData<ClassNameIdListResult> getClassNameIdLiveData() {
        return classRepo.getClassNameIdMutableLiveData();
    }
    public void getGroupList(String uid, long class_id) {
        groupRepo.getGroupNameIdList(uid, class_id);
    }
    public LiveData<GroupNameIDListResult> getGroupNameIdLiveData() {
        return groupRepo.getGroupNameIDListMutableLiveData();
    }

    public void getStudentList(String uid, long class_id, long group_id) {
        studentRepo.getStudentList(uid, class_id, group_id);
    }
    public LiveData<StudentListResult> getStudentListLiveData() {
        return studentRepo.getStudentListMutableLiveData();
    }


}