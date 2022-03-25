package com.example.wntprototype.ui.list;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;

import android.widget.TextView;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.WebSearchAPI.WebSearchData;
import com.example.wntprototype.APIWrappers.WebSearchAPI.WebSearchWrapper;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentListBinding;
import com.example.wntprototype.databinding.FragmentWordMapBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.*;
import java.util.concurrent.ExecutionException;

public class ListFragment extends Fragment {
    APISearch search = new APISearch();
    List<APIData> searchList;
    List<String> apiToStringList = new ArrayList<String>();
    private FragmentListBinding binding;
    ListView listFormat;
    ArrayAdapter arrayAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listFormat = root.findViewById(R.id.listFormat);
        search.setQuery("tesla");
        {
            try {
                searchList = new WebSearchWrapper().execute(search).get();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //TODO: Parse the APIData as a string.
        int numID = 0;
        for(APIData api : searchList) {
            apiToStringList.add("#" + numID++ + ": " + api.getToParse());
        }

        arrayAdapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_list_item_1, apiToStringList);
        listFormat.setAdapter(arrayAdapter);
        listFormat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Snackbar sb = Snackbar.make(root, apiToStringList.get(i),Snackbar.LENGTH_SHORT);
                sb.setAction("MORE INFO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("URL:");
                        builder.setMessage("Selected URL goes here");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        AlertDialog url = builder.create();
                        url.show();
                    }
                });
                sb.show();
            }
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}