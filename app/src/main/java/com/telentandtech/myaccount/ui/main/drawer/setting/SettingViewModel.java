package com.telentandtech.myaccount.ui.main.drawer.setting;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telentandtech.myaccount.database.entityes.Attendance;
import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.resultObjects.AttendanceListResult;
import com.telentandtech.myaccount.database.resultObjects.PaymentsListResult;
import com.telentandtech.myaccount.database.resultObjects.StudentListResult;
import com.telentandtech.myaccount.repository.AttendanceRepo;
import com.telentandtech.myaccount.repository.PaymentRepo;
import com.telentandtech.myaccount.repository.StudentRepo;

import java.util.List;

public class SettingViewModel extends AndroidViewModel {
    private StudentRepo studentRepo;
    private PaymentRepo paymentRepo;
    private AttendanceRepo attendanceRepo;

    public SettingViewModel(@NonNull Application application) {
        super(application);
        studentRepo=new StudentRepo(application);
        paymentRepo=new PaymentRepo(application);
        attendanceRepo=new AttendanceRepo(application);
    }

    public void getStudent(String uid){
        studentRepo.getAllStudents(uid);
    }
    public LiveData<StudentListResult> getAllStudentList(){
       return studentRepo.getStudentListMutableLiveData();
    }

    public void getPayment(String uid){
        paymentRepo.getAllPayment(uid);
    }
    public LiveData<PaymentsListResult> getAllPaymentList(){
        return paymentRepo.getPaymentsListResultMutableLiveData();
    }

    public void getAttendance(String uid){
        attendanceRepo.getAllAttendanceList(uid);
    }
    public LiveData<AttendanceListResult> getAllAttendanceList(){
        return attendanceRepo.getAttendanceListMutableLiveData();
    }
}
