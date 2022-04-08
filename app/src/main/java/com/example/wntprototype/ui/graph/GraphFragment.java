package com.example.wntprototype.ui.graph;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wntprototype.APIWrappers.TrendingContent;
import com.example.wntprototype.DataCache;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentGraphBinding;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment {

    /**
     * Cache that hodls the cached data
     */
    final private DataCache cache = DataCache.getCache();
    /**
     * The following instance variables are
     * for creating the structure of the graph
     */

    private int XMAX = 5;
    private final String graphLabel = "Results & Values";
    /**
     * The bar chart to be displayed
     */
    BarChart barChart;
    /**
     * The data that is retrieved from the user's
     * search (relevant articles or phrases)
     */
    List<TrendingContent> data;
    ArrayList<String> words = new ArrayList<>();
    ArrayList<Integer> values = new ArrayList<>();

    /**
     * Method that builds the graph view based
     * on the user's search.
     *
     * @param inflater provided by Android API
     * @param container provided by Android API
     * @param savedInstanceState provided by Android API
     * @return The Graph view
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //setting up the Graph fragment with a FragmentGraphBinding
        /*
         * Creates the binding for the
         * graph fragment
         */
        com.example.wntprototype.databinding.FragmentGraphBinding binding = FragmentGraphBinding.inflate(inflater, container, false);
        View root = binding.getRoot(); //sets the view by getting the root

        barChart = root.findViewById(R.id.bargraph);

        if (cache.hasData()) {
            setData();
            setAppearance();
            BarData barData = makeBarData();
            barData.setValueTextSize(16f);
            barChart.setData(barData);
        }
        return root;
    }


    private void setAppearance() {
        barChart.getDescription().setEnabled(true);
        barChart.getDescription().setText(graphLabel);
        XAxis xAxis = barChart.getXAxis();
        xAxis.getValueFormatter();
        //  @Override
        //public String xFormatter(int val){
        //   return words.get(val);
        //  }
        // });

        YAxis yLeft = barChart.getAxisLeft();
        yLeft.setGranularity(5f);
        yLeft.setAxisMinimum(0);

        YAxis yRight = barChart.getAxisRight();
        yRight.setGranularity(5f);
        yRight.setAxisMinimum(0);

    }

    private void setData() {
        Toast.makeText(getContext(), "Display graph", Toast.LENGTH_LONG).show();
        data = cache.getData();
        for (TrendingContent myData : data) {
            words.add(myData.getPhrase());
            values.add(myData.getValue());

        }
        if (values.size() < 5) {
            XMAX = values.size();
        }

    }

    private BarData makeBarData() {
        ArrayList<BarEntry> mappings = new ArrayList<>();
        for (int i = 0; i < XMAX; i++) {
            int thisNum = values.get(i);
            System.out.println(thisNum);
            mappings.add(new BarEntry(i, thisNum));
        }
        BarDataSet set = new BarDataSet(mappings, graphLabel);
        set.setColors(ColorTemplate.JOYFUL_COLORS);
        ArrayList<IBarDataSet> myDataSets = new ArrayList<>();
        myDataSets.add(set);
        return new BarData(myDataSets);


    }

}
