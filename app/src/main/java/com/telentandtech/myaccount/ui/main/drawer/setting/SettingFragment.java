package com.telentandtech.myaccount.ui.main.drawer.setting;

import static com.telentandtech.myaccount.core.DataClass.UID;
import static com.telentandtech.myaccount.core.DataClass.USER_EMAIL;
import static com.telentandtech.myaccount.core.DataClass.USER_NAME;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.PreferenceManager;

import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.telentandtech.myaccount.DocumentUtils.CSVUtils;
import com.telentandtech.myaccount.DocumentUtils.CSVtoPDFConverter;
import com.telentandtech.myaccount.DocumentUtils.ListToXlsxConverter;
import com.telentandtech.myaccount.R;

import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.database.dataBase.AccountDatabase;
import com.telentandtech.myaccount.database.entityes.Attendance;
import com.telentandtech.myaccount.database.entityes.Payments;
import com.telentandtech.myaccount.database.entityes.Students;
import com.telentandtech.myaccount.database.entityes.User;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import ir.androidexception.roomdatabasebackupandrestore.Backup;
import ir.androidexception.roomdatabasebackupandrestore.Restore;


public class SettingFragment extends Fragment {


    private SettingViewModel mViewModel;
    private User authUser;
    private CardView studentCardView;
    private CardView attendanceCardView;
    private CardView paymentCardView;
    private CardView backupDatabaseView;
    private CardView restoreDatabaseView;
    private ProgressBar studentListProgress;
    private int indicator=0;
    private static final String TAG = "SettingFragment";


    public static SettingFragment newInstance() {
        return new SettingFragment();
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SettingViewModel.class);
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        studentCardView=view.findViewById(R.id.student_list_export);
        attendanceCardView=view.findViewById(R.id.attendance_list_export);
        paymentCardView=view.findViewById(R.id.payment_list_export);
        studentListProgress=view.findViewById(R.id.progressBar);
        backupDatabaseView =view.findViewById(R.id.backup_full_database_export);
        restoreDatabaseView =view.findViewById(R.id.restore_full_database_export);

        studentCardView.setClickable(true);
        attendanceCardView.setClickable(true);
        paymentCardView.setClickable(true);
        backupDatabaseView.setClickable(true);


        studentCardView.setOnClickListener(v -> exportStudentList());
        attendanceCardView.setOnClickListener(v -> exportAttendanceList());
        paymentCardView.setOnClickListener(v -> exportPaymentList());

        mViewModel.getAllStudentList().observe(getViewLifecycleOwner(), studentListResult -> {
            studentListProgress.setVisibility(View.GONE);
            if (studentListResult!=null && studentListResult.getStudentsList() !=null){
                if (studentListResult.getStudentsList().size()>0){
                    if (indicator==1){
                        exportPdfStudentList(studentListResult.getStudentsList());
                       }else if (indicator==-1){
                        exportExcelStudentList(studentListResult.getStudentsList());
                    }
                }
            }
        });

        mViewModel.getAllAttendanceList().observe(getViewLifecycleOwner(), attendanceListResult -> {
            studentListProgress.setVisibility(View.GONE);
            if (attendanceListResult!=null && attendanceListResult.getAttendanceList() !=null){
                if (attendanceListResult.getAttendanceList().size()>0){
                    if (indicator==1){
                        exportPdfAttendanceList(attendanceListResult.getAttendanceList());
                    }else if (indicator==-1){
                        exportExcelAttendanceList(attendanceListResult.getAttendanceList());
                    }
                }
            }
        });

        mViewModel.getAllPaymentList().observe(getViewLifecycleOwner(), paymentListResult -> {
            studentListProgress.setVisibility(View.GONE);
            if (paymentListResult!=null && paymentListResult.getPaymentsList() !=null){
                if (paymentListResult.getPaymentsList().size()>0){
                    if (indicator==1){
                        exportPdfPaymentList(paymentListResult.getPaymentsList());
                    }else if (indicator==-1){
                        exportExcelPaymentList(paymentListResult.getPaymentsList());
                    }
                }
            }
        });

