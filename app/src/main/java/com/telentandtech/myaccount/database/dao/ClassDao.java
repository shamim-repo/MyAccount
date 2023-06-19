package com.telentandtech.myaccount.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;

import java.util.List;

@Dao
public interface ClassDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertClass(Classe classe);
    @Update
    void updateClass(Classe classe);
    @Delete
    void deleteClass(Classe classe);
    @Query("SELECT * FROM 'classes' WHERE uid=:uid order by created_at  DESC")
    List<Classe> getAllClassesList(String uid);
    @Query("SELECT * FROM 'classes' WHERE uid=:uid AND class_id=:classId")
    Classe getClass(String uid, long classId);
    @Query("SELECT DISTINCT class_id,class_name  FROM 'classes' WHERE uid=:uid order by created_at  DESC")
    List<ClassNameId> getDistinctClassesList(String uid);
}
