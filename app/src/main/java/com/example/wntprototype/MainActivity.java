package com.example.wntprototype;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import android.content.Intent;
import android.widget.*;
import java.util.*;
import com.example.wntprototype.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    TextView filterResults;
    ArrayList<Integer> arraylist = new ArrayList<Integer>();
    boolean[] selectedTopics;
    boolean sportsSelected = false;
    boolean WESelected = false;
    boolean COVIDSelected = false;
    String[] topics = {"Sports", "World Events", "COVID"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        BottomNavigationView navView = findViewById(R.id.nav_view);
        filterResults = findViewById(R.id.textView);
        filterResults.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(
                        MainActivity.this
                );
                builder.setTitle("Filter Results");
                builder.setCancelable(false);
                builder.setMultiChoiceItems(topics, selectedTopics, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                        if(b) {
                            if(topics[i].equals("Sports")) {
                                sportsSelected = true;
                            }
                            else if(topics[i].equals("World Events")) {
                                WESelected = true;
                            }
                            else {
                                COVIDSelected = true;
                            }
                        }
                        else {
                            if(topics[i].equals("Sports")) {
                                sportsSelected = false;
                            }
                            else if(topics[i].equals("World Events")) {
                                WESelected = false;
                            }
                            else {
                                COVIDSelected = false;
                            }
                        }
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(!COVIDSelected && !WESelected && !sportsSelected) {

                        }
                    }
                });
                builder.show();
            }
        });
        selectedTopics = new boolean[topics.length];
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        FragmentContainerView f = this.findViewById(R.id.nav_host_fragment_activity_main);
        NavController navController = ((NavHostFragment)f.getFragment()).getNavController();
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }
}