package com.telentandtech.myaccount.ui.main.drawer.student.addStudent;

import static com.telentandtech.myaccount.core.DataClass.UID;
import static com.telentandtech.myaccount.core.DataClass.USER_EMAIL;
import static com.telentandtech.myaccount.core.DataClass.USER_NAME;
import static com.telentandtech.myaccount.core.DataClass.classListToIdNameStringList;
import static com.telentandtech.myaccount.core.DataClass.groupListToIdNameStringList;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
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
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputEditText;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.database.entityes.Classe;
import com.telentandtech.myaccount.database.entityes.Group;
import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.entityes.User;

import java.sql.Timestamp;

public class AddStudentFragment extends Fragment {

    private AddStudentViewModel mViewModel;

    private TextInputEditText fullNameEditText;
    private TextInputEditText idEditText;
    private TextInputEditText schoolEditText;
    private TextInputEditText guardianEditText;
    private TextInputEditText phoneEditText;
    private TextInputEditText addressEditText;
    private Spinner classSpinner;
    private Spinner groupSpinner;
    private MaterialButton pickMonthButton;
    private TextInputEditText startingMonthEditText;
    private MaterialButton addButton;
    private User authUser;
    private Classe selectedClass;
    private Group selectedGroup;
    private Long selectedMonth;
    private boolean insertSuccess=false;

    public static AddStudentFragment newInstance() {
        return new AddStudentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_student, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(AddStudentViewModel.class);
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
        fullNameEditText = view.findViewById(R.id.student_full_name_edit_text);
        idEditText = view.findViewById(R.id.student_id_edit_text);
        schoolEditText = view.findViewById(R.id.student_school_name_edit_text);
        guardianEditText = view.findViewById(R.id.student_guardian_name_edit_text);
        phoneEditText = view.findViewById(R.id.student_contact_number_edit_text);
        addressEditText = view.findViewById(R.id.student_address_edit_text);
        classSpinner = view.findViewById(R.id.edit_student_class_spinner);
        groupSpinner = view.findViewById(R.id.edit_student_group_spinner);
        pickMonthButton = view.findViewById(R.id.edit_student_pick_date_button);
        startingMonthEditText = view.findViewById(R.id.student_start_date_edit_text);
        addButton = view.findViewById(R.id.add_student_add_button);

        mViewModel.getClassList(authUser.getUid());

        mViewModel.getClassListLiveData().observe(getViewLifecycleOwner(), classes -> {
            if (classes.isSuccessful()) {

                ArrayAdapter<String> classAdapter =
                        new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                                classListToIdNameStringList(classes.getClassList()));
                classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                classSpinner.setAdapter(classAdapter);
                classSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
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
                classSpinner.setSelection(0);
            }else
                Toast.makeText(getContext(), classes.getMessage(), Toast.LENGTH_SHORT).show();
        });

        mViewModel.getGroupListLiveData().observe(getViewLifecycleOwner(), groups -> {
            if (groups.isSuccessful()) {
                ArrayAdapter<String> groupAdapter =
                        new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                                groupListToIdNameStringList(groups.getGroupList()));
                groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                groupSpinner.setAdapter(groupAdapter);
                groupSpinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        selectedGroup=groups.getGroupList().get(position);
                        mViewModel.getStudentCount(authUser.getUid(),selectedClass.getClass_id(),selectedGroup.getGroup_id());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                groupSpinner.setSelection(0);
            }
        });

        mViewModel.getStudentCountLiveData().observe(getViewLifecycleOwner(), integer -> {
            if (integer != null) {
                idEditText.setEnabled(false);
                if (selectedGroup!=null && selectedClass!=null) {
                    String count = "";
                    if (selectedClass.getClass_id() < 10)
                        count += "0" + selectedClass.getClass_id();
                    else
                        count += selectedClass.getClass_id();
                    if (selectedGroup.getGroup_id() < 10)
                        count += "0" + selectedGroup.getGroup_id();
                    else
                        count += selectedGroup.getGroup_id();
                    if (integer < 10)
                        count += "0" + integer+1;
                    else
                        count += integer+1;

                    idEditText.setText(count);
                }
            }else
                idEditText.setEnabled(true);
        });

        pickMonthButton.setOnClickListener(v -> {
            MaterialDatePicker.Builder builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Starting Month");
            builder.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
            MaterialDatePicker materialDatePicker = builder.build();
            materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");

            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                selectedMonth = DateObj.monthYearToLong( new Timestamp((long) selection));
                startingMonthEditText.setText(DateObj.longToMonthYear(selectedMonth));
            });
        });

        addButton.setOnClickListener(v -> {
            String fullName=fullNameEditText.getText().toString().trim();
            String school=schoolEditText.getText().toString().trim();
            String guardian=guardianEditText.getText().toString().trim();
            String phone=phoneEditText.getText().toString().trim();
            String address=addressEditText.getText().toString().trim();
            String id=idEditText.getText().toString().trim();

            if(checkInput(fullName,school,guardian,phone,address,id)){
                addButton.setEnabled(false);
                Students students = new Students(
                        fullName,id,school,guardian,phone,address,selectedClass.getClass_name(),
                        selectedClass.getClass_id(),selectedGroup.getGroup_name(),
                        selectedGroup.getGroup_id(),selectedMonth, new Timestamp(System.currentTimeMillis()),
                        authUser.getUid());
                mViewModel.insert(students);
                insertSuccess=true;
            }
        });
        mViewModel.getInsertLiveData().observe(getViewLifecycleOwner(), studentResult -> {
            addButton.setEnabled(true);
            if (!insertSuccess)
                return;
            if (studentResult.isSuccessful()){
                Toast.makeText(getContext(),studentResult.getMessage(),Toast.LENGTH_SHORT).show();
                fullNameEditText.setText("");
                schoolEditText.setText("");
                guardianEditText.setText("");
                phoneEditText.setText("");
                addressEditText.setText("");
                startingMonthEditText.setText("");
                idEditText.setText("");
                selectedMonth=null;

                mViewModel.getStudentCount(authUser.getUid(),selectedClass.getClass_id(),
                        selectedGroup.getGroup_id());
                insertSuccess=false;

            }else {
                Toast.makeText(getContext(),studentResult.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkInput(String fullName, String school, String guardian,
                               String phone, String address, String id) {

        if(selectedClass == null){
            Toast.makeText(getContext(),"Select Class",Toast.LENGTH_SHORT).show();
            classSpinner.requestFocus();
            return false;
        }if (selectedGroup == null){
            Toast.makeText(getContext(),"Select Group",Toast.LENGTH_SHORT).show();
            groupSpinner.requestFocus();
            return false;
        }if (fullName.isEmpty()) {
            fullNameEditText.setError("Enter Full Name");
            fullNameEditText.requestFocus();
            return false;
        }if (school.isEmpty()) {
            schoolEditText.setError("Enter School Name");
            schoolEditText.requestFocus();
            return false;
        }if (guardian.isEmpty()) {
            guardianEditText.setError("Enter Guardian Name");
            guardianEditText.requestFocus();
            return false;
        }if (phone.isEmpty()) {
            phoneEditText.setError("Enter Phone Number");
            phoneEditText.requestFocus();
            return false;
        }if (address.isEmpty()) {
            addressEditText.setError("Enter Address");
            addressEditText.requestFocus();
            return false;
        }if (selectedMonth == null){
            Toast.makeText(getContext(),"Pick Date",Toast.LENGTH_SHORT).show();
            pickMonthButton.requestFocus();
            return false;
        }
        return true;
    }





}