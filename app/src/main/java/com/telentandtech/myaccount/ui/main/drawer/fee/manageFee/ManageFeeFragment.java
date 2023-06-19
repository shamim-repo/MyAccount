package com.telentandtech.myaccount.ui.main.drawer.fee.manageFee;

import static com.telentandtech.myaccount.core.DataClass.UID;
import static com.telentandtech.myaccount.core.DataClass.USER_EMAIL;
import static com.telentandtech.myaccount.core.DataClass.USER_NAME;
import static com.telentandtech.myaccount.core.DataClass.classNameIDListToStringArray;
import static com.telentandtech.myaccount.core.DataClass.groupNameIDListTOStringArray;

import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.database.entityes.Fees;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.ui.main.drawer.fee.manageFee.recyclerView.ManageFeeAdapter;

import java.sql.Timestamp;
import java.util.List;

public class ManageFeeFragment extends Fragment implements OnClickListener {

    private ManageFeeViewModel mViewModel;
    private User authUser;
    private Spinner classSpinner;
    private Spinner groupSpinner;
    private RecyclerView recyclerView;
    private ManageFeeAdapter adapter;
    private ClassNameId selectedClassNameId;
    private GroupNameID selectedGroupNameID;
    private List<Fees> feesList;
    private int lastEditedPosition;
    private int lastDeletedPosition;
    private boolean isEdit = false;
    private boolean isDelete = false;
    private boolean isAdd = false;
    private boolean isList = false;


    public static ManageFeeFragment newInstance() {
        return new ManageFeeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_fee, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ManageFeeViewModel.class);
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

        classSpinner = view.findViewById(R.id.manage_fee_class_spinner);
        groupSpinner = view.findViewById(R.id.manage_fee_group_spinner);
        recyclerView = view.findViewById(R.id.manage_fee_recycler_view);

        mViewModel.getClassNameIDList(authUser.getUid());
        mViewModel.getClassNameIDListLiveData().observe(getViewLifecycleOwner(), classNameIDList -> {
            if (classNameIDList.getSuccessful()) {
                ArrayAdapter<String> classAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, classNameIDListToStringArray(classNameIDList.getClassNameIdList()));
                classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                classSpinner.setAdapter(classAdapter);

                classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mViewModel.getGroupNameIDList(authUser.getUid(),
                                classNameIDList.getClassNameIdList().get(position).getClass_id());
                        selectedClassNameId = classNameIDList.getClassNameIdList().get(position);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                classSpinner.setSelection(0);
            }
        });

        mViewModel.getGroupNameIDListLiveData().observe(getViewLifecycleOwner(), groupNameIDList -> {
            if (groupNameIDList.getSuccessful()) {
                ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item,
                        groupNameIDListTOStringArray(groupNameIDList.getGroupNameIDList()));
                groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                groupSpinner.setAdapter(groupAdapter);
                groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        isList = true;
                        selectedGroupNameID = groupNameIDList.getGroupNameIDList().get(position);
                        mViewModel.getFeesList(authUser.getUid(), selectedClassNameId.getClass_id(), selectedGroupNameID.getGroup_id());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                groupSpinner.setSelection(0);
            }
        });

       mViewModel.getFeesListLiveData().observe(getViewLifecycleOwner(), feesList -> {
           if (feesList.isSuccessful() && isList) {
                this.feesList = feesList.getFeesList();
               adapter = new ManageFeeAdapter(feesList.getFeesList(),this::onClick);
               recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
               recyclerView.setAdapter(adapter);
                isList = false;
           }
       });

       mViewModel.getDeleteLiveData().observe(getViewLifecycleOwner(), delete -> {
           if (delete.isSuccessful() && isDelete ) {
               feesList.remove(lastDeletedPosition);
               adapter.notifyItemRemoved(lastDeletedPosition);
               adapter.notifyItemRangeChanged(lastDeletedPosition,feesList.size());
               Snackbar.make(getView(), "Fee Deleted", Snackbar.LENGTH_LONG)
                       .setAction("Undo", v -> {
                            isAdd = true;
                           mViewModel.insertFee(delete.getFees());
                       }).show();
                isDelete = false;
           }
       });

       mViewModel.getInsertLiveData().observe(getViewLifecycleOwner(), insert -> {
           if (insert.isSuccessful() && isAdd) {
               feesList.add(lastDeletedPosition,insert.getFees());
               adapter.notifyItemInserted(lastDeletedPosition);
               adapter.notifyItemRangeChanged(lastDeletedPosition,feesList.size());
                isAdd = false;
           }
       });
       mViewModel.getUpdateLiveData().observe(getViewLifecycleOwner(), update -> {
           if (update.isSuccessful() && isEdit) {
               feesList.set(lastEditedPosition,update.getFees());
               adapter.notifyItemChanged(lastEditedPosition);
               isEdit = false;
           }
       });
    }


    @Override
    public void onClick(int position, String option) {
        switch (option) {
            case "edit":
                lastEditedPosition = position;
                editFees(feesList.get(position));
                break;
            case "delete":
                isDelete = true;
                lastDeletedPosition = position;
                mViewModel.deleteFee(feesList.get(position));
                break;
        }
    }

    private void editFees(Fees fees){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_fee, null);
        builder.setView(view);
        builder.setTitle("Edit Fee");


        TextInputEditText feeAmountEditText = view.findViewById(R.id.add_fee_amount_edit_text);
        Spinner classSpinner = view.findViewById(R.id.add_fee_class_spinner);
        Spinner groupSpinner = view.findViewById(R.id.add_fee_group_spinner);
        TextInputEditText feeMonthEditText = view.findViewById(R.id.edit_fee_month_edit_text);
        MaterialButton feeMonthPickerButton = view.findViewById(R.id.edit_fee_pick_month_button);
        feeMonthPickerButton.setEnabled(false);
        RadioGroup feeTypeRadioGroup = view.findViewById(R.id.edit_fee_monthly_yearly_radio_group);
        feeTypeRadioGroup.setVisibility(View.GONE);

        feeAmountEditText.setText(String.valueOf(fees.getFee_amount()));
        feeMonthEditText.setText(DateObj.longToMonthYear(fees.getFee_month()));

        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{fees.getClass_name()});
        classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        classSpinner.setAdapter(classAdapter);
        classSpinner.setSelection(0);

        ArrayAdapter<String> groupAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, new String[]{fees.getGroup_name()});
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupAdapter);
        groupSpinner.setSelection(0);

        builder.setPositiveButton("Update", (dialog, which) -> {
            if (feeAmountEditText.getText().toString().isEmpty()) {
                feeAmountEditText.setError("Enter Fee Amount");
                return;
            }
            isEdit = true;
            fees.setFee_amount(Double.parseDouble(feeAmountEditText.getText().toString()));
            mViewModel.updateFee(fees);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}