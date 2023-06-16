package com.telentandtech.myaccount.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.telentandtech.myaccount.database.entityes.Payments;

import java.sql.Timestamp;
import java.util.List;

@Dao
public interface PaymentDao {
    @Insert
    void insertPayment(Payments payments);
    @Delete
    void deletePayment(Payments payments);
    @Update
    void updatePayment(Payments payments);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND group_id = :group_id order by id  DESC")
    List<Payments> getPaymentsByGroupId(long uid,long group_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND class_id = :class_id order by id  DESC")
    List<Payments> getPaymentsByClassId(long uid,long class_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND student_id = :student_id" )
    List<Payments> getPaymentsByStudentId(long uid,long student_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND payment_month = :payment_month " +
            "AND class_id = :class_id AND group_id = :group_id")
    List<Payments> getPaymentsByPaymentMonth(long uid,long payment_month, long class_id,long group_id);

}
