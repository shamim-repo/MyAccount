package com.telentandtech.myaccount.database.dao;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface GroupDao {
    @Insert
    void insertGroup(Group group);
    @Delete
    void deleteGroup(Group group);
    @Update
    void updateGroup(Group group);
    @Query("SELECT * FROM 'groups' WHERE uid=:uid order by created_at  DESC")
    List<Group> getAllGroups(String uid);
    @Query("SELECT * FROM 'groups' WHERE uid=:uid AND class_id = :class_id order by created_at  DESC")
    List<Group> getGroupsByClassId(String uid,long class_id);
    @Query("SELECT * FROM 'groups' WHERE uid=:uid AND group_id = :group_id order by created_at  DESC")
    Group getGroupsByGroupId(String uid,long group_id);
    @Query("SELECT * FROM 'groups' WHERE uid=:uid AND active=:active AND start_date <:start_date  order by created_at  DESC")
    List<Group> getGroupsBetweenDatesActive(String uid,boolean active, long start_date);
    @Query("SELECT * FROM 'groups' WHERE uid=:uid AND class_id = :class_id AND active=:active  order by created_at  DESC")
    List<Group> getGroupsByActive(String uid,long class_id, boolean active);
    @Query("SELECT DISTINCT class_id, class_name FROM 'groups' WHERE uid=:uid  order by created_at  DESC")
    List<ClassNameId> getDistinctClassNames(String uid);
    @Query("SELECT DISTINCT group_id, group_name FROM 'groups' WHERE uid=:uid AND class_id=:class_id order by created_at  DESC")
    List<GroupNameID> getDistinctGroupNames(String uid, long class_id);
}
