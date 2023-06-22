package com.telentandtech.myaccount.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.entityes.Fees;


import java.util.List;

@Dao
public interface FeesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertFees(Fees fees);
    @Delete
    void deleteFees(Fees fees);
    @Update
    void updateFees(Fees fees);
    @Query("SELECT * FROM 'fees' WHERE uid=:uid order by created_at  DESC")
    List<Fees> getAllFees(String uid);
    @Query("SELECT * FROM 'fees' WHERE uid=:uid AND class_id= :class_id AND group_id = :group_id order by created_at  DESC")
    List<Fees> getFeesByGroupId(String uid,long class_id, long group_id);
    @Query("SELECT * FROM 'fees' WHERE uid=:uid AND fee_id = :fee_id order by created_at  DESC")
    List<Fees> getFeesByFeeId(String uid, long fee_id);
    @Query("SELECT * FROM 'fees' WHERE uid=:uid AND class_id= :class_id AND group_id = :group_id " +
            "AND fee_month = :fee_month order by created_at  DESC")
    List<Fees> getFeeByMonth(String uid, long class_id, long group_id, long fee_month);
    @Query("SELECT DISTINCT class_id,class_name  FROM 'fees' WHERE uid=:uid order by created_at  DESC")
    List<ClassNameId> getDistinctClassNameId(String uid);
    @Query("SELECT DISTINCT group_id,group_name  FROM 'fees' WHERE uid=:uid AND class_id= :class_id order by created_at  DESC")
    List<GroupNameID> getDistinctGroupNameId(String uid, long class_id);
}
