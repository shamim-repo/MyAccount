package com.telentandtech.myaccount.ui.main.drawer.paymentAndDue;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.telentandtech.myaccount.database.entityes.Payments;
import com.telentandtech.myaccount.database.resultObjects.ClassNameIdListResult;
import com.telentandtech.myaccount.database.resultObjects.GroupNameIDListResult;
import com.telentandtech.myaccount.database.resultObjects.PaymentsListResult;
import com.telentandtech.myaccount.database.resultObjects.PaymentsResult;
import com.telentandtech.myaccount.repository.PaymentRepo;

import java.util.List;

public class PaymentAndDueViewModel extends AndroidViewModel {
    private PaymentRepo paymentRepo;
    public PaymentAndDueViewModel(@NonNull Application application) {
        super(application);
        paymentRepo = new PaymentRepo(application);
    }

    public void insertPayment(Payments payments){
        paymentRepo.insertPayment(payments);
    }
    public LiveData<PaymentsResult> getInsertPaymentResultLiveData(){
        return paymentRepo.getInsertPaymentResultMutableLiveData();
    }

    public void updatePayment(Payments payments){
        paymentRepo.updatePayment(payments);
    }

    public LiveData<PaymentsResult> getUpdatePaymentResultLiveData(){
        return paymentRepo.getUpdatePaymentResultMutableLiveData();
    }
    public void deletePayment(Payments payments){
        paymentRepo.deletePayment(payments);
    }

    public LiveData<PaymentsResult> getDeletePaymentResultLiveData(){
        return paymentRepo.getDeletePaymentResultMutableLiveData();
    }
    public void getPaymentListByPaymentStatusAndDate(String uid, long group_id,boolean payment_status, long date){
        paymentRepo.getPaymentsByClassIdGroupIdStatus(uid, group_id, date, payment_status);
    }

    public void getPaymentListByDate(String uid, long group_id,long date){
        paymentRepo.getPaymentsByClassIdGroupId(uid, group_id, date);
    }

    public LiveData<PaymentsListResult> getPaymentListLiveData(){
        return paymentRepo.getPaymentsListResultMutableLiveData();
    }

    public void getClassIdNameList(String uid){
        paymentRepo.getClassIdName(uid);
    }
    public void getGroupIdNameList(String uid, long class_id){
        paymentRepo.getGroupIdName(uid, class_id);
    }

    public LiveData<ClassNameIdListResult> getClassIdNameListLiveData(){
        return paymentRepo.getClassNameIdMutableLiveData();
    }

    public LiveData<GroupNameIDListResult> getGroupIdNameListLiveData(){
        return paymentRepo.getGroupNameIDMutableLiveData();
    }
}