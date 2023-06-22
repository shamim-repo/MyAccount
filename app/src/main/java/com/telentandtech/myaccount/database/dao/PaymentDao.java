package com.telentandtech.myaccount.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.telentandtech.myaccount.database.resultObjects.AddStudentPaymentList;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.resultObjects.PaidUnpaidByMonth;
import com.telentandtech.myaccount.database.resultObjects.PaidUnpaidCountResult;
import com.telentandtech.myaccount.database.entityes.Payments;

import java.util.List;

@Dao
public interface PaymentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPayment(Payments payments);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertPaymentList(List<Payments> paymentsList);
    @Delete
    void deletePayment(Payments payments);
    @Query("DELETE FROM 'payments' WHERE uid=:uid AND payment_id=:fees_id")
    void deletePaymentById(String uid,long fees_id);
    @Update
    void updatePayment(Payments payments);
    //get all payments
    @Query("SELECT * FROM 'payments' WHERE uid=:uid order by id  DESC")
    List<Payments> getAllPayments(String uid);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND group_id = :group_id order by id  DESC")
    List<Payments> getPaymentsByGroupId(String uid,long group_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND class_id = :class_id order by id  DESC")
    List<Payments> getPaymentsByClassId(String uid,long class_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND student_id = :student_id" )
    List<Payments> getPaymentsByStudentId(String uid,long student_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND payment_month = :payment_month " +
            "AND group_id = :group_id order by CAST(id AS INTEGER )  DESC")
    List<Payments> getPaymentsByPaymentMonth(String uid,long group_id,long payment_month);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid " +
            "AND group_id = :group_id AND fees_id=:fee_id order by CAST(id AS INTEGER )  DESC")
    List<Payments> getPaymentsByPaymentMonthFeeId(String uid,long group_id,long fee_id);
    @Query("SELECT * FROM 'payments' WHERE uid=:uid AND payment_month = :payment_month " +
            "AND group_id = :group_id AND payment_status=:payment_status order by CAST(id AS INTEGER )  DESC")
    List<Payments> getPaymentsByPaymentMonthAndStatus(String uid,long group_id,
                                                      long payment_month,boolean payment_status);
    @Query("SELECT DISTINCT class_id,class_name FROM 'payments' WHERE uid=:uid  order by payment_timestamp  DESC")
    List<ClassNameId> getClassNameIDList(String uid);
    @Query("SELECT DISTINCT group_id,group_name FROM 'payments' WHERE uid=:uid AND class_id=:class_id order by group_id DESC")
    List<GroupNameID> getGroupNameIDList(String uid, long class_id);

    @Query("SELECT COUNT(*) AS totalCount, SUM(CASE WHEN payment_status = 0 THEN 1 ELSE 0 END) " +
            "AS paidCount, SUM(CASE WHEN payment_status = 1 THEN 1 ELSE 0 END) AS unpaidCount " +
            "FROM 'payments' WHERE uid=:uid AND group_id=:group_id AND payment_month>=:payment_month " +
            "AND payment_month<=(:payment_month+100)")
    PaidUnpaidCountResult getPaidUnpaidCount(String uid, long group_id, long payment_month);
    @Query("SELECT COUNT(*) AS totalCount, SUM(CASE WHEN payment_status = 0 THEN 1 ELSE 0 END) " +
            "AS paidCount, SUM(CASE WHEN payment_status = 1 THEN 1 ELSE 0 END) AS unpaidCount " +
            "FROM 'payments' WHERE uid=:uid AND payment_month>=:payment_month " +
            "AND payment_month<=(:payment_month+100)")
    PaidUnpaidCountResult getPaidUnpaidCountAll(String uid, long payment_month);

    //every distinct month total payment due

    @Query("SELECT payment_month AS month,SUM(CASE WHEN payment_status = 1 THEN 1 ELSE 0 END) AS paid, " +
            "SUM(CASE WHEN payment_status = 0 THEN 1 ELSE 0 END) AS due " +
            "FROM payments WHERE uid=:uid AND group_id=:group_id AND payment_month>=:payment_month " +
            "AND payment_month<=(:payment_month+100) GROUP BY payment_month")
    List<PaidUnpaidByMonth> getPaidDueCountByMonth(String uid, long group_id, long payment_month);

    @Query("SELECT payment_month AS month,SUM(CASE WHEN payment_status = 1 THEN 1 ELSE 0 END) AS paid, " +
            "SUM(CASE WHEN payment_status = 0 THEN 1 ELSE 0 END) AS due " +
            "FROM payments WHERE uid=:uid AND payment_month>=:payment_month " +
            "AND payment_month<=(:payment_month+100) GROUP BY payment_month")
    List<PaidUnpaidByMonth> getPaidDueCountByMonthAll(String uid,long payment_month);

    @Query("SELECT DISTINCT payment_month,payment_amount,fees_id FROM 'payments' WHERE uid=:uid AND group_id=:group_id " +
            "AND payment_month>=:payment_month order by payment_month ASC")
    List<AddStudentPaymentList> getAddStudentPaymentList(String uid, long group_id, long payment_month);
    @Query("DELETE FROM 'payments' WHERE uid=:uid AND payment_month>=:starting_date AND student_id=:student_id")
    void deletePaymentByPaymentMonthAndGroupIdAndStudentId(String uid,long starting_date,long student_id);

}
