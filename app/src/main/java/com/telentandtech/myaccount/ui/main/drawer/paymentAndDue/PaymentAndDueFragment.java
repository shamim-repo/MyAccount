package com.telentandtech.myaccount.ui.main.drawer.paymentAndDue;

import static com.telentandtech.myaccount.core.DataClass.UID;
import static com.telentandtech.myaccount.core.DataClass.USER_EMAIL;
import static com.telentandtech.myaccount.core.DataClass.USER_NAME;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.entityes.Payments;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.ui.main.drawer.attentence.recycleView.AttendanceAdapter;
import com.telentandtech.myaccount.ui.main.drawer.paymentAndDue.recycleView.PaymentDueAdapter;

import java.sql.Timestamp;

public class PaymentAndDueFragment extends Fragment implements OnClickListener {

    private PaymentAndDueViewModel mViewModel;
    private User authUser;
    private Spinner classSpinner;
    private Spinner groupSpinner;
    private Spinner paymentStatusSpinner;

    private TextInputEditText paymentDateEditText;
    private MaterialButton datePickButton;

    private RecyclerView recyclerView;
    private PaymentDueAdapter adapter;
    private Long selectedDate;
    private int lastPayPosition;
    private boolean updateStatus=false;


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
        getSharedPref();
    }

    private void getSharedPref() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        authUser=new User(
                sharedPreferences.getString(UID,""),
                sharedPreferences.getString(USER_EMAIL,""),
                sharedPreferences.getString(USER_NAME,"")
        );
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = getView().findViewById(R.id.payment_due_recycler_view);
        classSpinner = view.findViewById(R.id.payment_class_spinner);
        groupSpinner = view.findViewById(R.id.payment_group_spinner);
        datePickButton = view.findViewById(R.id.payment_pick_date_button);
        paymentDateEditText = view.findViewById(R.id.payment_due_month_edit_text);
        paymentDateEditText.setEnabled(false);
        paymentStatusSpinner = view.findViewById(R.id.payment_payment_status_spinner);

        paymentStatusSpinner.setAdapter(new ArrayAdapter<String>(
                getContext(),
                android.R.layout.simple_spinner_dropdown_item,
                new String[]{"All","Paid", "Due"}
        ));

        paymentStatusSpinner.setSelection(0);

        paymentStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                recyclerView.setAdapter(null);
                if (!paymentDateEditText.getText().toString().isEmpty()){
                    if(selectedDate != null){
                        getPaymentList();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mViewModel.getClassIdNameList(authUser.getUid());
        mViewModel.getClassIdNameListLiveData().observe(getViewLifecycleOwner(), classes -> {
            if(classes.getSuccessful() && classes.getClassNameIdList().size() > 0){
                groupSpinner.setEnabled(true);
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter.addAll(DataClass.classNameIDListToStringArray(classes.getClassNameIdList()));
                classSpinner.setAdapter(adapter);
                classSpinner.setSelection(0);
                classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        groupSpinner.setAdapter(null);
                        recyclerView.setAdapter(null);
                        mViewModel.getGroupIdNameList(authUser.getUid(),
                                classes.getClassNameIdList().get(position).getClass_id());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }else
                groupSpinner.setEnabled(false);
        });

        mViewModel.getGroupIdNameListLiveData().observe(getViewLifecycleOwner(), groups -> {
            if(groups.getSuccessful() && groups.getGroupNameIDList().size() > 0){
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getContext(), android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                adapter.addAll(DataClass.groupNameIDListTOStringArray(groups.getGroupNameIDList()));
                groupSpinner.setAdapter(adapter);
                groupSpinner.setSelection(0);
                groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        recyclerView.setAdapter(null);
                        if (!paymentDateEditText.getText().toString().isEmpty()){
                            if(selectedDate != null){

                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        datePickButton.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Date");
            MaterialDatePicker<Long> picker = builder.build();
            picker.show(getParentFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(selection -> {
                recyclerView.setAdapter(null);
                selectedDate = DateObj.monthYearToLong(new Timestamp(selection));
                paymentDateEditText.setText(DateObj.longToMonthYear(selectedDate));
                getPaymentList();
            });
        });

        mViewModel.getPaymentListLiveData().observe(getViewLifecycleOwner(), paymentList -> {
            if(paymentList.isSuccessful() && paymentList.getPaymentsList().size() > 0){
                adapter = new PaymentDueAdapter(paymentList.getPaymentsList(), this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });

        mViewModel.getUpdatePaymentResultLiveData().observe(getViewLifecycleOwner(), updatePaymentResult -> {
            if (!updateStatus)
                return;
            if(updatePaymentResult.isSuccessful()){
                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                adapter.notifyItemChanged(lastPayPosition);
                updateStatus=false;
            }else
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        });

    }


    public void getPaymentList() {

        if (selectedDate == null)
            return;
        if (mViewModel.getGroupIdNameListLiveData().getValue() == null)
            return;
        if (mViewModel.getGroupIdNameListLiveData().getValue().getGroupNameIDList().size() == 0)
            return;

        long group_id = mViewModel.getGroupIdNameListLiveData().getValue().getGroupNameIDList().
                get(groupSpinner.getSelectedItemPosition()).getGroup_id();

        switch (paymentStatusSpinner.getSelectedItemPosition()){
            case 1:
                mViewModel.getPaymentListByPaymentStatusAndDate(authUser.getUid(),
                        group_id,
                        true,selectedDate);
                break;
            case 2:
                mViewModel.getPaymentListByPaymentStatusAndDate(authUser.getUid(),
                            group_id, false,selectedDate );
                break;
            default:mViewModel.getPaymentListByDate(authUser.getUid(),
                    group_id,
                    selectedDate);
                break;
        }

    }

    @Override
    public void onClick(int position, String option) {
        updateStatus=true;
        switch (option){
            case "Un Pay":
                lastPayPosition = position;
                upPay(position);
                break;
            case "pay":
                lastPayPosition = position;
                pay(position);
                break;
        }

    }

    private void upPay(int position) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Payment");
        builder.setMessage("Are you sure you want to un pay?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            Payments payment = mViewModel.getPaymentListLiveData().getValue().getPaymentsList().get(position);
            payment.setPayment_status(false);
            payment.setPayment_timestamp(null);
            mViewModel.updatePayment(payment);
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.show();
    }

    private void pay(int position) {

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Payment");
        builder.setMessage("Are you sure you want to pay?");

        builder.setPositiveButton("Yes", (dialog, which) -> {
            Payments payment = mViewModel.getPaymentListLiveData().getValue().getPaymentsList().get(position);
            payment.setPayment_status(true);
            payment.setPayment_timestamp(new Timestamp(System.currentTimeMillis()));
            mViewModel.updatePayment(payment);
        });

        builder.setNegativeButton("No", (dialog, which) -> dialog.dismiss());

        builder.show();


    }
}