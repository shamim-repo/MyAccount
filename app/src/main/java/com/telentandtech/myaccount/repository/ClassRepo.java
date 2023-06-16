package com.telentandtech.myaccount.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.telentandtech.myaccount.database.dao.ClassDao;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.resultObjects.ClassListResult;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.ClassResult;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ClassRepo {
    private ClassDao classDao;
    private AccountDatabase db;
    private MutableLiveData<ClassListResult> classListLiveData;
    private MutableLiveData<ClassResult> classDeleteLiveData;
    private MutableLiveData<ClassResult> classLiveData;
    private MutableLiveData<ClassResult> classInsertLiveData;
    private MutableLiveData<ClassResult> classUpdateLiveData;
    private MutableLiveData<ClassNameIdListResult> classNameIdMutableLiveData;

    private TaskRunner taskRunner;


    public ClassRepo(Application application) {
        db = AccountDatabase.getInstance(application);
        classDao = db.classDao();
        classListLiveData = new MutableLiveData<>();
        classDeleteLiveData = new MutableLiveData<>();
        classLiveData = new MutableLiveData<>();
        classInsertLiveData = new MutableLiveData<>();
        classUpdateLiveData = new MutableLiveData<>();
        classNameIdMutableLiveData = new MutableLiveData<>();


        taskRunner = new TaskRunner();
    }

    public LiveData<ClassResult> getClassDeleteLiveData() {
        return classDeleteLiveData;
    }
    public LiveData<ClassListResult> getClassListLiveData() {
        return classListLiveData;
    }
    public LiveData<ClassResult> getClassInsertLiveData() {
        return classInsertLiveData;
    }
    public LiveData<ClassResult> getClassUpdateLiveData() {
        return classUpdateLiveData;
    }
    public LiveData<ClassResult> getClassLiveData() {
        return classLiveData;
    }
    public LiveData<ClassNameIdListResult> getClassNameIdMutableLiveData() {
        return classNameIdMutableLiveData;
    }


    public void insertClass(Classe classe) {
        taskRunner.executeAsync(new InsertClassCallable(classe), result -> {
            if (result != null)
                classInsertLiveData.postValue(new ClassResult(result,true,
                        "Class added successfully"));
            else
                classInsertLiveData.postValue(new ClassResult(null,false,
                        "Error adding class"));
        });
    }
    public void updateClass(Classe classe) {
        taskRunner.executeAsync(new UpdateClassCallable(classe), result -> {
            if (result != null)
                classUpdateLiveData.postValue(new ClassResult(result,true,
                        "Class updated successfully"));
            else
                classUpdateLiveData.postValue(new ClassResult(null,false,
                        "Error updating class"));
        });
    }
    public void deleteClass(Classe classe) {
        taskRunner.executeAsync(new DeleteClassCallable(classe), result -> {
            if (result != null)
                classDeleteLiveData.postValue(new ClassResult(result,true,
                        "Class deleted successfully"));
            else
                classDeleteLiveData.postValue(new ClassResult(null,false,
                        "Error deleting class"));
        });
    }
    public void getClassById(String uid , int id) {
        taskRunner.executeAsync(new GetClassByIdCallable(uid,id), result -> {
            if (result != null)
                classLiveData.postValue(new ClassResult(result,true,
                        "Class retrieved successfully"));
            else
                classLiveData.postValue(new ClassResult(null,false,
                        "Error retrieving class"));
        });
    }
    public void getAllClassesList(String uid) {
        taskRunner.executeAsync(new GetAllClassesCallable(uid), result -> {

            if (result != null)
                classListLiveData.postValue(new ClassListResult(result,true,
                        "Classes retrieved successfully"));
            else
                classListLiveData.postValue(new ClassListResult(null,false,
                        "Error retrieving classes"));
        });
    }

    public void getClassNameId(String uid) {
        taskRunner.executeAsync(new GetClassNameIdCallable(uid), result -> {
            if (result != null)
                classNameIdMutableLiveData.postValue(new ClassNameIdListResult(
                        result,true, "Class name and id retrieved successfully"));
            else
                classNameIdMutableLiveData.postValue(new ClassNameIdListResult(
                        null,false, "Error retrieving class name and id"));
        });
    }

    private class InsertClassCallable implements Callable<Classe>{
        private Classe classe;

        public InsertClassCallable(Classe classe) {
            this.classe = classe;
        }

        @Override
        public Classe call() throws Exception {
             classDao.insertClass(classe);
             return classe;
        }

    }
    private class UpdateClassCallable implements Callable<Classe>{
        private Classe classe;

        public UpdateClassCallable(Classe classe) {
            this.classe = classe;
        }

        @Override
        public Classe call() throws Exception {
            classDao.updateClass(classe);
            return classe;
        }

    }
    private class DeleteClassCallable implements Callable<Classe>{
        private Classe classe;

        public DeleteClassCallable(Classe classe) {
            this.classe = classe;
        }

        @Override
        public Classe call() throws Exception {
            classDao.deleteClass(classe);
            return classe;
        }

    }
    private class GetClassByIdCallable implements Callable<Classe>{
        private String uid;
        private int id;

        public GetClassByIdCallable(String uid,int id) {
            this.uid = uid;
            this.id = id;
        }

        @Override
        public Classe call() throws Exception {
            return classDao.getClass(uid,id);
        }

    }
    private class GetAllClassesCallable implements Callable<List<Classe>>{
        private String uid;

        public GetAllClassesCallable(String uid) {
            this.uid = uid;
        }

        @Override
        public List<Classe> call() throws Exception {


            return classDao.getAllClassesList(uid);
        }
    }

    private class GetClassNameIdCallable implements Callable<List<ClassNameId>>{
        private String uid;

        public GetClassNameIdCallable(String uid) {
            this.uid = uid;
        }

        @Override
        public List<ClassNameId> call() throws Exception {
            return classDao.getDistinctClassesList(uid);
        }
    }

    private class TaskRunner {
        private final Executor executor = Executors.newSingleThreadExecutor(); // change according to your requirements
        private final Handler handler = new Handler(Looper.getMainLooper());

        public interface Callback<R> {
            void onComplete(R result);
        }

        public <R> void executeAsync(Callable<R> callable, Callback<R> callback) {
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
