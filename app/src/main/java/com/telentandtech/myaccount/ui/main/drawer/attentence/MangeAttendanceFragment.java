package com.telentandtech.myaccount.ui.main.drawer.attentence;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.print.PrintManager;
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
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import com.telentandtech.myaccount.DocumentUtils.CSVUtils;
import com.telentandtech.myaccount.DocumentUtils.CSVtoPDFConverter;
import com.telentandtech.myaccount.DocumentUtils.PdfDocumentAdapter;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.database.entityes.Attendance;
import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.ui.main.drawer.attentence.recycleView.AttendanceAdapter;

import java.io.File;
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
    private boolean getAttendance = false;
    private boolean getStudents = false;
    private boolean getUpdate = false;
    private ExtendedFloatingActionButton addAttendanceButton;
    private ExtendedFloatingActionButton printButton;

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
                sharedPreferences.getString(DataClass.UID,""),
                sharedPreferences.getString(DataClass.USER_EMAIL,""),
                sharedPreferences.getString(DataClass.USER_NAME,"")
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
        addAttendanceButton = view.findViewById(R.id.manage_attendance_add_fab);
        printButton = view.findViewById(R.id.manage_attendance_print_fab);

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
                                getAttendance = true;
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
                getAttendance = true;
                if (groupSpinner!=null && groupSpinner.getSelectedItemPosition() != -1 &&
                    selectedDate != null && mViewModel.getGroupNameIdLiveData().getValue() != null)
                mViewModel.getAttendanceList(authUser.getUid(),
                            mViewModel.getGroupNameIdLiveData().getValue().getGroupNameIDList()
                                    .get(groupSpinner.getSelectedItemPosition()).getGroup_id(), selectedDate);
            });
        });

        mViewModel.getAttendanceListLiveData().observe(getViewLifecycleOwner(), attendanceList -> {
            if(attendanceList.isSuccessful() && getAttendance){
                if (attendanceList.getMessage().equals("No Attendance Found")) {
                    printButton.setVisibility(View.GONE);
                    addAttendanceButton.setVisibility(View.VISIBLE);
                }else if(attendanceList.getAttendanceList() != null && attendanceList.getAttendanceList().size() > 0 ){
                    attendanceAdapter = new AttendanceAdapter(attendanceList.getAttendanceList(),this::onClick);
                    recyclerView.setAdapter(attendanceAdapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    getAttendance = false;
                    addAttendanceButton.setVisibility(View.GONE);
                    printButton.setVisibility(View.VISIBLE);
                }
                Toast.makeText(getContext(), attendanceList.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        addAttendanceButton.setOnClickListener(v -> {
            mViewModel.getStudentList(authUser.getUid(),
                    mViewModel.getClassNameIdLiveData().getValue().getClassNameIdList()
                            .get(classSpinner.getSelectedItemPosition()).getClass_id(),
                    mViewModel.getGroupNameIdLiveData().getValue().getGroupNameIDList()
                            .get(groupSpinner.getSelectedItemPosition()).getGroup_id());
            getStudents = true;
        });

        mViewModel.getStudentListLiveData().observe(getViewLifecycleOwner(), studentList -> {
            if(studentList.isSuccessful() && studentList.getStudentsList().size() > 0 &&  getStudents){
                mViewModel.insertAttendanceList(getAttendanceList(studentList.getStudentsList()));
            getStudents = false;
            }
        });

        mViewModel.getUpdateLiveData().observe(getViewLifecycleOwner(), update -> {
            if(update.isSuccessful() && getUpdate){
                attendanceAdapter.notifyItemChanged(lastUpdate);
                getUpdate = false;
            }
        });

        printButton.setOnClickListener(v -> {
            if (attendanceAdapter != null && attendanceAdapter.attendanceList.size() > 0) {
                createPrintDocument(attendanceAdapter.attendanceList);
            }
        });

    }

    @Override
    public void onClick(int position, String option) {
        lastUpdate=position;
        getUpdate = true;
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

        if (studentsList==null && studentsList.size() == 0)
            return null;
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
                    selectedDate,
                    authUser.getUid()));
        }
        return attendanceList;
    }

    private void createPrintDocument(List<Attendance> attendanceList) {
        if(attendanceList==null && attendanceList.size() == 0)
            return;
        MaterialAlertDialogBuilder builder=
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Print Attendance List")
                        .setMessage("Are you sure you want to print attendance list?")
                        .setPositiveButton("Print", (dialog, which) -> {
                            ArrayList<List<String>> rows = new ArrayList<>();
                            List<String> list1 = new ArrayList<String>();
                            list1.add("UID");
                            list1.add("ID");
                            list1.add("Name");
                            list1.add("Group ID");
                            list1.add("Group");
                            list1.add("Phone");
                            list1.add("Date");
                            list1.add("Attendance");
                            rows.add(list1);

                            for (Attendance attendance : attendanceList) {
                                List<String> list = new ArrayList<String>();
                                list.add(String.valueOf(attendance.getStudent_id()));
                                list.add(String.valueOf(attendance.getId()));
                                list.add(attendance.getStudent_name());
                                list.add(String.valueOf(attendance.getGroup_id()));
                                list.add(attendance.getGroup_name());
                                list.add(attendance.getPhone());
                                list.add(DateObj.longToDateString(attendance.getDate()));
                                if (attendance.isAttended())
                                    list.add("Present");
                                else
                                    list.add("Absent");

                                rows.add(list);
                            }
                            ArrayList<String[]> list2 = new ArrayList<>();

                            for (List<String> list : rows) {
                                String[] strings = new String[list.size()];
                                strings = list.toArray(strings);
                                list2.add(strings);
                            }
                            File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
                            File dir=new File(directory.getAbsolutePath()+"/MyAccount");

                            File fileCsv = new File(getContext().getExternalFilesDir(null), "attendance.csv");
                            CSVUtils.writeListToCSV(list2, fileCsv.getPath());
                            CSVtoPDFConverter.convertCSVtoPDF(fileCsv.getPath(), "attendance.pdf", 8);

                            dir=new File(dir.getAbsolutePath(),"attendance.pdf");

                            PdfDocumentAdapter documentAdapter = new PdfDocumentAdapter(getContext(),dir.getPath());
                            PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
                            String jobName = getString(R.string.app_name) + " Print Attendance List";
                            printManager.print(jobName, documentAdapter, null);
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}