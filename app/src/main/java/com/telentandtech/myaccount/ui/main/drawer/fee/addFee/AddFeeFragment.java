package com.telentandtech.myaccount.ui.main.drawer.fee.addFee;

import static com.telentandtech.myaccount.core.DataClass.UID;
import static com.telentandtech.myaccount.core.DataClass.USER_EMAIL;
import static com.telentandtech.myaccount.core.DataClass.USER_NAME;
import static com.telentandtech.myaccount.core.DataClass.classListToIdNameStringList;
import static com.telentandtech.myaccount.core.DataClass.groupListToIdNameStringList;

import androidx.core.util.Pair;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.entityes.Fees;
import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.entityes.User;

import java.sql.Timestamp;

public class AddFeeFragment extends Fragment {

    private TextInputEditText addFeeAmountEditText;
    private Spinner addFeeClassSpinner;
    private Spinner addFeeGroupSpinner;
    private MaterialButton addFeeButton;
    private MaterialButton pickMonthButton;
    private TextInputEditText addFeeMonthEditText;
    private RadioGroup addFeeTypeRadioGroup;
    private AddFeeViewModel mViewModel;
    private User authUser;
    private Classe selectedClass;
    private Group selectedGroup;
    private Long selectedMonth;
    private Long selectedMonth2;
    private boolean insertStatus=false;
    private boolean rangePicker=false;
    private int insertCount = 0;
    private int count = 0;
    private ProgressBar progressBar;

    public static AddFeeFragment newInstance() {
        return new AddFeeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_fee, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddFeeViewModel.class);
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

        addFeeAmountEditText = view.findViewById(R.id.add_fee_amount_edit_text);
        addFeeClassSpinner = view.findViewById(R.id.add_fee_class_spinner);
        addFeeGroupSpinner = view.findViewById(R.id.add_fee_group_spinner);
        addFeeButton = view.findViewById(R.id.add_fee_add_button);
        pickMonthButton = view.findViewById(R.id.edit_fee_pick_month_button);
        addFeeMonthEditText = view.findViewById(R.id.edit_fee_month_edit_text);
        addFeeTypeRadioGroup = view.findViewById(R.id.edit_fee_monthly_yearly_radio_group);
        progressBar = view.findViewById(R.id.add_fee_progress_bar);
        progressBar.setVisibility(View.GONE);


        mViewModel.getClassList(authUser.getUid());

