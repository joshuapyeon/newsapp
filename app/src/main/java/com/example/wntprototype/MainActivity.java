package com.example.wntprototype;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.view.View;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import com.example.wntprototype.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private final String[] topics = { "COVID", "Sports", "World Events" };
    private final boolean[] selectedTopics = { true, true, true };
    private final String[] sources = { "CNN", "NBC", "FOX" };
    private final boolean[] selectedSources = { true, true, true };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        com.example.wntprototype.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
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
            //Open visualize dropdown
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