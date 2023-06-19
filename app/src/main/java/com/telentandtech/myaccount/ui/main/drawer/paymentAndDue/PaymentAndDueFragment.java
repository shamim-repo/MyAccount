package com.telentandtech.myaccount.ui.main.drawer.paymentAndDue;

import static com.telentandtech.myaccount.core.DataClass.UID;
import static com.telentandtech.myaccount.core.DataClass.USER_EMAIL;
import static com.telentandtech.myaccount.core.DataClass.USER_NAME;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.core.OnClickListener;
import com.telentandtech.myaccount.DocumentUtils.CSVUtils;
import com.telentandtech.myaccount.DocumentUtils.CSVtoPDFConverter;
import com.telentandtech.myaccount.DocumentUtils.PdfDocumentAdapter;
import com.telentandtech.myaccount.database.entityes.Payments;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.ui.main.drawer.paymentAndDue.recycleView.PaymentDueAdapter;

import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

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
    private boolean insertStatus=false;
    private boolean deleteStatus=false;

    private ExtendedFloatingActionButton printButton;


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
        printButton = view.findViewById(R.id.payment_print_fab);

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
                Log.d("PaymentAndDueFragment", "onViewCreated: "+classes.getClassNameIdList().size());
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
                if (!updatePaymentResult.getPayments().getPayment_status() && paymentStatusSpinner.getSelectedItem().equals("Paid")) {
                    adapter.paymentsList.remove(lastPayPosition);
                    adapter.notifyItemRemoved(lastPayPosition);
                    adapter.notifyItemRangeChanged(lastPayPosition, adapter.getItemCount());
                }else if (updatePaymentResult.getPayments().getPayment_status() && paymentStatusSpinner.getSelectedItem().equals("Due")){
                    adapter.paymentsList.remove(lastPayPosition);
                    adapter.notifyItemRemoved(lastPayPosition);
                    adapter.notifyItemRangeChanged(lastPayPosition, adapter.getItemCount());
                }
                Toast.makeText(getContext(), "Successful", Toast.LENGTH_SHORT).show();
                adapter.notifyItemChanged(lastPayPosition);
                updateStatus=false;
            }else
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        });

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("PaymentAndDueFragment", "onViewCreated: ");
                if (adapter == null)
                    return;
                if (adapter.paymentsList.size() == 0)
                    return;
                createPrintDocument(adapter.paymentsList);
            }
        });

        mViewModel.getDeletePaymentResultLiveData().observe(getViewLifecycleOwner(), deletePaymentResult -> {
            if (!deleteStatus)
                return;
            if(deletePaymentResult.isSuccessful()){
                adapter.paymentsList.remove(lastPayPosition);
                adapter.notifyItemRemoved(lastPayPosition);
                adapter.notifyItemRangeChanged(lastPayPosition, adapter.getItemCount());

                Snackbar.make(getView(), "Delete Successful", Snackbar.LENGTH_LONG)
                        .setAction("Undo", v -> {
                            mViewModel.insertPayment(deletePaymentResult.getPayments());
                            insertStatus = true;
                        }).show();
                deleteStatus=false;
            }else
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        });

        mViewModel.getInsertPaymentResultLiveData().observe(getViewLifecycleOwner(), insertPaymentResult -> {
            if (!insertStatus)
                return;
            if(insertPaymentResult.isSuccessful()){
                adapter.paymentsList.add(lastPayPosition, insertPaymentResult.getPayments());
                adapter.notifyItemInserted(lastPayPosition);
                adapter.notifyItemRangeChanged(lastPayPosition, adapter.getItemCount());
                insertStatus=false;
            }else
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
        });


    }

    //crete alardialog for delete payment
    private void createDeletePaymentDialog(Payments payments) {
        MaterialAlertDialogBuilder builder=
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Delete Payment")
                        .setMessage("Are you sure you want to delete this payment?")
                        .setPositiveButton("Delete", (dialog, which) -> {
                            mViewModel.deletePayment(payments);
                            deleteStatus = true;
                        })
                        .setNegativeButton("Cancel", (dialog, which) -> {
                            dialog.dismiss();
                        });
        builder.show();
    }
    private void createPrintDocument(List<Payments> paymentsList) {
        MaterialAlertDialogBuilder builder=
                new MaterialAlertDialogBuilder(getContext())
                        .setTitle("Print Attendance List")
                        .setMessage("Are you sure you want to print payment list?")
                        .setPositiveButton("Print", (dialog, which) -> {
                            ArrayList<List<String>> rows = new ArrayList<>();
                            List<String> list1 = new ArrayList<String>();
                            list1.add("UID");
                            list1.add("ID");
                            list1.add("Name");
                            list1.add("Group ID");
                            list1.add("Group");
                            list1.add("Phone");
                            list1.add("Month");
                            list1.add("Fee Amount");
                            list1.add("Status");
                            list1.add("Payment Date");

                            rows.add(list1);

                            for (Payments payments : paymentsList) {
                                List<String> list = new ArrayList<String>();
                                list.add(String.valueOf(payments.getStudent_id()));
                                list.add(String.valueOf(payments.getId()));
                                list.add(payments.getStudent_name());
                                list.add(String.valueOf(payments.getGroup_id()));
                                list.add(payments.getGroup_name());
                                list.add(payments.getPhone());
                                list.add(DateObj.longToMonthYear(payments.getPayment_month()));
                                list.add(String.valueOf(payments.getPayment_amount()));
                                if (payments.getPayment_status()) {
                                    list.add("Paid");
                                    list.add(DateObj.timestampToDateString(payments.getPayment_timestamp()));
                                }else {
                                    list.add("Due");
                                    list.add(" ");
                                }

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

                            File fileCsv = new File(getContext().getExternalFilesDir(null), "payment.csv");
                            CSVUtils.writeListToCSV(list2, fileCsv.getPath());
                            CSVtoPDFConverter.convertCSVtoPDF(fileCsv.getPath(), "payment.pdf", 10);

                            dir=new File(dir.getAbsolutePath(),"payment.pdf");

                            PdfDocumentAdapter documentAdapter = new PdfDocumentAdapter(getContext(),dir.getPath());
                            PrintManager printManager = (PrintManager) getActivity().getSystemService(Context.PRINT_SERVICE);
                            String jobName = getString(R.string.app_name) + " Print Payment List";
                            printManager.print(jobName, documentAdapter, null);
                        })
                        .setNegativeButton("No", (dialog, which) -> dialog.dismiss());
        builder.show();
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
            case "delete":
                lastPayPosition = position;
                createDeletePaymentDialog(adapter.paymentsList.get(position));
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