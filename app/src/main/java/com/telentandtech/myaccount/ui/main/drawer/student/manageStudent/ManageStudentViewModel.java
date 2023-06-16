package com.telentandtech.myaccount.ui.main.drawer.student.manageStudent;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.resultObjects.ClassListResult;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameIDListResult;
import com.telentandtech.myaccount.database.resultObjects.StudentListResult;
import com.telentandtech.myaccount.database.resultObjects.StudentResult;
import com.telentandtech.myaccount.repository.ClassRepo;
import com.telentandtech.myaccount.repository.GroupRepo;
import com.telentandtech.myaccount.repository.StudentRepo;

public class ManageStudentViewModel extends AndroidViewModel {
    private StudentRepo studentRepo;
    private ClassRepo classRepo;
    private GroupRepo groupRepo;
    public ManageStudentViewModel(@NonNull Application application) {
        super(application);
        studentRepo =new StudentRepo(application);
        classRepo = new ClassRepo(application);
        groupRepo = new GroupRepo(application);
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
    public void getClassNameIDList(String uid){
        studentRepo.getClassNameIdList(uid);
    }
    public LiveData<ClassNameIdListResult> getClassNameIDListLiveData(){
        return studentRepo.getClassNameIdListMutableLiveData();
    }

    public void getGroupNameIDList(String uid,long class_id){
        studentRepo.getGroupNameIdList(uid,class_id);
    }
    public LiveData<GroupNameIDListResult> getGroupNameIDListLiveData(){
        return studentRepo.getClassNameIdMutableLiveData();
    }
    public void getStudentList(String uid,long class_id,long group_id){
        studentRepo.getStudentList(uid,class_id,group_id);
    }

    public LiveData<StudentListResult> getStudentListLiveData(){
        return studentRepo.getStudentListMutableLiveData();
    }

    public void delete(Students students){
        studentRepo.deleteStudent(students);
    }
    public LiveData<StudentResult> getDeleteLiveData(){
        return studentRepo.getDeleteStudentMutableLiveData();
    }
    public void update(Students students){
        studentRepo.updateStudent(students);
    }
    public LiveData<StudentResult> getUpdateLiveData(){
        return studentRepo.getUpdateStudentMutableLiveData();
    }
    public void insert(Students fees){
        studentRepo.insertStudent(fees);
    }
    public LiveData<StudentResult> getInsertLiveData(){
        return studentRepo.getInsertStudentMutableLiveData();
    }

    public void getStudentCount(String uid, long class_id, long group_id) {
        studentRepo.getStudentCount( uid, class_id, group_id);
    }
    public LiveData<Integer> getStudentCountLiveData(){
        return studentRepo.getStudentCountMutableLiveData();
    }
}