package com.telentandtech.myaccount.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.telentandtech.myaccount.database.entityes.Payments;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;

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
    List<Payments> getPaymentsByGroupId(String uid,long group_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND class_id = :class_id order by id  DESC")
    List<Payments> getPaymentsByClassId(String uid,long class_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND student_id = :student_id" )
    List<Payments> getPaymentsByStudentId(String uid,long student_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND payment_month = :payment_month " +
            "AND group_id = :group_id order by CAST(id AS INTEGER )  DESC")
    List<Payments> getPaymentsByPaymentMonth(String uid,long group_id,long payment_month);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND payment_month = :payment_month " +
            "AND group_id = :group_id AND fees_id=:fee_id order by CAST(id AS INTEGER )  DESC")
    List<Payments> getPaymentsByPaymentMonthFeeId(String uid,long group_id,long payment_month,long fee_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND payment_month = :payment_month " +
            "AND group_id = :group_id AND payment_status=:payment_status order by CAST(id AS INTEGER )  DESC")
    List<Payments> getPaymentsByPaymentMonthAndStatus(String uid,long group_id,
                                                      long payment_month,boolean payment_status);
    @Query("SELECT DISTINCT class_id,class_name FROM 'attendance' WHERE uid=:uid  order by class_id  DESC")
    List<ClassNameId> getClassNameIDList(String uid);
    @Query("SELECT DISTINCT group_id,group_name FROM 'attendance' WHERE uid=:uid AND class_id=:class_id order by group_id DESC")
    List<GroupNameID> getGroupNameIDList(String uid, long class_id);

}
