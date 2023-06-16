package com.telentandtech.myaccount.database.dao;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.telentandtech.myaccount.database.entityes.Lesion;

import java.util.List;

@Dao
public interface LesionDao {
    @Insert
    void insertLesion(Lesion lesion);
    @Delete
    void deleteLesion(Lesion lesion);
    @Query("SELECT * FROM lesion WHERE uid=:uid order by lesion_date  DESC")
    List<Lesion> getAllLesion(long uid);
    @Query("SELECT * FROM lesion WHERE uid=:uid AND  class_id = :class_id order by lesion_date  DESC")
    List<Lesion> getLesionByClassId(long uid,long class_id);
    @Query("SELECT * FROM lesion WHERE uid=:uid AND group_id = :group_id order by lesion_date  DESC")
    List<Lesion> getLesionByGroupId(long uid,long group_id);

}
