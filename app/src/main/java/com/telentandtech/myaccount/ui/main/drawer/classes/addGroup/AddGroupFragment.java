package com.telentandtech.myaccount.ui.main.drawer.classes.addGroup;

import static com.telentandtech.myaccount.core.DataClass.UID;
import static com.telentandtech.myaccount.core.DataClass.USER_EMAIL;
import static com.telentandtech.myaccount.core.DataClass.USER_NAME;
import static com.telentandtech.myaccount.core.DataClass.classListToIdNameStringList;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.icu.util.Calendar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.core.TimeFormatHelper;
import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.entityes.User;

import java.sql.Timestamp;

public class AddGroupFragment extends Fragment {

    private AddGroupViewModel mViewModel;
    private Spinner selectClassSpinner;
    private TextInputEditText groupNameEditText;
    private TextView timeFromTextView;
    private RadioGroup timeFromRadioGroup;
    private RadioButton timeFromAMRadioButton;
    private RadioButton timeFromPMRadioButton;
    private TextView timeToTextView;
    private RadioGroup timeToRadioGroup;
    private RadioButton timeToAMRadioButton;
    private RadioButton timeToPMRadioButton;
    private MaterialButton timeFromPickerButton;
    private MaterialButton timeToPickerButton;
    private MaterialButton addGroupButton;
    private Classe selectedClass;
    private Timestamp timeFrom;
    private Timestamp timeTo;
    private User authUser;
    private Timestamp dateFrom;
    private TextInputEditText dateEditText;
    private MaterialButton dateFromPickerButton;



