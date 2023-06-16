package com.telentandtech.myaccount.ui.main.drawer.paymentAndDue;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.telentandtech.myaccount.R;

public class PaymentAndDueFragment extends Fragment {

    private PaymentAndDueViewModel mViewModel;

    public static PaymentAndDueFragment newInstance() {
        return new PaymentAndDueFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_and_due, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PaymentAndDueViewModel.class);
    }
}