        backupDatabaseView.setOnClickListener(v ->{
            studentListProgress.setVisibility(View.VISIBLE);
            backupDatabase();});
        restoreDatabaseView.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("text/plain");
            folderPickerLauncher.launch(intent);
        });
    }


    private void backupDatabase(){
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)
                + "/MyAccount/DatabaseBackup");
        if (!folder.exists())
            folder.mkdirs();
        String fileName="database_backup_"+DateObj.timestampToDateString(new Timestamp(System.currentTimeMillis()))+"+.txt";
        new Backup.Init()
                .database(AccountDatabase.getInstance(getContext()))
                .path(folder.getAbsolutePath())
                .fileName(fileName)
                //.secretKey("your-secret-key") //optional
                .onWorkFinishListener((success, message) -> {
                    studentListProgress.setVisibility(View.GONE);
                    if (success) {
                        Toast.makeText(getContext(), "Database Saved To:"
                                +folder.getAbsolutePath()+"/"+fileName, Toast.LENGTH_LONG).show();
                    }else {
                        Toast.makeText(getContext(), "Failed to backup database "+message, Toast.LENGTH_LONG).show();
                    }
                })
                .execute();
    }
    private void restoreDatabase(String filePath){
        new Restore.Init()
                .database(AccountDatabase.getInstance(getContext()))
                .backupFilePath(filePath)
                .onWorkFinishListener((success, message) -> {
                    Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
                    studentListProgress.setVisibility(View.GONE);
                    if (success) {
                        Toast.makeText(getContext(), "Database Restored Successfully.", Toast.LENGTH_LONG).show();
                        SharedPreferences preferences= PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean(DataClass.AUTHENTICATION_STATUS, false);
                        editor.remove(DataClass.USER_NAME);
                        editor.remove(DataClass.USER_EMAIL);
                        editor.remove(DataClass.UID);
                        editor.commit();
                        getActivity().finish();
                    }else
                        Toast.makeText(getContext(), "Failed to Restore Database.", Toast.LENGTH_LONG).show();
                })
                .execute();
    }

    // Declare the activity result launcher
    ActivityResultLauncher<Intent> folderPickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        Uri uri = data.getData();
                        restoreDatabase(getFilePathFromUri(uri));
                    }
                }
            }
    );

    private String getFilePathFromUri(Uri uri) {
        String filePath = null;

        if (ContentResolver.SCHEME_FILE.equals(uri.getScheme())) {
            filePath = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            // Content scheme
            ContentResolver contentResolver = getContext().getContentResolver();
            Cursor cursor = null;

            try {
                cursor = contentResolver.query(uri, null, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    filePath = cursor.getString(index);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        return filePath;
    }

    private void exportStudentList() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Export Student List");
        builder.setMessage("Do you want to export student list?");

        builder.setPositiveButton("PDF", (dialog, which) -> {
            studentListProgress.setVisibility(View.VISIBLE);
            indicator=1;
            mViewModel.getStudent(authUser.getUid());
        });
        builder.setNegativeButton("Excel", (dialog, which) -> {
            studentListProgress.setVisibility(View.VISIBLE);
            indicator=-1;
            mViewModel.getStudent(authUser.getUid());
        });
        builder.setNeutralButton("Cancel", (dialog, which) -> {
            indicator=0;
            dialog.dismiss();
        });
        builder.show();
    }

    private void exportPdfStudentList(List<Students> studentsList) {
        ArrayList<String[]> list=new ArrayList<>();

        list.add(new String[]{
                "UID",
                "Name",
                "ID",
                "Phone",
                "Address",
                "College",
                "Guardian",
                "Class ID",
                "Class Name",
                "Group ID",
                "Group Name",
                "Starting Date"
        });

        for (Students students:studentsList){
            list.add(new String[]{
                    String.valueOf(students.getStudent_id()),
                    students.getFull_name(),
                    students.getId(),
                    students.getPhone(),
                    students.getAddress(),
                    students.getSchool_name(),
                    students.getGuardian_name(),
                    String.valueOf(students.getClass_id()),
                    students.getClass_name(),
                    String.valueOf(students.getGroup_id()),
                    students.getGroup_name(),
                    DateObj.longToMonthYear(students.getStarting_date())
            });
        }

        String csvPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/MyAccount/AllStudentList.csv";
        String pdfPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/MyAccount";



        if (CSVUtils.writeListToCSV(list, csvPath) &&
                CSVtoPDFConverter.convertCSVtoPDF(csvPath, "AllStudentList.pdf", list.get(0).length)) {
            Toast.makeText(getContext(), "Exported to: "+pdfPath+"/"+"AllStudentList.pdf", Toast.LENGTH_LONG).show();
            studentListProgress.setVisibility(View.GONE);
        }else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            studentListProgress.setVisibility(View.GONE);
        }
    }
    private void exportExcelStudentList(List<Students> studentsList) {
        ArrayList<String[]> list=new ArrayList<>();

        list.add(new String[]{
                "UID",
                "Name",
                "ID",
                "Phone",
                "Address",
                "College",
                "Guardian",
                "Class ID",
                "Class Name",
                "Group ID",
                "Group Name",
                "Starting Date",
        });
        for (Students students:studentsList){
            list.add(new String[]{
                    String.valueOf(students.getStudent_id()),
                    students.getFull_name(),
                    students.getId(),
                    students.getPhone(),
                    students.getAddress(),
                    students.getSchool_name(),
                    students.getGuardian_name(),
                    String.valueOf(students.getClass_id()),
                    students.getClass_name(),
                    String.valueOf(students.getGroup_id()),
                    students.getGroup_name(),
                    DateObj.longToMonthYear(students.getStarting_date())
            });
        }
        String fineName="AllStudentList.xlsx";

        String path= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"/MyAccount"+"/"+fineName;

        if (ListToXlsxConverter.convertToXlsx(list, path)){
            studentListProgress.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Exported to: "+path, Toast.LENGTH_LONG).show();
        }else {
            studentListProgress.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Failed to export", Toast.LENGTH_LONG).show();
        }
    }

    private void exportAttendanceList() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Export Attendance List");
        builder.setMessage("Do you want to export attendance list?");

        builder.setPositiveButton("PDF", (dialog, which) -> {
            studentListProgress.setVisibility(View.VISIBLE);
            indicator=1;
            mViewModel.getAttendance(authUser.getUid());
        });
        builder.setNegativeButton("Excel", (dialog, which) -> {
            studentListProgress.setVisibility(View.VISIBLE);
            indicator=-1;
            mViewModel.getAttendance(authUser.getUid());
        });
        builder.setNeutralButton("Cancel", (dialog, which) -> {
            indicator=0;
            dialog.dismiss();
        });
        builder.show();
    }

    private void exportPaymentList(){
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Export Payment List");
        builder.setMessage("Do you want to export payment list?");

        builder.setPositiveButton("PDF", (dialog, which) -> {
            studentListProgress.setVisibility(View.VISIBLE);
            indicator=1;
            mViewModel.getPayment(authUser.getUid());
        });
        builder.setNegativeButton("Excel", (dialog, which) -> {
            studentListProgress.setVisibility(View.VISIBLE);
            indicator=-1;
            mViewModel.getPayment(authUser.getUid());
        });
        builder.setNeutralButton("Cancel", (dialog, which) -> {
            indicator=0;
            dialog.dismiss();
        });
        builder.show();
    }

    private void exportPdfAttendanceList(List<Attendance> attendanceList) {
        ArrayList<String[]> list = new ArrayList<>();

        list.add(new String[]{
                "UID",
                "Name",
                "ID",
                "Phone",
                "Class ID",
                "Class Name",
                "Group ID",
                "Group Name",
                "Date",
                "Attendance"
        });

        for (Attendance attendance : attendanceList) {
            list.add(new String[]{
                    String.valueOf(attendance.getStudent_id()),
                    attendance.getStudent_name(),
                    attendance.getId(),
                    attendance.getPhone(),
                    String.valueOf(attendance.getClass_id()),
                    attendance.getClass_name(),
                    String.valueOf(attendance.getGroup_id()),
                    attendance.getGroup_name(),
                    DateObj.longToDateString(attendance.getDate()),
                    attendance.isAttended()? "Present":"Absent"
            });
        }

        String csvPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/MyAccount/AllAttendanceList.csv";
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/MyAccount";

        if (CSVUtils.writeListToCSV(list, csvPath) &&
                CSVtoPDFConverter.convertCSVtoPDF(csvPath, "AllAttendanceList.pdf", list.get(0).length)) {
            Toast.makeText(getContext(), "Exported to: " + pdfPath + "/" + "AllAttendanceList.pdf", Toast.LENGTH_LONG).show();
            studentListProgress.setVisibility(View.GONE);
        } else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            studentListProgress.setVisibility(View.GONE);
        }

    }

    private void exportExcelAttendanceList(List<Attendance> attendanceList) {
        ArrayList<String[]> list = new ArrayList<>();

        list.add(new String[]{
                "UID",
                "Name",
                "ID",
                "Phone",
                "Class ID",
                "Class Name",
                "Group ID",
                "Group Name",
                "Date",
                "Attendance"
        });

        for (Attendance attendance : attendanceList) {
            list.add(new String[]{
                    String.valueOf(attendance.getStudent_id()),
                    attendance.getStudent_name(),
                    attendance.getId(),
                    attendance.getPhone(),
                    String.valueOf(attendance.getClass_id()),
                    attendance.getClass_name(),
                    String.valueOf(attendance.getGroup_id()),
                    attendance.getGroup_name(),
                    DateObj.longToDateString(attendance.getDate()),
                    attendance.isAttended()? "Present":"Absent"
            });
        }

        String fineName = "AllAttendanceList.xlsx";

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/MyAccount" + "/" + fineName;

        if (ListToXlsxConverter.convertToXlsx(list, path)) {
            studentListProgress.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Exported to: " + path, Toast.LENGTH_LONG).show();
        } else {
            studentListProgress.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Failed to export", Toast.LENGTH_LONG).show();
        }
    }

    private void exportPdfPaymentList(List<Payments> paymentList) {
        ArrayList<String[]> list = new ArrayList<>();

        list.add(new String[]{
                "UID",
                "Name",
                "ID",
                "Phone",
                "Class ID",
                "Class Name",
                "Group ID",
                "Group Name",
                "Month",
                "Amount",
                "Payment",
                "Payment Date"
        });

        for (Payments payment : paymentList) {
            if (payment.getPayment_status()) {
                list.add(new String[]{
                        String.valueOf(payment.getStudent_id()),
                        payment.getStudent_name(),
                        payment.getId(),
                        payment.getPhone(),
                        String.valueOf(payment.getClass_id()),
                        payment.getClass_name(),
                        String.valueOf(payment.getGroup_id()),
                        payment.getGroup_name(),
                        DateObj.longToMonthYear(payment.getPayment_month()),
                        String.valueOf(payment.getPayment_amount()),
                        payment.getPayment_status() ? "Paid" : "Due",
                        DateObj.timestampToDateString(payment.getPayment_timestamp())
                });
            }else {
                list.add(new String[]{
                        String.valueOf(payment.getStudent_id()),
                        payment.getStudent_name(),
                        payment.getId(),
                        payment.getPhone(),
                        String.valueOf(payment.getClass_id()),
                        payment.getClass_name(),
                        String.valueOf(payment.getGroup_id()),
                        payment.getGroup_name(),
                        DateObj.longToMonthYear(payment.getPayment_month()),
                        String.valueOf(payment.getPayment_amount()),
                        (payment.getPayment_status()? "Paid":"Due"),
                        "Not Paid"});
                }
            }

        String csvPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/MyAccount/AllPaymentList.csv";
        String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/MyAccount";

        if (CSVUtils.writeListToCSV(list, csvPath) &&
                CSVtoPDFConverter.convertCSVtoPDF(csvPath, "AllPaymentList.pdf", list.get(0).length)) {
            Toast.makeText(getContext(), "Exported to: " + pdfPath + "/" + "AllPaymentList.pdf", Toast.LENGTH_LONG).show();
            studentListProgress.setVisibility(View.GONE);
        } else {
            Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
            studentListProgress.setVisibility(View.GONE);
        }

    }

    private void exportExcelPaymentList(List<Payments> paymentList) {
        ArrayList<String[]> list = new ArrayList<>();

        list.add(new String[]{
                "UID",
                "Name",
                "ID",
                "Phone",
                "Class ID",
                "Class Name",
                "Group ID",
                "Group Name",
                "Month",
                "Amount",
                "Payment",
                "Payment Date"
        });

        for (Payments payment : paymentList) {
            if (payment.getPayment_status()) {
                list.add(new String[]{
                        String.valueOf(payment.getStudent_id()),
                        payment.getStudent_name(),
                        payment.getId(),
                        payment.getPhone(),
                        String.valueOf(payment.getClass_id()),
                        payment.getClass_name(),
                        String.valueOf(payment.getGroup_id()),
                        payment.getGroup_name(),
                        DateObj.longToMonthYear(payment.getPayment_month()),
                        String.valueOf(payment.getPayment_amount()),
                        payment.getPayment_status() ? "Paid" : "Due",
                        DateObj.timestampToDateString(payment.getPayment_timestamp())
                });
            } else {
                list.add(new String[]{
                        String.valueOf(payment.getStudent_id()),
                        payment.getStudent_name(),
                        payment.getId(),
                        payment.getPhone(),
                        String.valueOf(payment.getClass_id()),
                        payment.getClass_name(),
                        String.valueOf(payment.getGroup_id()),
                        payment.getGroup_name(),
                        DateObj.longToMonthYear(payment.getPayment_month()),
                        String.valueOf(payment.getPayment_amount()),
                        (payment.getPayment_status() ? "Paid" : "Due"),
                        "Not Paid"});
            }
        }

        String fineName = "AllPaymentList.xlsx";

        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/MyAccount" + "/" + fineName;

        if (ListToXlsxConverter.convertToXlsx(list, path)) {
            studentListProgress.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Exported to: " + path, Toast.LENGTH_LONG).show();
        } else {
            studentListProgress.setVisibility(View.GONE);
            Toast.makeText(getContext(), "Failed to export", Toast.LENGTH_LONG).show();
        }
    }

}