package com.example.wntprototype.ui.graph;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.GoogleAPIs.TrendingWrapper;
import com.example.wntprototype.DataCache;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentGraphBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GraphFragment extends Fragment {

    BarChart barChart;
    private final Activity implementActivity = getActivity();
    ArrayList headlinesList = new ArrayList();
    private FragmentGraphBinding binding;
    ArrayList barEntries = new ArrayList();
    List<APIData> data;
    ArrayList<BarDataSet> set = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) { //throws ExecutionException, InterruptedException {

        //binding = FragmentGraphBinding.inflate(inflater, container, false);
        //


        super.onCreate(savedInstanceState);
        getActivity().setContentView(R.layout.fragment_graph);
        barChart = getActivity().findViewById(R.id.bargraph);
        //setData();
        getData();
        setData();
        BarDataSet barDataSetY = new BarDataSet(barEntries, "Ranks");
        BarDataSet barDataSetX = new BarDataSet(barEntries, "Headlines");
        BarData barData = new BarData();
        for (BarDataSet mySet : set) {
            barData.addDataSet(mySet);
        }
        barChart.setData(barData);
        barDataSetY.setColors(ColorTemplate.PASTEL_COLORS);
        barDataSetY.setValueTextColor(Color.BLACK);
        barDataSetY.setValueTextSize(16f);


    }

    //sets y-axis data points
    private void getData() {
        float value = 20;
        for (int i = 3; i > 0; i--) {
            value = value + 5;
            barEntries.add(new BarEntry(value, i));
        }
    }

    private void setData() {

        DataCache cache = DataCache.getCache();
        if (cache.hasData()) {
            data = cache.getData();
            // for (APIData myData : data) {
            //   headlinesList.add(myData.getToParse());
            // }
        } else {
            APISearch search = new APISearch();
            Toast.makeText(getContext(), "Display graph", Toast.LENGTH_LONG).show();
            try {
                data = new TrendingWrapper().execute(search).get();
                for (APIData myData : data) {
                    headlinesList.add(myData.getToParse());
                    set.add(new BarDataSet(barEntries, myData.getToParse()));
                }
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //className.dataCache.getCache.hasData();
        //sets x-axis values
    }


    // @Override
    //public void onDestroyView() {
    //  super.onDestroyView();
    // binding = null;
    //}


}
