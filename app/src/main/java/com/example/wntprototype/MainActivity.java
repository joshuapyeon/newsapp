package com.example.wntprototype;

import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.fragment.NavHostFragment;

import android.widget.Toast;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.GoogleAPIs.GTSWrapper;
import com.example.wntprototype.databinding.ActivityMainBinding;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String[] topics = { "COVID", "Sports", "World Events" };
    private final boolean[] selectedTopics = { true, true, true };
    private final String[] sources = { "CNN", "NBC", "FOX" };
    private final boolean[] selectedSources = { true, true, true };
    private final String[] visualizations = { "List", "Graph", "Word Map" };
    private int currVisualization = 0;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        this.findViewById(R.id.search_bar).setOnClickListener((u1) -> MainActivity.this.findViewById(R.id.filter_view).setVisibility(View.VISIBLE));
        this.findViewById(R.id.search_bar).setOnKeyListener((u1, u2, u3) -> {
            MainActivity.this.findViewById(R.id.filter_view).setVisibility(View.VISIBLE);
            return true;
        });
        ((SearchView)this.findViewById(R.id.search_bar)).setOnCloseListener(() -> {
            MainActivity.this.executeSearch(((SearchView)this.findViewById(R.id.search_bar)).getQuery().toString());
            return true;
        });
        this.findViewById(R.id.button_search).setOnClickListener((u1) -> MainActivity.this.executeSearch(((SearchView)this.findViewById(R.id.search_bar)).getQuery().toString()));
        this.findViewById(R.id.button_close).setOnClickListener((u1) -> MainActivity.this.findViewById(R.id.filter_view).setVisibility(View.GONE));

        this.findViewById(R.id.topic_filter).setOnClickListener((u1) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.findViewById(R.id.filter_view).getContext());

            builder.setTitle("Filter by Topic");
            builder.setCancelable(false);
            builder.setMultiChoiceItems(topics, selectedTopics, (v1, v2, v3) -> selectedTopics[v2] = v3);
            builder.setPositiveButton("OK", (v1, v2) -> {  });
            builder.show();
        });

        this.findViewById(R.id.source_filter).setOnClickListener((u1) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this.findViewById(R.id.filter_view).getContext());

            builder.setTitle("Filter by Source");
            builder.setCancelable(false);
            builder.setMultiChoiceItems(sources, selectedSources, (v1, v2, v3) -> selectedSources[v2] = v3);
            builder.setPositiveButton("OK", (v1, v2) -> {  });
            builder.show();
        });

        this.findViewById(R.id.visualize_button).setOnClickListener((u1) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(binding.getRoot().getContext());

            builder.setTitle("Choose Visualization");
            builder.setCancelable(false);
            builder.setSingleChoiceItems(visualizations, currVisualization, (v1, v2) -> currVisualization = v2);
            builder.setPositiveButton("OK", (v1, v2) -> {
                int dest;
                switch (currVisualization) {
                    case 0:
                        dest = R.id.navigation_home;
                        break;
                    case 1:
                        dest = R.id.navigation_dashboard;
                        break;
                    default:
                        dest = R.id.navigation_notifications;
                        break;
                }
                ((NavHostFragment)((FragmentContainerView)this.findViewById(R.id.nav_host_fragment_activity_main)).getFragment()).getNavController().navigate(dest);
            });
            builder.show();
        });

        this.findViewById(R.id.share_button).setOnClickListener((u1) -> {
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

        //Builds the search.  Right now, the only necessary parameter is the query, or the keyword
        //in the search.

//        APISearch search = new APISearch();
//        search.setQuery("tesla");

//        try {
            //All of the API Data pulls use an AsyncTask because it throws an error if there is a
            //network call on the main thread, so this gets the list of APIData

//            List<APIData> data = new GTSWrapper().execute(search).get();
//            data.get(0);
//        }catch(Exception e){
//            e.printStackTrace();
//        }
    }

    private void executeSearch(String keyword) {
        //Requires Backend...
        Toast.makeText(this.getBaseContext(), "Searching for headlines relating to " + keyword, Toast.LENGTH_LONG).show();
        this.findViewById(R.id.filter_view).setVisibility(View.GONE);
        ((SearchView)this.findViewById(R.id.search_bar)).setQuery("", false);
        for (int i = 0; i < this.selectedTopics.length; i++)
            selectedTopics[i] = true;
        for (int i = 0; i < this.selectedSources.length; i++)
            selectedSources[i] = true;
    }
}