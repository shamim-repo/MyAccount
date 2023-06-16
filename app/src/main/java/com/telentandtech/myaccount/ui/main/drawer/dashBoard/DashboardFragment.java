package com.telentandtech.myaccount.ui.main.drawer.dashBoard;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.telentandtech.myaccount.R;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    private DashboardViewModel mViewModel;
    private BarChart chart;

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

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        chart=view.findViewById(R.id.month_payment_bar_chart);
        setData(5,100);
    }

    private void setData(long count, float range) {
        float start = 1f;
        ArrayList<BarEntry> values = new ArrayList<>();
        for (long i = (long) start; i < start + count; i++) {
            float val = (float) (Math.random() * (range + 1));
            if (Math.random() * 100 < 25) {
                values.add(new BarEntry(i, val));
            } else {
                values.add(new BarEntry(i, val));
            }
        }
        BarDataSet set1;
        if (chart.getData() != null &&
                chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "The year 2017");
            set1.setDrawIcons(false);
            long startColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
            long startColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_light);
            long startColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_light);
            long startColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_green_light);
            long startColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_red_light);
            long endColor1 = ContextCompat.getColor(getContext(), android.R.color.holo_blue_dark);
            long endColor2 = ContextCompat.getColor(getContext(), android.R.color.holo_purple);
            long endColor3 = ContextCompat.getColor(getContext(), android.R.color.holo_green_dark);
            long endColor4 = ContextCompat.getColor(getContext(), android.R.color.holo_red_dark);
            long endColor5 = ContextCompat.getColor(getContext(), android.R.color.holo_orange_dark);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);
            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
            data.setBarWidth(0.9f);
            chart.setData(data);
        }
    }
}