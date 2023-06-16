package com.telentandtech.myaccount.repository;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import androidx.lifecycle.MutableLiveData;

import com.telentandtech.myaccount.database.dao.GroupDao;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.resultObjects.GroupNameIDListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupResult;

import java.sql.Timestamp;
import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class GroupRepo {

    private AccountDatabase db;
    private GroupDao groupDao;
    private TaskRunner taskRunner;

    private MutableLiveData<GroupResult> groupLiveData;
    private MutableLiveData<GroupResult> groupDeleteLiveData;
    private MutableLiveData<GroupResult> groupInsertLiveData;
    private MutableLiveData<GroupResult> groupUpdateLiveData;
    private MutableLiveData<GroupListResult> groupListLiveData;
    private MutableLiveData<ClassNameIdListResult> classNameIdListLiveData;
    private MutableLiveData<GroupNameIDListResult> groupNameIDListMutableLiveData;

    public GroupRepo(Application application) {
        db = AccountDatabase.getInstance(application);
        groupDao = db.groupDao();
        groupLiveData = new MutableLiveData<>();
        groupDeleteLiveData = new MutableLiveData<>();
        groupListLiveData = new MutableLiveData<>();
        classNameIdListLiveData = new MutableLiveData<>();
        groupInsertLiveData = new MutableLiveData<>();
        groupUpdateLiveData = new MutableLiveData<>();
        groupNameIDListMutableLiveData = new MutableLiveData<>();


        taskRunner = new TaskRunner();
    }

    public MutableLiveData<GroupResult> getGroupLiveData() {
        return groupLiveData;
    }

    public MutableLiveData<GroupResult> getGroupDeleteLiveData() {
        return groupDeleteLiveData;
    }

    public MutableLiveData<GroupListResult> getGroupListLiveData() {
        return groupListLiveData;
    }

    public MutableLiveData<ClassNameIdListResult> getClassNameIdListLiveData() {
        return classNameIdListLiveData;
    }

    public MutableLiveData<GroupResult> getGroupInsertLiveData() {
        return groupInsertLiveData;
    }

    public MutableLiveData<GroupResult> getGroupUpdateLiveData() {
        return groupUpdateLiveData;
    }

    public MutableLiveData<GroupNameIDListResult> getGroupNameIDListMutableLiveData() {
        return groupNameIDListMutableLiveData;
    }


    public void getClassNameIdList(String uid) {
        taskRunner.executeAsync(new GetClassNameIdListCallable(uid), result -> {
            if (result != null)
                classNameIdListLiveData.postValue(new ClassNameIdListResult(result,true,
                        "Class name and id list retrieved successfully"));
            else
                classNameIdListLiveData.postValue(new ClassNameIdListResult(null,false,
                        "Error retrieving class name and id list"));
        });
    }
    public void getGroupNameIdList(String uid, long class_id) {
        taskRunner.executeAsync(new GetGroupNameIdListCallable(uid, class_id), result -> {
            if (result != null)
                groupNameIDListMutableLiveData.postValue(new GroupNameIDListResult(result,true,
                        "Group name and id list retrieved successfully"));
            else
                groupNameIDListMutableLiveData.postValue(new GroupNameIDListResult(null,false,
                        "Error retrieving group name and id list"));
        });
    }

    public void insertGroup(Group group) {
        taskRunner.executeAsync(new InsertGroupCallable(group), result -> {
            if (result != null)
                groupInsertLiveData.postValue(new GroupResult(result,true,
                        "Group added successfully"));
            else
                groupInsertLiveData.postValue(new GroupResult(null,false,
                        "Error adding group"));
        });

    }

    public void getGroupByGroupId(String uid,long groupId) {
        taskRunner.executeAsync(new GetGroupByGroupIdCallable(uid,groupId), result -> {
            if (result != null)
                groupLiveData.postValue(new GroupResult(result,true,
                        "Group retrieved successfully"));
            else
                groupLiveData.postValue(new GroupResult(null,false,
                        "Error retrieving group"));
        });
    }

    public void updateGroup(Group group) {
        taskRunner.executeAsync(new UpdateGroupCallable(group), result -> {
            if (result != null)
                groupUpdateLiveData.postValue(new GroupResult(result,true,
                        "Group updated successfully"));
            else
                groupUpdateLiveData.postValue(new GroupResult(null,false,
                        "Error updating group"));
        });
    }

    public void deleteGroup(Group group) {
        taskRunner.executeAsync(new DeleteGroupCallable(group), result -> {
            if (result != null)
                groupDeleteLiveData.postValue(new GroupResult(result,true,
                        "Group deleted successfully"));
            else
                groupDeleteLiveData.postValue(new GroupResult(null,false,
                        "Error deleting group"));
        });
    }

    public void getAllGroupsByUid(String uid) {
        taskRunner.executeAsync(new GetAllGroupsCallable(uid), result -> {
            if (result != null)
                groupListLiveData.postValue(new GroupListResult(result,true,
                        "Groups retrieved successfully"));
            else
                groupListLiveData.postValue(new GroupListResult(null,false,
                        "Error retrieving groups"));
        });
    }

    public void getGroupsByClassID(String uid,long class_id) {
        taskRunner.executeAsync(new GetGroupsByClassIDCallable(uid,class_id), result -> {
            if (result != null)
                groupListLiveData.postValue(new GroupListResult(result,true,
                        "Groups retrieved successfully"));
            else
                groupListLiveData.postValue(new GroupListResult(null,false,
                        "Error retrieving groups"));
        });
    }

    public void getGroupByBetweenDates(String uid,boolean active, long date) {
        taskRunner.executeAsync(new GetGroupByBetweenDatesCallable(uid,active,date), result -> {
            if (result != null)
                groupListLiveData.postValue(new GroupListResult(result,true,
                        "Groups retrieved successfully"));
            else
                groupListLiveData.postValue(new GroupListResult(null,false,
                        "Error retrieving groups"));
        });
    }

    public void getGroupByActive(String uid,long class_id, boolean active){
        taskRunner.executeAsync(new GetGroupByActiveCallable(uid,class_id,active), result -> {
            if (result != null)
                groupListLiveData.postValue(new GroupListResult(result,true,
                        "Groups retrieved successfully"));
            else
                groupListLiveData.postValue(new GroupListResult(null,false,
                        "Error retrieving groups"));
        });
    }


    private class GetClassNameIdListCallable implements Callable<List<ClassNameId>> {
        private String uid;

        public GetClassNameIdListCallable(String uid) {
            this.uid = uid;
        }

        @Override
        public List<ClassNameId> call() throws Exception {
            return groupDao.getDistinctClassNames(uid);
        }
    }

    private class GetGroupNameIdListCallable implements Callable<List<GroupNameID>> {
        private String uid;
        private long class_id;

        public GetGroupNameIdListCallable(String uid, long class_id) {
            this.uid = uid;
            this.class_id = class_id;
        }

        @Override
        public List<GroupNameID> call() throws Exception {
            return groupDao.getDistinctGroupNames(uid,class_id);
        }
    }

    private class InsertGroupCallable implements Callable<Group> {
        private Group group;

        public InsertGroupCallable(Group group) {
            this.group = group;
        }

        @Override
        public Group call() throws Exception {
            groupDao.insertGroup(group);
            return group;
        }
    }

    private class GetGroupByGroupIdCallable implements Callable<Group> {
        private String uid;
        private long groupId;

        public GetGroupByGroupIdCallable(String uid,long groupId) {
            this.uid = uid;
            this.groupId = groupId;
        }

        @Override
        public Group call() throws Exception {
            return groupDao.getGroupsByGroupId(uid,groupId);
        }
    }

    private class GetGroupsByClassIDCallable implements Callable<List<Group>> {
        private String uid;
        private long class_id;

        public GetGroupsByClassIDCallable(String uid,long class_id) {
            this.uid = uid;
            this.class_id = class_id;
        }

        @Override
        public List<Group> call() throws Exception {
            return groupDao.getGroupsByClassId(uid,class_id);
        }
    }


    private class UpdateGroupCallable implements Callable<Group> {
        private Group group;

        public UpdateGroupCallable(Group group) {
            this.group = group;
        }

        @Override
        public Group call() throws Exception {
            groupDao.updateGroup(group);
            return group;
        }
    }

    private class DeleteGroupCallable implements Callable<Group> {
        private Group group;

        public DeleteGroupCallable(Group group) {
            this.group = group;
        }

        @Override
        public Group call() throws Exception {
            groupDao.deleteGroup(group);
            return group;
        }
    }

    private class GetAllGroupsCallable implements Callable<List<Group>> {
        private String uid;

        public GetAllGroupsCallable(String uid) {
            this.uid = uid;
        }

        @Override
        public List<Group> call() throws Exception {
            return groupDao.getAllGroups(uid);
        }
    }


    private class GetGroupByBetweenDatesCallable implements Callable<List<Group>>{
        private String uid;
        private boolean active;
        private long date;
        public GetGroupByBetweenDatesCallable(String uid,boolean active,long date){
            this.uid=uid;
            this.active=active;
            this.date=date;
        }

        @Override
        public List<Group> call() throws Exception {
            return groupDao.getGroupsBetweenDatesActive(uid, active, date);
        }
    }


    private class GetGroupByActiveCallable implements Callable<List<Group>>{
        private String uid;
        private long class_id;
        private boolean active;
        public GetGroupByActiveCallable(String uid, long class_id, boolean active){
            this.uid=uid;
            this.active=active;
            this.class_id=class_id;
        }

        @Override
        public List<Group> call() throws Exception {
            return groupDao.getGroupsByActive(uid, class_id, active);
        }
    }

    private class TaskRunner{
        private final Executor executor = Executors.newSingleThreadExecutor(); // change according to your requirements
        private final Handler handler = new Handler(Looper.getMainLooper());

        public interface Callback<R> {
            void onComplete(R result);
        }

        public <R> void executeAsync(Callable<R> callable, GroupRepo.TaskRunner.Callback<R> callback) {
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

