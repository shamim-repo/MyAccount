package com.telentandtech.myaccount.ui.main.drawer.attentence;

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
import com.google.android.material.textfield.TextInputEditText;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.database.entityes.Attendance;
import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.ui.main.drawer.attentence.recycleView.AttendanceAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class MangeAttendanceFragment extends Fragment implements OnClickListener {

    private MangeAttendanceViewModel mViewModel;
    private RecyclerView recyclerView;
    private AttendanceAdapter attendanceAdapter;
    private Spinner classSpinner;
    private Spinner groupSpinner;
    private MaterialButton pickDateButton;
    private TextInputEditText dateEditText;
    private User authUser;
    private Long selectedDate;
    private int lastUpdate;

    public static MangeAttendanceFragment newInstance() {
        return new MangeAttendanceFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_mange_attendance, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MangeAttendanceViewModel.class);
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
        recyclerView = getView().findViewById(R.id.attendance_recycler_view);
        classSpinner = view.findViewById(R.id.attendance_class_spinner);
        groupSpinner = view.findViewById(R.id.attendance_group_spinner);
        pickDateButton = view.findViewById(R.id.attendance_pick_date_button);
        dateEditText = view.findViewById(R.id.attendance_date_edit_text);
        dateEditText.setEnabled(false);

        mViewModel.getClassList(authUser.getUid());
        mViewModel.getClassNameIdLiveData().observe(getViewLifecycleOwner(), classes -> {
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
                        mViewModel.getGroupList(authUser.getUid(),
                                classes.getClassNameIdList().get(position).getClass_id());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
            }else
                groupSpinner.setEnabled(false);
        });

        mViewModel.getGroupNameIdLiveData().observe(getViewLifecycleOwner(), groups -> {
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
                        if (!dateEditText.getText().toString().isEmpty()){
                            if(selectedDate != null){
                                mViewModel.getAttendanceList(authUser.getUid(),
                                        groups.getGroupNameIDList().get(position).getGroup_id(),
                                        selectedDate);
                            }
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        pickDateButton.setOnClickListener(v -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            builder.setTitleText("Select Date");
            MaterialDatePicker<Long> picker = builder.build();
            picker.show(getParentFragmentManager(), picker.toString());
            picker.addOnPositiveButtonClickListener(selection -> {
                recyclerView.setAdapter(null);
                selectedDate = DateObj.dateToLong(new Timestamp(selection));
                dateEditText.setText(DateObj.longToDateString(selectedDate));
                mViewModel.getAttendanceList(authUser.getUid(),
                            mViewModel.getGroupNameIdLiveData().getValue().getGroupNameIDList()
                                    .get(groupSpinner.getSelectedItemPosition()).getGroup_id(), selectedDate);
            });
        });

        mViewModel.getAttendanceListLiveData().observe(getViewLifecycleOwner(), attendanceList -> {
            if(attendanceList.isSuccessful()){
                Log.d("ManageAttendanceFragment", "attendanceList.isSuccessful(): ");
                if (attendanceList.getMessage().equals("No Attendance Found")) {
                    Log.d("ManageAttendanceFragment", "onViewCreated: No Data Found");
                    mViewModel.getStudentList(authUser.getUid(),
                            mViewModel.getClassNameIdLiveData().getValue().getClassNameIdList()
                                    .get(classSpinner.getSelectedItemPosition()).getClass_id(),
                            mViewModel.getGroupNameIdLiveData().getValue().getGroupNameIDList()
                                    .get(groupSpinner.getSelectedItemPosition()).getGroup_id());

                }else if(attendanceList.getAttendanceList() != null && attendanceList.getAttendanceList().size() > 0){
                    attendanceAdapter = new AttendanceAdapter(attendanceList.getAttendanceList(),this::onClick);
                    recyclerView.setAdapter(attendanceAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                }
                Toast.makeText(getContext(), attendanceList.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getStudentListLiveData().observe(getViewLifecycleOwner(), studentList -> {
            if(studentList.isSuccessful() && studentList.getStudentsList().size() > 0){
                Log.d("ManageAttendanceFragment", "onViewCreated: "+studentList.getStudentsList().size());
                mViewModel.insertAttendanceList(getAttendanceList(studentList.getStudentsList()));
            }
            Log.d("ManageAttendanceFragment", "onViewCreated: "+studentList.getMessage());
        });

        mViewModel.getUpdateLiveData().observe(getViewLifecycleOwner(), update -> {
            if(update.isSuccessful()){
                attendanceAdapter.notifyItemChanged(lastUpdate);
            }
        });
    }

    @Override
    public void onClick(int position, String option) {
        lastUpdate=position;
        Attendance attendance = mViewModel.getAttendanceListLiveData()
                .getValue().getAttendanceList().get(position);
        if(attendance.isAttended()){
            attendance.setAttended(false);
            mViewModel.update(attendance);
        }else {
            attendance.setAttended(true);
            mViewModel.update(attendance);
        }
    }

    //Students To attendanceList
    private List<Attendance> getAttendanceList(List<Students> studentsList){

        ArrayList<Attendance> attendanceList = new ArrayList<>();
        for (Students student : studentsList){
            attendanceList.add(new Attendance(
                    student.getClass_id(),
                    student.getClass_name(),
                    student.getGroup_id(),
                    student.getGroup_name(),
                    student.getFull_name(),
                    student.getStudent_id(),
                    student.getId(),
                    student.getPhone(),
                    false,
                    (long)selectedDate,
                    authUser.getUid()));
        }
        return attendanceList;
    }
}