    public static AddGroupFragment newInstance() {
        return new AddGroupFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_group, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddGroupViewModel.class);
        getSharePref();
    }

    private void getSharePref() {
        SharedPreferences sharedPref= PreferenceManager.getDefaultSharedPreferences(getContext());
        authUser = new User(
                sharedPref.getString(UID, ""),
                sharedPref.getString(USER_NAME, ""),
                sharedPref.getString(USER_EMAIL, "")
        );
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectClassSpinner = view.findViewById(R.id.edit_group_class_spinner);
        groupNameEditText = view.findViewById(R.id.edit_group_name_edit_text);
        timeFromTextView = view.findViewById(R.id.time_from_text_view);
        timeFromRadioGroup = view.findViewById(R.id.time_from_radio_group);
        timeFromAMRadioButton = view.findViewById(R.id.time_from_am);
        timeFromPMRadioButton = view.findViewById(R.id.time_from_pm);
        timeToTextView = view.findViewById(R.id.time_to_text_view);
        timeToRadioGroup = view.findViewById(R.id.time_to_to_group);
        timeToAMRadioButton = view.findViewById(R.id.time_to_am);
        timeToPMRadioButton = view.findViewById(R.id.time_to_pm);
        timeFromPickerButton = view.findViewById(R.id.time_from_button);
        timeToPickerButton = view.findViewById(R.id.time_to_button);
        addGroupButton = view.findViewById(R.id.add_group_add_button);
        dateFromPickerButton = view.findViewById(R.id.edit_group_starting_date_button);
        dateEditText = view.findViewById(R.id.edit_group_starting_date_edit_text);

        timeToRadioGroup.clearCheck();
        timeFromRadioGroup.clearCheck();
        selectClassSpinner.setEnabled(false);

        mViewModel.getClassList(authUser.getUid());
        mViewModel.getClassListLivedata().observe(getViewLifecycleOwner(), classes -> {
            if (classes.isSuccessful()) {
                if (classes.getClassList().size() > 0) {
                    selectClassSpinner.setEnabled(true);
                    selectClassSpinner.setAdapter(new ArrayAdapter<>(getContext(),
                            android.R.layout.simple_spinner_dropdown_item,
                            classListToIdNameStringList(classes.getClassList())));
                    selectClassSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            selectedClass = classes.getClassList().get(position);
                        }
                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                } else {
                    Toast.makeText(getContext(), "No class is available", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), classes.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        timeFromPickerButton.setOnClickListener(v -> {
            MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Starting Timestamp")
                    .build();
            materialTimePicker.show(getChildFragmentManager(), "timePicker");
            materialTimePicker.addOnPositiveButtonClickListener(v1 -> {
                timeFrom = TimeFormatHelper.hourMinuteToTimestamp(materialTimePicker.getHour(),
                        materialTimePicker.getMinute());
                int hour = materialTimePicker.getHour() > 12 ? materialTimePicker.getHour() - 12 : materialTimePicker.getHour();
                int minute = materialTimePicker.getMinute();
                String timestamp = hour + ":" + minute;
                timeFromTextView.setText(timestamp);
                if (materialTimePicker.getHour() >= 12)
                    timeFromRadioGroup.check(R.id.time_from_pm);
                else
                    timeFromRadioGroup.check(R.id.time_from_am);
            });
        });

        timeToPickerButton.setOnClickListener(v -> {
            MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Select Ending Timestamp")
                    .build();
            materialTimePicker.show(getChildFragmentManager(), "timePicker");
            materialTimePicker.addOnPositiveButtonClickListener(v1 -> {
                timeTo = TimeFormatHelper.hourMinuteToTimestamp(
                        materialTimePicker.getHour(),materialTimePicker.getMinute());
                int hour = materialTimePicker.getHour() > 12 ? materialTimePicker.getHour() - 12 : materialTimePicker.getHour();
                int minute = materialTimePicker.getMinute();
                String Timestamp = hour + ":" + minute;
                timeToTextView.setText(Timestamp);
                if (materialTimePicker.getHour() >= 12)
                    timeToRadioGroup.check(R.id.time_to_pm);
                else
                    timeToRadioGroup.check(R.id.time_to_am);
            });
        });

        dateFromPickerButton.setOnClickListener(v -> {
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Starting Date");
            builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
            MaterialDatePicker materialDatePicker = builder.build();
            materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");

            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                dateFrom = new Timestamp((long) selection);
                dateEditText.setText(DateObj.timestampToMonthYearString(dateFrom));});
        });

        addGroupButton.setOnClickListener(v -> {
            if (checkData()) {
                mViewModel.insertGroup(new Group(
                        groupNameEditText.getText().toString(),
                        selectedClass.getClass_name(),
                        selectedClass.getClass_id(),
                        true,
                        DateObj.monthYearToLong(dateFrom),
                        DateObj.monthYearToLong(hundredYearsToTimestamp()),
                        timeFrom,
                        timeTo,
                        new Timestamp(System.currentTimeMillis()),
                        authUser.getUid()
                ));
            }
        });

        mViewModel.getGroupLivedata().observe(getViewLifecycleOwner(), group -> {
            if (group.isSuccessful()) {
                Toast.makeText(getContext(), group.getMessage(), Toast.LENGTH_SHORT).show();
                groupNameEditText.setText("");
                timeFromTextView.setText("");
                timeToTextView.setText("");
                timeToRadioGroup.clearCheck();
                timeFromRadioGroup.clearCheck();
                dateEditText.setText("");
                dateFrom = null;
                timeFrom = null;
                timeTo = null;


            } else {
                Toast.makeText(getContext(), group.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkData() {
        if (selectedClass == null){
            Toast.makeText(getContext(), "Please Select Class", Toast.LENGTH_SHORT).show();
            selectClassSpinner.requestFocus();
            return false;
        }
        if (groupNameEditText.getText().toString().isEmpty()){
            groupNameEditText.setError("Please Enter Group Name");
            groupNameEditText.requestFocus();
            return false;
        }
        if (timeFrom == null){
            Toast.makeText(getContext(), "Please Select Starting Time", Toast.LENGTH_SHORT).show();
            timeFromPickerButton.requestFocus();
            return false;
        }
        if (timeTo==null){
            Toast.makeText(getContext(), "Please Select Ending Time", Toast.LENGTH_SHORT).show();
            timeToPickerButton.requestFocus();
            return false;
        }if (dateFrom == null){
            Toast.makeText(getContext(), "Please Select Starting Date", Toast.LENGTH_SHORT).show();
            dateFromPickerButton.requestFocus();
            return false;
        }
        return true;
    }

    private Timestamp hundredYearsToTimestamp() {
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.YEAR, 100);
        return new Timestamp(calendar.getTimeInMillis());
    }
}