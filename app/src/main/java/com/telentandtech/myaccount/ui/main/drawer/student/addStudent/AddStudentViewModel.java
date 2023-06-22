package com.telentandtech.myaccount.ui.main.drawer.student.addStudent;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.resultObjects.ClassListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupListResult;
import com.telentandtech.myaccount.database.resultObjects.StudentResult;
import com.telentandtech.myaccount.repository.ClassRepo;
import com.telentandtech.myaccount.repository.GroupRepo;
import com.telentandtech.myaccount.repository.StudentRepo;

public class AddStudentViewModel extends AndroidViewModel {

    private StudentRepo studentRepo;
    private ClassRepo classRepo;
    private GroupRepo groupRepo;

    public AddStudentViewModel(@NonNull Application application) {
        super(application);
        studentRepo = new StudentRepo(application);
        classRepo = new ClassRepo(application);
        groupRepo = new GroupRepo(application);
    }

    public void insert(Students students) {
        studentRepo.insertStudent(students);
    }
    public LiveData<StudentResult> getInsertLiveData() {
        return studentRepo.getInsertStudentMutableLiveData();
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
    public void getStudentCount(String uid, long class_id, long group_id) {
        studentRepo.getStudentCount( uid, class_id, group_id);
    }
    public LiveData<Integer> getStudentCountLiveData(){
        return studentRepo.getStudentCountMutableLiveData();
    }

}