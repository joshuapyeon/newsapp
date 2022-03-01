package com.example.wntprototype.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wntprototype.MainActivity;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentHomeBinding;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
    TextView filterResults;
    ArrayList<Integer> arraylist = new ArrayList<Integer>();
    boolean[] selectedTopics;
    boolean sportsSelected = false;
    boolean WESelected = false;
    boolean COVIDSelected = false;

    boolean applyFilter = false;
    String[] topics = {"Sports", "World Events", "COVID"};
    private FragmentHomeBinding binding;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        filterResults = root.findViewById(R.id.textView);
        filterResults.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());

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
                        if(!COVIDSelected && !WESelected && !sportsSelected)
                            applyFilter = false;
                        else
                            applyFilter = true;
                    }
                });
                builder.show();
            }
        });
        selectedTopics = new boolean[topics.length];
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}