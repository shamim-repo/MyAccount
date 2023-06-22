package com.telentandtech.myaccount.ui.main.drawer.dashBoard;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.graphics.Color;
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

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import com.telentandtech.myaccount.R;
import com.telentandtech.myaccount.core.DataClass;
import com.telentandtech.myaccount.core.DateObj;
import com.telentandtech.myaccount.database.entityes.User;
import com.telentandtech.myaccount.database.resultObjects.AttendanceCount;
import com.telentandtech.myaccount.database.resultObjects.GroupNameID;
import com.telentandtech.myaccount.database.resultObjects.PaidUnpaidByMonth;
import com.telentandtech.myaccount.database.resultObjects.PaidUnpaidCountResult;

import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment {

    private DashboardViewModel mViewModel;
    private BarChart paymentByMontBarChart;
    private PieChart attendancePieChart;
    private PieChart paymentPieChart;
    private User authUser;
    private Spinner yearSpinner;
    private Spinner groupSpinner;

    private boolean countLoad = false;
    private List<GroupNameID> groupList = new ArrayList<>();


    public static DashboardFragment newInstance() {
        return new DashboardFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
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
        paymentByMontBarChart = view.findViewById(R.id.month_payment_bar_chart);
        attendancePieChart = view.findViewById(R.id.attendance_pi_chart);
        paymentPieChart = view.findViewById(R.id.paid_unpaid_pi_chart);
        yearSpinner = view.findViewById(R.id.dashboard_year_spinner);
        groupSpinner = view.findViewById(R.id.dashboard_group_spinner);


        ArrayAdapter<String> yearAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getYearRange());
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        ArrayAdapter<String> groupAdapter =
                new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, new String[]{"All"});
        groupAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupAdapter);

        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                mViewModel.getGroupListAll(authUser.getUid(),getYearLong(yearSpinner.getSelectedItem().toString()));
                paymentPieChart.clear();
                attendancePieChart.clear();
                paymentByMontBarChart.clear();
                if (groupSpinner.getSelectedItem().equals("All")) {
                    mViewModel.getAttendanceCount(authUser.getUid(), getYearLongLong(yearAdapter.getItem(position)),0l,true);
                    mViewModel.getDuePaidCount(authUser.getUid(), getYearLong(yearAdapter.getItem(position)),0l,true);
                    mViewModel.paidDueCountByMonth(authUser.getUid(), getYearLong(yearAdapter.getItem(position)),0l,true);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mViewModel.getGroupListAllLiveData().observe(getViewLifecycleOwner(), groups -> {
            if (groups != null && groups.size() > 0) {
                groupList=groups;
                ArrayAdapter<String> groupAdapter1 =
                        new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, getGroupList(groups));
                groupAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                groupSpinner.setAdapter(groupAdapter1);
            }
        });

        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                countLoad = true;
                paymentPieChart.clear();
                attendancePieChart.clear();
                paymentByMontBarChart.clear();

                if (groupSpinner.getSelectedItem().equals("All")) {
                    mViewModel.getAttendanceCount(authUser.getUid(), getYearLongLong(yearSpinner.getSelectedItem().toString()), 0l, true);
                    mViewModel.getDuePaidCount(authUser.getUid(), getYearLong(yearSpinner.getSelectedItem().toString()), 0l, true);
                    mViewModel.paidDueCountByMonth(authUser.getUid(), getYearLong(yearSpinner.getSelectedItem().toString()), 0l, true);
                } else {
                    mViewModel.getAttendanceCount(authUser.getUid(), getYearLongLong(yearSpinner.getSelectedItem().toString()), groupList.get(position-1).getGroup_id(), false);
                    mViewModel.getDuePaidCount(authUser.getUid(), getYearLong(yearSpinner.getSelectedItem().toString()), groupList.get(position-1).getGroup_id(), false);
                    mViewModel.paidDueCountByMonth(authUser.getUid(), getYearLong(yearSpinner.getSelectedItem().toString()), groupList.get(position-1).getGroup_id(), false);

                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        mViewModel.getAttendanceCountLiveData().observe(getViewLifecycleOwner(), attendanceCount -> {
            if (attendanceCount.getTotal_count() != null ) {
                setAttendancePiChart(attendanceCount);
            }
        });


        mViewModel.getPaidUnpaidCountLiveData().observe(getViewLifecycleOwner(), duePaidCount -> {
            if (duePaidCount != null && duePaidCount.getTotalCount() != null) {
                setPaidUnpaintedChart(duePaidCount);
            }
        });

        mViewModel.getPaidUnpaidByMonthLiveData().observe(getViewLifecycleOwner(), paidUnpaidByMonth -> {
            if (paidUnpaidByMonth != null && paidUnpaidByMonth.size() > 0) {
                setMonthlyPaymentDueStackedBarChart(paidUnpaidByMonth);
            }
        });


    }

    private String[] getGroupList(List<GroupNameID> groups) {
        String[] groupNames = new String[groups.size()+1];
        groupNames[0]="All";
        int i=1;
        for(GroupNameID groupNameID:groups){
            groupNames[i++]=groupNameID.getGroup_name();
        }
        return groupNames;
    }

    private void setMonthlyPaymentDueStackedBarChart(List<PaidUnpaidByMonth> paidUnpaidByMonth) {

        paymentByMontBarChart.setDrawBarShadow(false);
        paymentByMontBarChart.setDrawValueAboveBar(true);
        paymentByMontBarChart.getDescription().setEnabled(false);
        paymentByMontBarChart.setPinchZoom(false);
        paymentByMontBarChart.setDrawGridBackground(false);
        paymentByMontBarChart.setDrawBorders(false);


        ArrayList<BarEntry> group1 = new ArrayList<>();
        ArrayList<String> xAxisLabels = new ArrayList<>();


        int i=0;
        for(PaidUnpaidByMonth paidUnpaidByMonth1:paidUnpaidByMonth) {
            group1.add(new BarEntry(i++, new float[]{paidUnpaidByMonth1.getPaid(), paidUnpaidByMonth1.getDue()}));
            xAxisLabels.add(DateObj.longToMonthYearShort(paidUnpaidByMonth1.getMonth()));
        }
        BarDataSet barDataSet1 = new BarDataSet(group1, "");
        barDataSet1.setColors(Color.GREEN, Color.RED);
        barDataSet1.setStackLabels(new String[]{"Paid", "Due"});

        BarData barData = new BarData(barDataSet1);
        paymentByMontBarChart.setData(barData);

        // Customize the X-axis and Y-axis as needed
        XAxis xAxis = paymentByMontBarChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.TOP);
        xAxis.setGranularity(1f);

        xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisLabels));

        YAxis leftAxis = paymentByMontBarChart.getAxisLeft();
        YAxis rightAxis = paymentByMontBarChart.getAxisRight();
        leftAxis.setAxisMinimum(0f);
        rightAxis.setAxisMinimum(0f);

