package com.example.wntprototype.ui.graph;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.APIWrappers.TrendingContent;
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

public class GraphFragment extends Fragment {

    final private DataCache cache = DataCache.getCache();
    BarChart barChart;
    //private final Activity implementActivity = getActivity();
    ArrayList headlinesList = new ArrayList();
    private FragmentGraphBinding binding;
    ArrayList barEntries = new ArrayList();
    List<TrendingContent> data;
    ArrayList<BarDataSet> set = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGraphBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //getActivity().setContentView(R.layout.fragment_graph);

        //barChart = getActivity().findViewById(R.id.bargraph);
        barChart = (BarChart) root.findViewById(R.id.bargraph);
        getData();
        if (cache.hasData()) {
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
        return root;
    }

    //sets y-axis data points
    private void getData() {
        float value = 5;
        //data = cache.getData();
        for (int i = 5; i > 0; i--) {
            value = value + 1;
            barEntries.add(new BarEntry(value, i));
        }
    }

    private void setData() {
        Toast.makeText(getContext(), "Display graph", Toast.LENGTH_LONG).show();
        data = cache.getData();
        for (TrendingContent myData : data) {
            headlinesList.add(myData.getPhrase());
            //set.add(new BarDataSet(barEntries, myData.getToParse()));
            int storeIndex = myData.getPhrase().indexOf(" ");
            BarDataSet forHeadlines = new BarDataSet(barEntries, myData.getPhrase().substring(0, storeIndex));
            forHeadlines.setColors(ColorTemplate.JOYFUL_COLORS);
            set.add(forHeadlines);
        }
    }

}
