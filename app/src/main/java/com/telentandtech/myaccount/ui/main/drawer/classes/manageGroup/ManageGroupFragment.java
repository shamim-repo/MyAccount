package com.telentandtech.myaccount.ui.main.drawer.classes.manageGroup;

import static android.content.ContentValues.TAG;
import static com.telentandtech.myaccount.core.DataClass.UID;
import static com.telentandtech.myaccount.core.DataClass.USER_EMAIL;
import static com.telentandtech.myaccount.core.DataClass.USER_NAME;
import static com.telentandtech.myaccount.core.DataClass.classListToIdNameStringList;
import static com.telentandtech.myaccount.core.DataClass.classNameIDListToStringArray;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
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
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.core.TimeFormatHelper;
import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.ui.main.drawer.classes.manageGroup.recycleViewHelper.ManageGroupAdapter;

import java.sql.Timestamp;
import java.util.List;

public class ManageGroupFragment extends Fragment implements OnClickListener {
    private RecyclerView recyclerView;
    private ManageGroupViewModel mViewModel;
    private ManageGroupAdapter adapter;
    private Spinner classSpinner;
    private Spinner activeSpinner;
    private User authUser;
    private List<Group> allGroupList;
    private int lastUpdatePosition;
    private int lastDeletePosition;

    public static ManageGroupFragment newInstance() {
        return new ManageGroupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_group, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ManageGroupViewModel.class);
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
        recyclerView = view.findViewById(R.id.manage_group_recycler_view);
        classSpinner = view.findViewById(R.id.manage_group_class_spinner);
        activeSpinner = view.findViewById(R.id.manage_group_active_spinner);
        classSpinner.setEnabled(false);

