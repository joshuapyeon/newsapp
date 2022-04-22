package com.example.wntprototype.ui.graph;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wntprototype.APIWrappers.TrendingContent;
import com.example.wntprototype.DataCache;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentGraphBinding;
import com.example.wntprototype.ui.Shareable;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

public class GraphFragment extends Fragment implements Shareable {

    /**
     * Cache that holds the cached data
     */
    final private DataCache cache = DataCache.getCache();
    /**
     * The label associated with the
     * graph (listed below the graph
     * when it is displayed)
     */

    private final String graphLabel = "Results & Values";
    /**
     * A list that stores all of the phrases
     * associated with each result of the search
     */
    ArrayList<String> words = new ArrayList<>();

    /**
     * The bar chart to be displayed
     */
    BarChart barChart;

    /**
     * The data that is retrieved from the user's
     * search (relevant articles or phrases)
     */
    List<TrendingContent> data;
    /**
     * A list that stores all of the values
     * associated with each result of the search.
     */
    ArrayList<Integer> values = new ArrayList<>();
    /**
     * The maximum value for the range
     * of the x-axis
     */

    private int XMAX = 5;

    private final String[] noWords = {"I", "The", "As", "For", "St.", "My", "A", "Your", "Yours", "That", "That's", "Why", "What", "With"};

    /**
     * Creates the binding for the
     * graph fragment
     */
    private FragmentGraphBinding binding;

    /**
     * Dialag Box to display phrases when a bar
     * on the bar chart is clicked.
     */
    AlertDialog alertDialog;

    public GraphFragment() {

    }

    /**
     * Method that builds the graph view based
     * on the user's search.
     *
     * @param inflater           unused
     * @param container          unused
     * @param savedInstanceState unused
     * @return The Graph view
     */


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //setting up the Graph fragment with a FragmentGraphBinding
        binding = FragmentGraphBinding.inflate(inflater, container, false);
        View root = binding.getRoot(); //sets the view by getting the root

        barChart = root.findViewById(R.id.bargraph); //attaches a bar chart template to this fragment

        //check whether an actual search was made for displaying data
        if (cache.hasData()) {
            setData(); //create the data that is to be displayed by operating on the results from the search
            setAppearance(); //sets the structure of the graph itself (x and y axis, etc.)
            BarData barData = makeBarData();
            makeListener();
            barData.setValueTextSize(16f);
            barChart.setData(barData);
        }
        return root;
    }
    /**
     * Sets up the structure of the graph
     * including the x-axis, as well as the y-axis.
     */
    private void setAppearance() {
        barChart.getDescription().setEnabled(false); //the description below the graph (the label) is not made visible
        barChart.getDescription().setText(graphLabel); //set the text for the description for the graph
        XAxis xAxis = barChart.getXAxis(); //obtain the x-axis for this graph
        //sets the x-axis labels for each of the bars in the bar chart
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            /*
             * Formats each x-axis bar label
             * only labeling by one word from each
             * phrase, if more than one
             * word exists.
             */
            public String getFormattedValue(float val) {
                String store = words.get((int) val);
                if (store.contains(" ")) {
                    int space = store.indexOf(" ");
                    return store.substring(0, space);
                }
                return store;
            }
        });
        xAxis.setGranularityEnabled(true);
        xAxis.setTextSize(12f);


        YAxis yLeft = barChart.getAxisLeft(); //get the left-side of the y-axis
        yLeft.setGranularity(5f); //every five units, a numeric label will be given
        yLeft.setAxisMinimum(0); //minimum y-value is 0


        YAxis yRight = barChart.getAxisRight(); //get the right-side of the y-axis
        yRight.setGranularity(5f); //every five units, a numeric label will be given
        yRight.setAxisMinimum(0); //minimum y-value is 0

    }

    /**
     * Obtains data from the cache,
     * which is data that is relevant
     * to the user's most recent search
     */
    private void setData() {
        Toast.makeText(getContext(), "Display graph", Toast.LENGTH_LONG).show();
        data = cache.getData(); //obtain search data from the cache

        for (TrendingContent myData : data) {
            words.add(myData.getPhrase()); //add phrases from the search results to an array list
            //System.out.println(myData.getPhrase());
            values.add(myData.getValue()); //add values corresponding to each phrase to an array list
        }
        //shows only the top five results in the bar graph for more visibility;
        //otherwise, if there are less than five results,
        //the maximum amount of bars will be set to whatever
        //numbers of values there are from that search
        if (values.size() < 5) {
            XMAX = values.size();
        }

    }

    /**
     * Creates the bar chart based on the
     * data. Actually builds the bar chart.
     *
     * @return barData The bar chart data to be displayed
     */
    private BarData makeBarData() {
        ArrayList<BarEntry> mappings = new ArrayList<>();
        //creates bar entries (x and y value to create a bar)
        //x values are indicated by which bar this is (0th, 1st, second, etc.)
        for (int i = 0; i < XMAX; i++) {
            int thisNum = values.get(i);
            // System.out.println(thisNum);
            mappings.add(new BarEntry(i, thisNum)); //add this bar entry to the array list of bar entries
        }
        BarDataSet set = new BarDataSet(mappings, graphLabel); //contains the set of all the bar entries
        set.setColors(ColorTemplate.JOYFUL_COLORS);
        ArrayList<IBarDataSet> myDataSets = new ArrayList<>(); //can contain bar entry sets, including more than one
        myDataSets.add(set); //add the bar entry set to list of BarDataSets
        return new BarData(myDataSets);
    }

    private void makeListener() {
        barChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                //get the index of the selected bar
                int barClicked = barChart.getBarData().getDataSetForEntry(e).getEntryIndex((BarEntry) e);
                String phrase = words.get(barClicked);
                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
                builder.setCancelable(true);
                View thisView = LayoutInflater.from(requireContext()).inflate(R.layout.popup_graphview, null);
                TextView thisPhrase = thisView.findViewById(R.id.phrase);
                thisPhrase.setText(phrase);
                builder.setView(thisView);
                alertDialog = builder.create();
                alertDialog = builder.show();


            }

            public void onNothingSelected() {

            }
        });
    }


    @Override
    public String getSharingType() {
        return "image/png";
    }

    @Override
    public File getSharingContent() {
        try {
            File parentDirectory = new File(this.requireContext().getFilesDir().getPath() + "/share");
            if (!parentDirectory.mkdir())
                System.out.println("Parent directory exists, skipping creation");
            File file = new File(parentDirectory, "/image.png");
            if (!file.createNewFile())
                System.out.println("File exists, skipping creation");
            PrintStream s = new PrintStream(file);
            if (file.setWritable(true)) {
                binding.bargraph.getChartBitmap().compress(Bitmap.CompressFormat.PNG, 100, s);
            } else
                throw new IOException("Failed to write to input file :(");
            s.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
