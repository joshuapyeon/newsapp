package com.example.wntprototype.ui.home;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
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

    TextView Beijing;
    TextView Ukraine;
    TextView Paralympics;
    TextView LebronJames;
    TextView MLB;
    TextView MaskMan;
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
        Beijing = root.findViewById(R.id.Beijing);
        Ukraine = root.findViewById(R.id.Ukraine);
        Paralympics = root.findViewById(R.id.Paralympics);
        LebronJames = root.findViewById(R.id.LebronJames);
        MaskMan = root.findViewById(R.id.MaskMan);
        MLB = root.findViewById(R.id.MLB);
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
                        Log.d("COVIDSelected", COVIDSelected + "");
                        Log.d("WESelected", WESelected + "");
                        Log.d("sportsSelected", sportsSelected + "");
                        if(!COVIDSelected && !WESelected && !sportsSelected ||
                        COVIDSelected && WESelected && sportsSelected)
                            applyFilter = false;
                        else
                            applyFilter = true;
                        applyFilters(applyFilter);
                    }
                });
                builder.show();
            }
        });
        selectedTopics = new boolean[topics.length];
        return root;
    }

    /**
     * Applies the filters to the keywords
     * @param filtersApplied Boolean value that determines if to apply filters or not
     */
    public void applyFilters(boolean filtersApplied) {
        Log.d("FILTERSAPPLIEd", filtersApplied+"");
        if(!filtersApplied) {
            Paralympics.setAlpha(1.0f);
            Beijing.setAlpha(1.0f);
            Ukraine.setAlpha(1.0f);
            MLB.setAlpha(1.0f);
            LebronJames.setAlpha(1.0f);
            MaskMan.setAlpha(1.0f);
            return;
        }
        else {
            if(sportsSelected && WESelected && !COVIDSelected) {
                MaskMan.setAlpha(0.0f);
                LebronJames.setAlpha(1.0f);
                MLB.setAlpha(1.0f);
                Paralympics.setAlpha(1.0f);
                Beijing.setAlpha(1.0f);
                Ukraine.setAlpha(1.0f);
            }
            else if(COVIDSelected && WESelected && !sportsSelected) {
                MLB.setAlpha(0.0f);
                LebronJames.setAlpha(0.0f);
                Paralympics.setAlpha(1.0f);
                Beijing.setAlpha(1.0f);
                Ukraine.setAlpha(1.0f);
                MaskMan.setAlpha(1.0f);
            }
            else if(COVIDSelected && sportsSelected && !WESelected) {
                Paralympics.setAlpha(0.0f);
                Beijing.setAlpha(0.0f);
                Ukraine.setAlpha(0.0f);
                MLB.setAlpha(1.0f);
                LebronJames.setAlpha(1.0f);
                MaskMan.setAlpha(1.0f);
            }
            else if(COVIDSelected && !WESelected && !sportsSelected) {
                Paralympics.setAlpha(0.0f);
                Beijing.setAlpha(0.0f);
                Ukraine.setAlpha(0.0f);
                MLB.setAlpha(0.0f);
                LebronJames.setAlpha(0.0f);
                MaskMan.setAlpha(1.0f);
            }
            else if(WESelected && !sportsSelected && !COVIDSelected) {
                LebronJames.setAlpha(0.0f);
                MLB.setAlpha(0.0f);
                MaskMan.setAlpha(0.0f);
                Paralympics.setAlpha(1.0f);
                Beijing.setAlpha(1.0f);
                Ukraine.setAlpha(1.0f);
            }
            else if(sportsSelected && !WESelected && !COVIDSelected){
                Ukraine.setAlpha(0.0f);
                Beijing.setAlpha(0.0f);
                Paralympics.setAlpha(0.0f);
                MaskMan.setAlpha(0.0f);
                MLB.setAlpha(1.0f);
                LebronJames.setAlpha(1.0f);
            }
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}