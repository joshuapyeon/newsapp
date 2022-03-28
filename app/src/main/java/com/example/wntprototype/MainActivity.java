package com.example.wntprototype;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.text.Editable;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.GoogleAPIs.GTSWrapper;
import com.example.wntprototype.APIWrappers.GoogleAPIs.TrendingWrapper;
import com.example.wntprototype.APIWrappers.WebSearchAPI.WebSearchWrapper;
import com.example.wntprototype.databinding.ActivityMainBinding;
import com.example.wntprototype.ui.graph.GraphFragment;
import com.example.wntprototype.ui.list.ListFragment;
import com.example.wntprototype.ui.wordmap.WordMapFragment;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * The different view options
     */
    private final String[] visualizations = { "List", "Graph", "Word Map" };

    /**
     * The list of different data sources that the application can pull from
     */
    private final String[] dataSources = { "News", "Google Trends", "Trending Searches" };

    /**
     * The current fragment value that we are seeing
     */
    private int currVisualization = 0;

    /**
     * The Main Activity xml Binding
     */
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Initializes the different options for the Data from the APIs
        ArrayAdapter arrayAdapter = new ArrayAdapter(this.getBaseContext(), R.layout.dropdown_item, dataSources);
        binding.dataSearchText.setAdapter(arrayAdapter);

        //Hides the Search Drop Down
        findViewById(R.id.filter_view).setVisibility(View.GONE);

        //Tells the search dropdown button to make the filters visible
        findViewById(R.id.search_dropdown_button).setOnClickListener((u1) -> MainActivity.this.findViewById(R.id.filter_view).setVisibility(View.VISIBLE));

        //This tells the search button to execute the search
        findViewById(R.id.button_search).setOnClickListener((u1) -> executeSearch());

        //This tells the close button to close the search drop down
        findViewById(R.id.close_search).setOnClickListener((u1) -> MainActivity.this.findViewById(R.id.filter_view).setVisibility(View.GONE));

        //Sets the onClickListener for the
        findViewById(R.id.visualize_button).setOnClickListener((u1) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());
            builder.setTitle("Choose Visualization");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(visualizations, currVisualization, (v1, v2) -> currVisualization = v2);
            builder.setPositiveButton("OK", (v1, v2) -> replaceFragment(getCurrVisualization()));
            builder.show();
        });

        //Sets the onClickListener for the Share button
        findViewById(R.id.share_button).setOnClickListener((u1) -> {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);

            // This will be the type of content; maybe PNG/JPEG image?
            sharingIntent.setType("text/plain");

            // Body of the content; maybe different types later
            String shareBody = "The stuff we wanted to share";
            String shareSubject = "Subject of the stuff we wanted to share";

            sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, shareSubject);
            startActivity(Intent.createChooser(sharingIntent, "Share using"));
        });
    }

    /**
     * This replaces the current fragment displayed on the main activity
     * @param fragment the fragment to be replaced to
     */
    private void replaceFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frame_layout, fragment);
            fragmentTransaction.commit();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * The Body of code that runs when the user presses the search button
     */
    private void executeSearch() {
        //The text in the keyword spot
        String dataSource = ((TextInputLayout)findViewById(R.id.data_search_layout)).getEditText().getText().toString();

        //These are just data pull dependencies, Eventually I would like to deal with this differently
        if(dataSource.equals("")){
            Toast.makeText(this.getBaseContext(), "Select Data Type", Toast.LENGTH_SHORT).show();
            return;
        }
        if(dataSource.equals(dataSources[2]) && dataSource.equals("")){
            Toast.makeText(this.getBaseContext(), "Search Requires Keyword", Toast.LENGTH_SHORT).show();
            return;
        }

        //Building the Search object for the search
        APISearch search = new APISearch();
        Editable keyword = ((TextInputEditText) findViewById(R.id.keyword_text)).getText();
        search.setQuery(keyword.toString());
        DataCache cache = DataCache.getCache();
        List<APIData> data = null;
        try {
//            All of the API Data pulls use an AsyncTask because it throws an error if there is a
//            network call on the main thread, so this gets the list of APIData
            Toast.makeText(this.getBaseContext(), "Searching...", Toast.LENGTH_SHORT).show();
            if(dataSource.equals(dataSources[1])){
                data = new TrendingWrapper().execute(search).get();
            }else if(dataSource.equals(dataSources[2])){
                data = new GTSWrapper().execute(search).get();
            }else if(dataSource.equals(dataSources[0])){
                data = new WebSearchWrapper().execute(search).get();
            }else{
                data = new WebSearchWrapper().execute(search).get();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        //Lets the user know if the search was successful
        if(data == null){
            Toast.makeText(this.getBaseContext(), "Search failed", Toast.LENGTH_LONG).show();
        }else{
            cache.setData(data);
            Toast.makeText(this.getBaseContext(), "Search Succeeded", Toast.LENGTH_SHORT).show();
        }
        //Hides the Search View and updates the current fragment
        this.findViewById(R.id.filter_view).setVisibility(View.GONE);
        replaceFragment(getCurrVisualization());
    }

    /**
     * Gets the current Fragment
     * @return The Fragment of the current visualization
     */
    private Fragment getCurrVisualization(){
        Fragment dest;
        switch (currVisualization) {
            case 0:
                dest = new ListFragment();
                break;
            case 1:
                dest = new GraphFragment();
                break;
            default:
                dest = new WordMapFragment();
                break;
        }
        return dest;
    }

}