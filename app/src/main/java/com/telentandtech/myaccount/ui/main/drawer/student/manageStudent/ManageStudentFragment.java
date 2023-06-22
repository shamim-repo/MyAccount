package com.telentandtech.myaccount.ui.main.drawer.student.manageStudent;

import static com.telentandtech.myaccount.core.DataClass.UID;
import static com.telentandtech.myaccount.core.DataClass.USER_EMAIL;
import static com.telentandtech.myaccount.core.DataClass.USER_NAME;
import static com.telentandtech.myaccount.core.DataClass.classListToIdNameStringList;
import static com.telentandtech.myaccount.core.DataClass.classNameIDListToStringArray;
import static com.telentandtech.myaccount.core.DataClass.groupListToIdNameStringList;
import static com.telentandtech.myaccount.core.DataClass.groupNameIDListTOStringArray;

import androidx.activity.result.ActivityResultLauncher;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import com.telentandtech.myaccount.DocumentUtils.CSVUtils;
import com.telentandtech.myaccount.DocumentUtils.CSVtoPDFConverter;
import com.telentandtech.myaccount.DocumentUtils.PdfDocumentAdapter;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.database.resultObjects.ClassNameId;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.ui.main.drawer.student.manageStudent.recycleView.ManageStudentAdapter;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class ManageStudentFragment extends Fragment implements OnClickListener {

    private ManageStudentViewModel mViewModel;
    private Spinner classSpinner;
    private Spinner groupSpinner;
    private RecyclerView recyclerView;
    private ManageStudentAdapter adapter;
    private ClassNameId selectedClassNameId;
    private GroupNameID selectedGroupNameID;
    private List<Students> studentsList;
    private ExtendedFloatingActionButton printFab;
    private int lastEditedPosition;
    private int lastDeletedPosition;
    private boolean snekberDelete=false;
    private boolean snekberInsert=false;
    private boolean snekberupdate=false;

    private long classId;
    private long groupId;

    private User authUser;
    private ActivityResultLauncher<String> requestPermissionLauncher;



    public static ManageStudentFragment newInstance() {
        return new ManageStudentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_manage_student, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ManageStudentViewModel.class);
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
        classSpinner = view.findViewById(R.id.manage_student_class_spinner);
        groupSpinner = view.findViewById(R.id.manage_student_group_spinner);
        recyclerView = view.findViewById(R.id.manage_student_recycler_view);
        printFab = view.findViewById(R.id.manage_student_print_fab);


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
                        recyclerView.setAdapter(null);
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
                        recyclerView.setAdapter(null);
                        selectedGroupNameID = groupNameIDList.getGroupNameIDList().get(position);
                        mViewModel.getStudentList(authUser.getUid(), selectedClassNameId.getClass_id(), selectedGroupNameID.getGroup_id());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                groupSpinner.setSelection(0);
            }
        });

        mViewModel.getStudentListLiveData().observe(getViewLifecycleOwner(), studentsList -> {
            if (studentsList.isSuccessful()) {
                this.studentsList = studentsList.getStudentsList();
                adapter = new ManageStudentAdapter(studentsList.getStudentsList(),this::onClick);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(adapter);
            }
        });

        mViewModel.getDeleteLiveData().observe(getViewLifecycleOwner(), delete -> {
            if (delete.isSuccessful() && snekberDelete) {
                studentsList.remove(lastDeletedPosition);
                adapter.notifyItemRemoved(lastDeletedPosition);
                adapter.notifyItemRangeChanged(lastDeletedPosition, studentsList.size());
                Snackbar.make(getView(), "Student Deleted", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            mViewModel.insert(delete.getStudents());
                            snekberInsert=true;
                        }).show();
                snekberDelete=false;
            }
        });

        mViewModel.getInsertLiveData().observe(getViewLifecycleOwner(), insert -> {
            if (insert.isSuccessful() && snekberInsert) {
                studentsList.add(lastDeletedPosition,insert.getStudents());
                adapter.notifyItemInserted(lastDeletedPosition);
                adapter.notifyItemRangeChanged(lastDeletedPosition,studentsList.size());
                snekberInsert = false;
            }
        });
        mViewModel.getUpdateLiveData().observe(getViewLifecycleOwner(), update -> {
            if (update.isSuccessful() && snekberupdate) {
                if (update.getStudents().getClass_id()!=classId || update.getStudents().getGroup_id()!=groupId){
                    studentsList.remove(lastEditedPosition);
                    adapter.notifyItemRemoved(lastEditedPosition);
                    adapter.notifyItemRangeChanged(lastEditedPosition,studentsList.size());
                } else {
                    studentsList.set(lastEditedPosition, update.getStudents());
                    adapter.notifyItemChanged(lastEditedPosition);
                    snekberupdate = false;
                }
            }
        });

        printFab.setOnClickListener(v -> {

            if (studentsList !=null && studentsList.size() > 0) {
                createPrintDocument(studentsList);
            } else {
                Toast.makeText(getContext(), "No Student Found", Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void createPrintDocument(List<Students> studentsList) {
        MaterialAlertDialogBuilder builder=
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Print Student List")
                        //.setMessage("Student list will be saved in this location\n"+dir.getPath())
                        .setMessage("Are you sure you want to print student list?")
                        .setPositiveButton("Print", (dialog, which) -> {
                            ArrayList<List<String>> rows = new ArrayList<>();
                            List<String> list1 = new ArrayList<String>();
                            list1.add("UID");
                            list1.add("Name");
                            list1.add("ID");
                            list1.add("Class");
                            list1.add("Group");
                            list1.add("Collage");
                            list1.add("Phone");
                            rows.add(list1);

                            for (Students students : studentsList) {
                                List<String> list = new ArrayList<String>();
                                list.add(String.valueOf(students.getStudent_id()));
                                list.add(students.getFull_name());
                                list.add(students.getId());
                                list.add(students.getClass_name());
                                list.add(students.getGroup_name());
                                list.add(students.getSchool_name());
                                list.add(students.getPhone());

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

                            File fileCsv = new File(getContext().getExternalFilesDir(null), "student.csv");
                            CSVUtils.writeListToCSV(list2, fileCsv.getPath());
                            CSVtoPDFConverter.convertCSVtoPDF(fileCsv.getPath(), "student.pdf", 7);
                            dir=new File(dir.getAbsolutePath(),"student.pdf");

                            PdfDocumentAdapter documentAdapter = new PdfDocumentAdapter(getContext(),dir.getPath());
                            PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
                            String jobName = getString(R.string.app_name) + " Print Student List";
                            printManager.print(jobName, documentAdapter, null);
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
    }


    @Override
    public void onClick(int position, String option) {
        switch (option) {
            case "edit":
                snekberupdate=true;
                lastEditedPosition = position;
                editStudents(studentsList.get(position));
                break;
            case "delete":
                lastDeletedPosition = position;
                snekberDelete=true;
                mViewModel.delete(studentsList.get(position));
                break;
            case "detail":
                studentDetailDialog(studentsList.get(position));
                break;
        }
    }
    private boolean idCheck = false;

    private void editStudents(Students students) {
        classId=students.getClass_id();
        groupId=students.getGroup_id();

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_edit_student, null);
        builder.setView(view);
        builder.setTitle("Edit Student");

        TextInputEditText fullNameEditText = view.findViewById(R.id.student_full_name_edit_text);
        TextView uidTextView = view.findViewById(R.id.edit_student_uid_text_view);
        TextInputEditText idEditText = view.findViewById(R.id.student_id_edit_text);
        TextInputEditText schoolEditText = view.findViewById(R.id.student_school_name_edit_text);
        TextInputEditText guardianEditText = view.findViewById(R.id.student_guardian_name_edit_text);
        TextInputEditText phoneEditText = view.findViewById(R.id.student_contact_number_edit_text);
        TextInputEditText addressEditText = view.findViewById(R.id.student_address_edit_text);
        Spinner classSpinnerEdit = view.findViewById(R.id.edit_student_class_spinner);
        Spinner groupSpinnerEdit = view.findViewById(R.id.edit_student_group_spinner);
        MaterialButton pickMonthButton = view.findViewById(R.id.edit_student_pick_date_button);
        TextInputEditText startingMonthEditText = view.findViewById(R.id.student_start_date_edit_text);

        fullNameEditText.setText(students.getFull_name());
        uidTextView.setText("UID :"+students.getStudent_id());
        idEditText.setText(students.getId());
        schoolEditText.setText(students.getSchool_name());
        guardianEditText.setText(students.getGuardian_name());
        phoneEditText.setText(students.getPhone());
        addressEditText.setText(students.getAddress());
        startingMonthEditText.setText(DateObj.longToMonthYear(students.getStarting_date()));


        mViewModel.getClassList(authUser.getUid());

        mViewModel.getClassListLiveData().observe(getViewLifecycleOwner(), classes -> {
            if (classes.isSuccessful()) {
                ArrayAdapter<String> classAdapter =
                        new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                                classListToIdNameStringList(classes.getClassList()));
                classAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                classSpinnerEdit.setAdapter(classAdapter);

                int position = 0;
                for (int i = 0; i < classes.getClassList().size(); i++) {
                    if (classes.getClassList().get(i).getClass_id()==(students.getClass_id())) {
                        position = i;
                        break;
                    }
                }
                classSpinnerEdit.setSelection(position);

                classSpinnerEdit.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mViewModel.getGroupList(authUser.getUid(),
                                classes.getClassList().get(position).getClass_id());
                        students.setClass_id(classes.getClassList().get(position).getClass_id());
                        students.setClass_name(classes.getClassList().get(position).getClass_name());
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }else
                Toast.makeText(getContext(), classes.getMessage(), Toast.LENGTH_SHORT).show();
        });

        mViewModel.getGroupListLiveData().observe(getViewLifecycleOwner(), groups -> {
            if (groups.isSuccessful()) {
                ArrayAdapter<String> groupAdapter =
                        new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item,
                                groupListToIdNameStringList(groups.getGroupList()));
                groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                groupSpinnerEdit.setAdapter(groupAdapter);

                int position = 0;
                for (int i = 0; i < groups.getGroupList().size(); i++) {
                    if (groups.getGroupList().get(i).getGroup_id()==(students.getGroup_id())) {
                        position = i;
                        break;
                    }
                }

                groupSpinnerEdit.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        students.setGroup_id(groups.getGroupList().get(position).getGroup_id());
                        students.setGroup_name(groups.getGroupList().get(position).getGroup_name());
                        mViewModel.getStudentCount(authUser.getUid(),students.getClass_id(),students.getGroup_id());
                        idCheck= true;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        });

        mViewModel.getStudentCountLiveData().observe(getViewLifecycleOwner(), integer -> {
            if (integer != null && idCheck) {
                idEditText.setEnabled(false);
                String count = "";
                if (students.getClass_id() < 10)
                    count += "0" + students.getClass_id();
                else
                    count += students.getClass_id();
                if (students.getGroup_id() < 10)
                    count += "0" + students.getGroup_id();
                else
                    count += students.getGroup_id();
                if (integer < 10)
                    count += "0" + integer + 1;
                else
                    count += integer + 1;

                idEditText.setText(count);
                idCheck = false;
            }
        });


        pickMonthButton.setOnClickListener(v -> {
            MaterialDatePicker.Builder builder1 = MaterialDatePicker.Builder.datePicker();
            builder1.setTitleText("Select Starting Month");
            builder1.setInputMode(MaterialDatePicker.INPUT_MODE_CALENDAR);
            MaterialDatePicker materialDatePicker = builder1.build();
            materialDatePicker.show(getChildFragmentManager(), "DATE_PICKER");

            materialDatePicker.addOnPositiveButtonClickListener(selection -> {
                students.setStarting_date(DateObj.monthYearToLong( new Timestamp((long) selection)));
                startingMonthEditText.setText(DateObj.longToMonthYear(students.getStarting_date()));
            });
        });


        builder.setPositiveButton("Update", (dialog, which) -> {

            String fullName=fullNameEditText.getText().toString().trim();
            String school=schoolEditText.getText().toString().trim();
            String guardian=guardianEditText.getText().toString().trim();
            String phone=phoneEditText.getText().toString().trim();
            String address=addressEditText.getText().toString().trim();
            String id=idEditText.getText().toString().trim();

            if (fullName.isEmpty()) {
                fullNameEditText.setError("Enter Full Name");
                fullNameEditText.requestFocus();
                return;
            }if (school.isEmpty()) {
                schoolEditText.setError("Enter School Name");
                schoolEditText.requestFocus();
                return;
            }if (guardian.isEmpty()) {
                guardianEditText.setError("Enter Guardian Name");
                guardianEditText.requestFocus();
                return;
            }if (phone.isEmpty()) {
                phoneEditText.setError("Enter Phone Number");
                phoneEditText.requestFocus();
                return;
            }if (address.isEmpty()) {
                addressEditText.setError("Enter Address");
                addressEditText.requestFocus();
                return;
            }
            mViewModel.update(students);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    private void studentDetailDialog(Students students){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.student_detail_layout, null);
        builder.setView(view);
        builder.setTitle("Student Detail");

        TextView uidTextView = view.findViewById(R.id.student_detail_uid_text_view);
        TextView fullNameTextView = view.findViewById(R.id.student_detail_full_name_text_view);
        TextView schoolTextView = view.findViewById(R.id.student_detail_school_text_view);
        TextView guardianTextView = view.findViewById(R.id.student_detail_guardian_text_view);
        TextView phoneTextView = view.findViewById(R.id.student_detail_mobile_text_view);
        TextView addressTextView = view.findViewById(R.id.student_detail_address_text_view);
        TextView idTextView = view.findViewById(R.id.student_detail_id_text_view);
        TextView startingMonthTextView = view.findViewById(R.id.student_detail_starting_month_text_view);
        TextView classTextView = view.findViewById(R.id.student_detail_class_text_view);
        TextView groupTextView = view.findViewById(R.id.student_detail_group_text_view);

        uidTextView.setText("UID: "+students.getStudent_id());
        fullNameTextView.setText(students.getFull_name());
        schoolTextView.setText(students.getSchool_name());
        guardianTextView.setText(students.getGuardian_name());
        phoneTextView.setText(students.getPhone());
        addressTextView.setText(students.getAddress());
        idTextView.setText(students.getId());
        startingMonthTextView.setText(DateObj.longToMonthYear(students.getStarting_date()));
        classTextView.setText(students.getClass_name());
        groupTextView.setText(students.getGroup_name());

        builder.setPositiveButton("Ok", (dialog, which) -> dialog.dismiss());
        builder.show();
    }



}