        mViewModel.getClassListLiveData().observe(getViewLifecycleOwner(), classes -> {
            if (classes.isSuccessful()) {
                ArrayAdapter<String> classAdapter =
                        new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                classListToIdNameStringList(classes.getClassList()));
                classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addFeeClassSpinner.setAdapter(classAdapter);
                addFeeClassSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mViewModel.getGroupList(authUser.getUid(),
                                classes.getClassList().get(position).getClass_id());
                        selectedClass=classes.getClassList().get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                addFeeClassSpinner.setSelection(0);
            }else
                Toast.makeText(getContext(), classes.getMessage(), Toast.LENGTH_SHORT).show();
        });

        mViewModel.getGroupListLiveData().observe(getViewLifecycleOwner(), groups -> {
            if (groups.isSuccessful()) {
                ArrayAdapter<String> groupAdapter =
                        new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                groupListToIdNameStringList(groups.getGroupList()));
                groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                addFeeGroupSpinner.setAdapter(groupAdapter);
                addFeeGroupSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedGroup=groups.getGroupList().get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                addFeeGroupSpinner.setSelection(0);
            }
        });


        addFeeTypeRadioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId==R.id.edit_fee_monthly_add_fee) {
                rangePicker = false;
                addFeeMonthEditText.setText("");
            }
            else {
                rangePicker = true;
                addFeeMonthEditText.setText("");
            }
        });

        pickMonthButton.setOnClickListener(v -> {
            if (rangePicker){
                MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker()
                        .setTitleText("Select Range of Month")
                        .setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR)
                        .build();
                materialDatePicker.addOnPositiveButtonClickListener(selection -> {

                    Pair<Long, Long> selectedDates = (Pair<Long, Long>) selection;
                    addFeeMonthEditText.setText(
                            DateObj.timestampToMonthYearString(new Timestamp(selectedDates.first))+" - "+
                            DateObj.timestampToMonthYearString(new Timestamp(selectedDates.second)));

                    selectedMonth = DateObj.monthYearToLong(new Timestamp(selectedDates.first));
                    selectedMonth2 = DateObj.monthYearToLong(new Timestamp(selectedDates.second));

                });
                materialDatePicker.addOnNegativeButtonClickListener(v1 -> materialDatePicker.dismiss());
                materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");

            }else {
                MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
                builder.setTitleText("Select Fee Month");
                builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
                MaterialDatePicker materialDatePicker = builder.build();
                materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");

                materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                    selectedMonth = DateObj.monthYearToLong( new Timestamp((long) selection));
                    addFeeMonthEditText.setText(DateObj.longToMonthYear(selectedMonth));
                });
            }
        });


        addFeeButton.setOnClickListener(v -> {
            if (addFeeAmountEditText.getText().toString().trim().isEmpty()){
                addFeeAmountEditText.setError("Enter Amount");
                return;
            }
            Double amount=Double.parseDouble(addFeeAmountEditText.getText().toString().trim());
            if(checkInput(amount)) {
                if (rangePicker) {
                    if (selectedMonth2 == null) {
                        Toast.makeText(getContext(), "Select Month Range", Toast.LENGTH_SHORT).show();
                        pickMonthButton.requestFocus();
                    } else {
                        progressBar.setVisibility(View.VISIBLE);
                        insertCount = 0;
                        for(long mont: DateObj.dateRangeToMonthYearList(selectedMonth,selectedMonth2)){
                            Fees fees = new Fees(
                                    selectedClass.getClass_name(), selectedClass.getClass_id(),
                                    selectedGroup.getGroup_name(), selectedGroup.getGroup_id(),
                                    amount, mont, new Timestamp(System.currentTimeMillis()),
                                    authUser.getUid()
                            );
                            mViewModel.insertFees(fees);
                            insertCount++;
                        }
                    }
                } else {
                    addFeeButton.setEnabled(false);
                    Fees fees = new Fees(
                            selectedClass.getClass_name(), selectedClass.getClass_id(),
                            selectedGroup.getGroup_name(), selectedGroup.getGroup_id(),
                            amount, selectedMonth, new Timestamp(System.currentTimeMillis()),
                            authUser.getUid()
                    );
                    mViewModel.insertFees(fees);
                }
                insertStatus = true;
            }
        });
        mViewModel.getInsertFeesLiveData().observe(getViewLifecycleOwner(), feesResult -> {
            addFeeButton.setEnabled(true);
            if (!insertStatus)
                return;
            if (feesResult.isSuccessful()) {
                if (rangePicker) {
                    count++;
                    if (insertCount == 1) {
                        Toast.makeText(getContext(), count + "Month Fee Added", Toast.LENGTH_SHORT).show();
                        addFeeAmountEditText.setText("");
                        addFeeMonthEditText.setText("");
                        selectedMonth = null;
                        selectedMonth2 = null;
                        count = 0;
                        progressBar.setVisibility(View.GONE);
                    }
                    insertCount--;
                } else {
                    Toast.makeText(getContext(), feesResult.getMessage(), Toast.LENGTH_SHORT).show();
                    addFeeAmountEditText.setText("");
                    addFeeMonthEditText.setText("");
                    selectedMonth = null;
                    selectedMonth2 = null;
                    count = 0;
                    progressBar.setVisibility(View.GONE);
                }
            }
        });



    }

    private boolean checkInput(Double amount) {
        if(selectedClass == null){
            Toast.makeText(getContext(),"Select Class",Toast.LENGTH_SHORT).show();
            addFeeGroupSpinner.requestFocus();
            return false;
        }if (selectedGroup == null){
            Toast.makeText(getContext(),"Select Group",Toast.LENGTH_SHORT).show();
            addFeeGroupSpinner.requestFocus();
            return false;
        }if (amount <= 0 ) {
            addFeeAmountEditText.setError("Enter Amount");
            addFeeAmountEditText.requestFocus();
            return false;
        }if (selectedMonth ==null){
            Toast.makeText(getContext(),"Pick Date",Toast.LENGTH_SHORT).show();
            pickMonthButton.requestFocus();
            return false;
        }
       return true;
    }

}