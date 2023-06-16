package com.telentandtech.myaccount.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;

import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.telentandtech.myaccount.database.entityes.Attendance;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;


import java.sql.Timestamp;
import java.util.List;

@Dao
public interface AttendanceDao {
    @Insert
    void insertAttendance(Attendance attendance);
    @Insert
    void insertAttendanceList(List<Attendance> attendanceList);
    @Delete
    void deleteAttendance(Attendance attendance);
    @Update
    void updateAttendance(Attendance attendance);
    @Query("SELECT *FROM 'attendance' WHERE uid=:uid")
    List<Attendance> getAllAttendance(String uid);
    @Query  ("SELECT * FROM 'attendance' WHERE group_id = :group_id AND uid=:uid order by CAST(id AS INTEGER)  DESC")
    Attendance getAttendanceByGroupId(String uid, long group_id);
    @Query("SELECT * FROM 'attendance' WHERE uid=:uid AND " +
            "group_id = :group_id  AND date= :lesion_date order by CAST(id AS INTEGER)  DESC")
    List<Attendance> getAttendanceByDate(String uid, long group_id, long lesion_date);
    @Query("SELECT class_id,class_name FROM 'attendance' WHERE uid=:uid  order by class_id  DESC")
    List<ClassNameId> getClassNameIDList(String uid);
    @Query("SELECT group_id,group_name FROM 'attendance' WHERE uid=:uid AND class_id=:class_id order by group_id DESC")
    List<GroupNameID> getGroupNameIDList(String uid, long class_id);

}
