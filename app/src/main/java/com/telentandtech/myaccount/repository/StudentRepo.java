package com.telentandtech.myaccount.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;

import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.database.dao.PaymentDao;
import com.telentandtech.myaccount.database.dao.StudentsDao;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.entityes.Payments;
import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.resultObjects.AddStudentPaymentList;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.resultObjects.GroupNameIDListResult;
import com.telentandtech.myaccount.database.resultObjects.StudentListResult;
import com.telentandtech.myaccount.database.resultObjects.StudentResult;

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class StudentRepo {
    private AccountDatabase db;
    private StudentsDao studentsDao;
    private PaymentDao paymentDao;
    private TaskRunner taskRunner;

    private MutableLiveData<StudentResult> studentsMutableLiveData;
    private MutableLiveData<StudentResult> insertStudentMutableLiveData;
    private MutableLiveData<StudentResult> updateStudentMutableLiveData;
    private MutableLiveData<StudentResult> deleteStudentMutableLiveData;
    private MutableLiveData<StudentListResult> studentListMutableLiveData;
    private MutableLiveData<ClassNameIdListResult> classNameIdListMutableLiveData;
    private MutableLiveData<GroupNameIDListResult> groupNameIDListMutableLiveData;
    private MutableLiveData<Integer> studentCountMutableLiveData;


    public StudentRepo(Application application){
        db = AccountDatabase.getInstance(application);
        studentsDao = db.studentsDao();
        studentsMutableLiveData = new MutableLiveData<>();
        insertStudentMutableLiveData = new MutableLiveData<>();
        updateStudentMutableLiveData = new MutableLiveData<>();
        deleteStudentMutableLiveData = new MutableLiveData<>();
        studentListMutableLiveData = new MutableLiveData<>();
        classNameIdListMutableLiveData = new MutableLiveData<>();
        groupNameIDListMutableLiveData = new MutableLiveData<>();
        studentCountMutableLiveData = new MutableLiveData<>();
        paymentDao = db.paymentsDao();
        taskRunner = new TaskRunner();
    }
    public MutableLiveData<StudentResult> getStudentsMutableLiveData() {
        return studentsMutableLiveData;
    }
    public MutableLiveData<StudentResult> getInsertStudentMutableLiveData() {
        return insertStudentMutableLiveData;
    }
    public MutableLiveData<StudentResult> getUpdateStudentMutableLiveData() {
        return updateStudentMutableLiveData;
    }
    public MutableLiveData<StudentResult> getDeleteStudentMutableLiveData() {
        return deleteStudentMutableLiveData;
    }
    public MutableLiveData<StudentListResult> getStudentListMutableLiveData() {
        return studentListMutableLiveData;
    }
    public MutableLiveData<ClassNameIdListResult> getClassNameIdListMutableLiveData() {
        return classNameIdListMutableLiveData;
    }
    public MutableLiveData<GroupNameIDListResult> getClassNameIdMutableLiveData() {
        return groupNameIDListMutableLiveData;
    }
    public MutableLiveData<Integer> getStudentCountMutableLiveData() {
        return studentCountMutableLiveData;
    }


    public void getStudentCount(String uid,long class_id,long group_id){
        taskRunner.executeAsync(new GetStudentCount(uid, class_id, group_id),result -> {
            if (result != null){
                studentCountMutableLiveData.postValue(result);
            }else {
                studentCountMutableLiveData.postValue(0);
            }
        });
    }

    public void insertStudent(Students students){
        taskRunner.executeAsync(new InsertStudent(students),result -> {
            if (result != null){
                insertStudentMutableLiveData.postValue(new
                        StudentResult(result,true,"Add Student Successfully"));
            }else {
                insertStudentMutableLiveData.postValue(new
                        StudentResult(null,false,"Add Student Failed"));
            }
        });

    }

    public void updateStudent(Students students){
        taskRunner.executeAsync(new UpdateStudent(students),result -> {
            if (result != null){
                updateStudentMutableLiveData.postValue(new
                        StudentResult(result,true,"Update Successful"));
            }else {
                updateStudentMutableLiveData.postValue(new
                        StudentResult(null,false,"Update Failed"));
            }
        });
    }

    public void deleteStudent(Students students){
        taskRunner.executeAsync(new DeleteStudent(students),result -> {
            if (result != null){
                deleteStudentMutableLiveData.postValue(new
                        StudentResult(result,true,"Delete Successful"));
            }else {
                deleteStudentMutableLiveData.postValue(new
                        StudentResult(null,false,"Delete Failed"));
            }
        });
    }

    public void getStudentList(String uid,long class_id,long group_id){
        taskRunner.executeAsync(new GetStudentList(uid,class_id,group_id),result -> {
            if (result != null && result.size() > 0){
                Log.d("StudentRepo","Student List Size: "+result.size());
                studentListMutableLiveData.postValue(new
                        StudentListResult(result,true,"Getting Student List Successfully"));
            }else {
                Log.d("StudentRepo","Student Else: "+result.size());
                studentListMutableLiveData.postValue(new
                        StudentListResult(null,false," Student List Failed"));
            }
        });
    }

    public void getClassNameIdList(String uid){
        taskRunner.executeAsync(new GetClassNameIdList(uid),result -> {
            if (result != null && result.size() > 0){
                classNameIdListMutableLiveData.postValue(new
                        ClassNameIdListResult(result,true,"Getting Class List Successful"));
            }else {
                classNameIdListMutableLiveData.postValue(new
                        ClassNameIdListResult(null,false,"Getting Class List Failed"));
            }
        });
    }

    public void getGroupNameIdList(String uid,long class_id){
        taskRunner.executeAsync(new GetGroupNameIdList(uid,class_id),result -> {
            if (result != null && result.size() > 0){
                groupNameIDListMutableLiveData.postValue(new
                        GroupNameIDListResult(result,true,"Getting Group List Successful"));
            }else {
                groupNameIDListMutableLiveData.postValue(new
                        GroupNameIDListResult(null,false,"Getting Group List Failed"));
            }
        });
    }


    public void getAllStudents(String uid){
        taskRunner.executeAsync(new GetAllStudents(uid),result -> {
            if (result != null && result.size() > 0){
                studentListMutableLiveData.postValue(new
                        StudentListResult(result,true,"Getting Student List Successfully"));
            }else {
                studentListMutableLiveData.postValue(new
                        StudentListResult(null,false,"Getting Student List Failed"));
            }
        });
    }

    public void getStudentByYear(String uid, long fYear,long lYear){
        taskRunner.executeAsync(new GetStudentByYear(uid,fYear,lYear),result -> {
            if (result != null && result.size() > 0){
                studentListMutableLiveData.postValue(new
                        StudentListResult(result,true,"Getting Student List Successfully"));
            }else {
                studentListMutableLiveData.postValue(new
                        StudentListResult(null,false,"Getting Student List Failed"));
            }
        });
    }


    private class GetStudentByYear implements Callable<List<Students>> {
        private String uid;
        private long fYear;
        private long lYear;

        public GetStudentByYear(String uid, long fYear, long lYear) {
            this.uid = uid;
            this.fYear = fYear;
            this.lYear = lYear;
        }

        @Override
        public List<Students> call() throws Exception {
            return studentsDao.getStudentByYear(uid,fYear,lYear);
        }
    }

    private class GetAllStudents implements Callable<List<Students>> {
        private String uid;

        public GetAllStudents(String uid) {
            this.uid = uid;
        }

        @Override
        public List<Students> call() throws Exception {
            return studentsDao.getAllStudents(uid);
        }
    }

    private class GetStudentCount implements Callable<Integer> {
        private String uid;
        private long class_id;
        private long group_id;

        public GetStudentCount(String uid, long class_id, long group_id) {
            this.uid = uid;
            this.class_id = class_id;
            this.group_id = group_id;
        }

        @Override
        public Integer call() throws Exception {
            return studentsDao.getStudentCount(uid,class_id,group_id);
        }
    }

    private class InsertStudent implements Callable<Students> {
        private Students students;

        public InsertStudent(Students students) {
            this.students = students;
        }

        @Override
        public Students call() throws Exception {

            studentsDao.insertStudents(students);
            List<Students> studentsList = studentsDao.getStudentsByStudentIdGCC(students.getUid()
                    ,students.getId(),students.getClass_id(),students.getGroup_id(),students.getCreated_at());

            if (studentsList != null && studentsList.size() > 0){
                 students=studentsList.get(0);
                for (AddStudentPaymentList pay: paymentDao.getAddStudentPaymentList(students.getUid()
                        ,students.getGroup_id(),students.getStarting_date())){
                    Payments payments = new Payments();
                    payments.setUid(students.getUid());
                    payments.setClass_id(students.getClass_id());
                    payments.setGroup_id(students.getGroup_id());
                    payments.setStudent_id(students.getStudent_id());
                    payments.setPayment_month(pay.getPayment_month());
                    payments.setPayment_amount(pay.getPayment_amount());
                    payments.setPayment_status(false);
                    payments.setClass_name(students.getClass_name());
                    payments.setGroup_name(students.getGroup_name());
                    payments.setStudent_name(students.getFull_name());
                    payments.setGuardian_name(students.getGuardian_name());
                    payments.setPhone(students.getPhone());
                    payments.setId(students.getId());
                    payments.setStudent_id(students.getStudent_id());
                }
            }
            return  students;
        }
    }

    private class UpdateStudent implements Callable<Students> {

        private Students students;

        public UpdateStudent(Students students) {
            this.students = students;
        }

        @Override
        public Students call() throws Exception {
             studentsDao.updateStudents(students);
             paymentDao.deletePaymentByPaymentMonthAndGroupIdAndStudentId(students.getUid(),
                    students.getStarting_date() ,students.getStudent_id());

            for (AddStudentPaymentList pay: paymentDao.getAddStudentPaymentList(students.getUid()
                    ,students.getGroup_id(),students.getStarting_date())){
                Payments payments = new Payments();
                payments.setUid(students.getUid());
                payments.setClass_id(students.getClass_id());
                payments.setGroup_id(students.getGroup_id());
                payments.setStudent_id(students.getStudent_id());
                payments.setPayment_month(pay.getPayment_month());
                payments.setPayment_amount(pay.getPayment_amount());
                payments.setPayment_status(false);
                payments.setClass_name(students.getClass_name());
                payments.setGroup_name(students.getGroup_name());
                payments.setStudent_name(students.getFull_name());
                payments.setGuardian_name(students.getGuardian_name());
                payments.setPhone(students.getPhone());
                payments.setId(students.getId());
                payments.setStudent_id(students.getStudent_id());
            }

             return  students;
        }
    }

    private class DeleteStudent implements Callable<Students> {
        private Students students;

        public DeleteStudent(Students students) {
            this.students = students;
        }

        @Override
        public Students call() throws Exception {
             studentsDao.deleteStudents(students);
                paymentDao.deletePaymentByPaymentMonthAndGroupIdAndStudentId(students.getUid(),
                        DateObj.monthYearToLong(new Timestamp(System.currentTimeMillis()))
                        ,students.getStudent_id());
             return  students;
        }
    }

    private class GetStudentList implements Callable<List<Students>> {
        private String uid;
        private long class_id;
        private long group_id;

        public GetStudentList(String uid, long class_id, long group_id) {
            this.uid = uid;
            this.class_id = class_id;
            this.group_id = group_id;
        }

        @Override
        public List<Students> call() throws Exception {
            return studentsDao.getStudentsByClassIdGroupId(uid,class_id,group_id);
        }
    }

    private class GetClassNameIdList implements Callable<List<ClassNameId>> {
        private String uid;

        public GetClassNameIdList(String uid) {
            this.uid = uid;
        }

        @Override
        public List<ClassNameId> call() throws Exception {
            return studentsDao.getDistinctClassNames(uid);
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
            return studentsDao.getDistinctGroupNames(uid,class_id);
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