        mViewModel.getClassList(authUser.getUid());
        mViewModel.getClassListLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.getSuccessful() && result.getClassNameIdList().size()> 0) {
                classSpinner.setEnabled(true);
                ArrayAdapter<String> classAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_item, classNameIDListToStringArray(result.getClassNameIdList()));
                classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                classSpinner.setAdapter(classAdapter);
                classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        getGroupList(result.getClassNameIdList().get(position).getClass_id(),
                                activeSpinner.getSelectedItem().toString());
                        recyclerView.setAdapter(null);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

                classSpinner.setSelection(0);

                activeSpinner.setEnabled(true);
                ArrayAdapter<String> activeAdapter = new ArrayAdapter<>(getContext(),
                        android.R.layout.simple_spinner_dropdown_item, new String[]{"All", "Active", "Inactive"});
                activeSpinner.setAdapter(activeAdapter);
                activeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        getGroupList(result.getClassNameIdList().get(classSpinner.getSelectedItemPosition()).getClass_id(),
                                activeSpinner.getSelectedItem().toString());
                        recyclerView.setAdapter(null);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
                activeSpinner.setSelection(0);

            }else {
            Toast.makeText(getContext(), "No class found", Toast.LENGTH_SHORT).show();
        }
        });

        mViewModel.getGroupListLiveData().observe(getViewLifecycleOwner(), groupList -> {
            if (groupList.isSuccessful() && groupList.getGroupList().size()> 0) {
                allGroupList = groupList.getGroupList();
                adapter = new ManageGroupAdapter(groupList.getGroupList(), this);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
                }else {
                Toast.makeText(getContext(), "No group found", Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getGroupEditLiveData().observe(getViewLifecycleOwner(), group -> {
            if (group.isSuccessful() && group.getGroup()!=null) {

                allGroupList.set(lastUpdatePosition,group.getGroup());
                adapter.editItem(lastUpdatePosition,group.getGroup());
                Toast.makeText(getContext(), group.getMessage(), Toast.LENGTH_SHORT).show();

            }else {
                Toast.makeText(getContext(), group.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getGroupDeleteLiveData().observe(getViewLifecycleOwner(), group -> {
            if (group.isSuccessful() && group.getGroup()!=null ) {
                allGroupList.remove(lastDeletePosition);
                adapter.remove(lastDeletePosition);
                Snackbar.make(getView(), group.getMessage(), Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            mViewModel.insertGroup(group.getGroup());
                        }).show();
            }else {
                Toast.makeText(getContext(), group.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        mViewModel.getGroupInsertLiveData().observe(getViewLifecycleOwner(), group -> {
            if (group.isSuccessful()) {
                if (allGroupList.size()==lastDeletePosition)
                    allGroupList.add(group.getGroup());
                else
                    allGroupList.add(lastDeletePosition,group.getGroup());
                adapter.insertItem(lastDeletePosition,group.getGroup());
            }else {
                Toast.makeText(getContext(), group.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getGroupEditLiveData().observe(getViewLifecycleOwner(), group -> {
            if (group.isSuccessful() && group.getGroup()!=null) {
                allGroupList.set(lastUpdatePosition,group.getGroup());
                adapter.notifyItemChanged(lastUpdatePosition);
                Toast.makeText(getContext(), group.getMessage(), Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(getContext(), group.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void getGroupList(long class_id, String active) {

        switch (active) {
            case "All":
                mViewModel.getGroupList(authUser.getUid(),class_id);
                break;
            case "Active":
                mViewModel.getGroupByActive(authUser.getUid(),class_id,true);
                break;
            case "Inactive":
                mViewModel.getGroupByActive(authUser.getUid(),class_id,false);
                break;
        }
    }



    @Override
    public void onClick(int position, String option) {

        switch (option) {
            case "edit":
                lastUpdatePosition=position;
                editDialog(allGroupList.get(position));
                break;
            case "delete":
                lastDeletePosition=position;
                mViewModel.deleteGroup(allGroupList.get(position));
                break;
        }
    }

    private void editDialog(Group group) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Edit Group");

        View view = getLayoutInflater().inflate(R.layout.fragment_edit_group, null);
        builder.setView(view);
        ArrayAdapter<String> classAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item,
                new String[]{group.getClass_name()});
            classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner classSpinner=view.findViewById(R.id.edit_group_class_spinner);
        classSpinner.setAdapter(classAdapter);
        classSpinner.setEnabled(false);

        TextInputEditText nameEditText=view.findViewById(R.id.edit_group_name_edit_text);
        nameEditText.setText(group.getGroup_name());
        nameEditText.setEnabled(false);

        TextView fromTextView=view.findViewById(R.id.time_from_text_view);
        fromTextView.setText(TimeFormatHelper.timestampToString(group.getStart_time()));
        TextView toTextView=view.findViewById(R.id.time_to_text_view);
        toTextView.setText(TimeFormatHelper.timestampToString(group.getEnd_time()));

        RadioGroup radioGroupFrom=view.findViewById(R.id.time_from_radio_group);
        RadioGroup radioGroupTo=view.findViewById(R.id.time_to_to_group);
        RadioGroup radioGroupActive=view.findViewById(R.id.group_active_deactive_radio_group);
        if (TimeFormatHelper.getHour(group.getStart_time())<12)
            radioGroupFrom.check(R.id.time_from_am);
        else
            radioGroupFrom.check(R.id.time_from_pm);

        if (TimeFormatHelper.getHour(group.getEnd_time())<12)
            radioGroupTo.check(R.id.time_to_am);
        else
            radioGroupTo.check(R.id.time_to_pm);

        if (group.isActive())
            radioGroupActive.check(R.id.group_active);
        else
            radioGroupActive.check(R.id.group_deactive);
        radioGroupActive.setEnabled(true);

        MaterialButton datePickerButton=view.findViewById(R.id.edit_group_starting_date_button);
        TextInputEditText datePickerEditText=view.findViewById(R.id.edit_group_starting_date_edit_text);
        datePickerEditText.setText(DateObj.longToMonthYear(group.getStart_date()));
        datePickerButton.setOnClickListener(v -> {
            MaterialDatePicker.Builder builder1 = MaterialDatePicker.Builder.datePicker();
            builder1.setTitleText("Select Starting Date");
            MaterialDatePicker materialDatePicker = builder1.build();
            materialDatePicker.show(getParentFragmentManager(), "DATE_PICKER");
            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                group.setStart_date((long)selection);
                datePickerEditText.setText(DateObj.longToMonthYear(group.getStart_date()));
            });

        });

        MaterialButton fromTimePickerButton=view.findViewById(R.id.time_from_button);
        MaterialButton toTimePickerButton=view.findViewById(R.id.time_to_button);
        fromTimePickerButton.setOnClickListener(v -> {
            MaterialTimePicker.Builder builder12 = new MaterialTimePicker.Builder();
            builder12.setTitleText("Select Starting Time");
            MaterialTimePicker materialTimePicker = builder12.build();
            materialTimePicker.show(getParentFragmentManager(), "TIME_PICKER");
            materialTimePicker.addOnPositiveButtonClickListener(selection -> {
                group.setStart_time(TimeFormatHelper.hourMinuteToTimestamp(materialTimePicker.getHour(),materialTimePicker.getMinute()));
                fromTextView.setText(TimeFormatHelper.timestampToString(group.getStart_time()));
            });
        });

        toTimePickerButton.setOnClickListener(v -> {
            MaterialTimePicker.Builder builder13 = new MaterialTimePicker.Builder();
            builder13.setTitleText("Select Ending Time");
            MaterialTimePicker materialTimePicker = builder13.build();
            materialTimePicker.show(getParentFragmentManager(), "TIME_PICKER");
            materialTimePicker.addOnPositiveButtonClickListener(selection -> {
                group.setEnd_time(TimeFormatHelper.hourMinuteToTimestamp(materialTimePicker.getHour(),materialTimePicker.getMinute()));
                toTextView.setText(TimeFormatHelper.timestampToString(group.getEnd_time()));
            });
        });


        builder.setPositiveButton("Update", (dialog, which) -> {
            if (radioGroupActive.getCheckedRadioButtonId()==R.id.group_active)
                group.setActive(true);
            else
                group.setActive(false);
            mViewModel.updateGroup(group);
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

}