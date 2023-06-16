package com.telentandtech.myaccount.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;

import java.util.List;

@Dao
public interface StudentsDao {
    @Insert
    void insertStudents(Students students);
    @Delete
    void deleteStudents(Students students);
    @Update
    void updateStudents(Students students);
    @Query("SELECT * FROM 'students' WHERE uid=:uid order by created_at  DESC")
    List<Students> getAllStudents(String uid);
    @Query("SELECT * FROM 'students' WHERE uid=:uid AND group_id = :group_id order by created_at  DESC")
    List<Students> getStudentsByGroupId(String uid,long group_id);
    @Query("SELECT * FROM 'students' WHERE uid=:uid AND class_id = :class_id AND group_id " +
            "=:group_id order by CAST(id AS INTEGER)  DESC")
    List<Students> getStudentsByClassIdGroupId(String uid,long class_id,long group_id);
    @Query("SELECT * FROM 'students' WHERE uid=:uid AND student_id = :student_id order by created_at  DESC")
    List<Students> getStudentsByStudentId(String uid,long student_id);
    @Query("SELECT DISTINCT class_id,class_name FROM 'students' WHERE uid=:uid order by created_at  DESC")
    List<ClassNameId> getDistinctClassNames(String uid);
    @Query("SELECT DISTINCT group_id,group_name FROM 'students' WHERE uid=:uid AND class_id=:class_id order by created_at  DESC")
    List<GroupNameID> getDistinctGroupNames(String uid, long class_id);
    @Query("SELECT COUNT(*) FROM 'students' WHERE uid=:uid AND class_id=:class_id AND group_id=:group_id")
    Integer getStudentCount(String uid, long class_id,long group_id);


}