// Customize the legend as needed
        Legend legend = paymentByMontBarChart.getLegend();
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setStackSpace(10f);
        paymentByMontBarChart.invalidate();

    }

    private void setAttendancePiChart(AttendanceCount attendanceCount){
        attendancePieChart.setCenterText("Attendance");
        attendancePieChart.setCenterTextSize(10);
        attendancePieChart.setCenterTextColor(ContextCompat.getColor(getContext(), R.color.black));
        attendancePieChart.setCenterTextRadiusPercent(100);
        attendancePieChart.setHoleRadius(50);
        attendancePieChart.setTransparentCircleRadius(55);
        attendancePieChart.getDescription().setEnabled(false);
        attendancePieChart.setDrawEntryLabels(true);
        attendancePieChart.setDrawHoleEnabled(true);
        attendancePieChart.setDrawCenterText(true);
        attendancePieChart.setUsePercentValues(true);
        attendancePieChart.setEntryLabelColor(ContextCompat.getColor(getContext(), R.color.black));
        attendancePieChart.setEntryLabelTextSize(10);
        attendancePieChart.setExtraOffsets(5, 10, 5, 5);
        attendancePieChart.setDragDecelerationFrictionCoef(0.95f);

        float total=100f/attendanceCount.getTotal_count();
        float present=attendanceCount.getPresent_count()!=null?total*attendanceCount.getPresent_count():0;
        float absent=attendanceCount.getAbsent_count()!=null?total*attendanceCount.getAbsent_count():0;

        List<PieEntry> entries = new ArrayList<>();

        if (present>0)
            entries.add(new PieEntry(total*attendanceCount.getPresent_count(), "Present"));
        if (absent>0)
            entries.add(new PieEntry(total*attendanceCount.getAbsent_count(), "Absent"));

        if (entries.size()==0){
            entries.add(new PieEntry(100, "No Data"));
            return;
        }
        PieDataSet set = new PieDataSet(entries, "Attendance");
        set.setColors(ContextCompat.getColor(getContext(), R.color.light_green),
                ContextCompat.getColor(getContext(), R.color.light_red));

        PieData data = new PieData(set);
        attendancePieChart.setData(data);
        attendancePieChart.invalidate();

    }

    private void setPaidUnpaintedChart(PaidUnpaidCountResult paidUnpaintedCount){
        paymentPieChart.setCenterText("Payment");
        paymentPieChart.setCenterTextSize(10);
        paymentPieChart.setCenterTextColor(ContextCompat.getColor(getContext(), R.color.black));
        paymentPieChart.setCenterTextRadiusPercent(100);
        paymentPieChart.setHoleRadius(50);
        paymentPieChart.setTransparentCircleRadius(55);
        paymentPieChart.getDescription().setEnabled(false);
        paymentPieChart.setDrawEntryLabels(true);
        paymentPieChart.setDrawHoleEnabled(true);
        paymentPieChart.setDrawCenterText(true);
        paymentPieChart.setUsePercentValues(true);
        paymentPieChart.setEntryLabelColor(ContextCompat.getColor(getContext(), R.color.black));
        paymentPieChart.setEntryLabelTextSize(10);
        paymentPieChart.setExtraOffsets(5, 10, 5, 5);
        paymentPieChart.setDragDecelerationFrictionCoef(0.95f);

        float total = paidUnpaintedCount.getTotalCount()!=null? paidUnpaintedCount.getTotalCount()/(float)100.0: 0;
        float paid = paidUnpaintedCount.getPaidCount()!=null? paidUnpaintedCount.getPaidCount()*total: 0;
        float unpaid = paidUnpaintedCount.getUnpaidCount()!=null? paidUnpaintedCount.getUnpaidCount()*total: 0;

        List<PieEntry> entries = new ArrayList<>();


        if (paid>0)
            entries.add(new PieEntry(paid, "Paid"));
        if (unpaid>0)
            entries.add(new PieEntry(unpaid, "Unpaid"));

        if (entries.size()==0) {
            entries.add(new PieEntry(1, "No Payment"));
            return;
        }
        PieDataSet set = new PieDataSet(entries, "");
        set.setColors(ContextCompat.getColor(getContext(), R.color.light_green),
                ContextCompat.getColor(getContext(), R.color.light_red));

        PieData data = new PieData(set);
        paymentPieChart.setData(data);
        paymentPieChart.invalidate(); // refresh

    }

    //2023 to 2040 is the range of years in string array return
    private String[] getYearRange(){
        int startYear = 2023;
        int endYear = 2040;
        int size = endYear-startYear+1;
        String[] years = new String[size];
        for (int i=0; i<size; i++){
            years[i] = String.valueOf(startYear+i);
        }
        return years;
    }

    //year string to long
    private long getYearLong(String year){
        return Long.valueOf(year+"01");
    }
    //year to long long
    private long getYearLongLong(String year){
        return Long.valueOf(year+"0101");
    }




}