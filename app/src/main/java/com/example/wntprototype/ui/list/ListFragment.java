package com.example.wntprototype.ui.list;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.GoogleAPIs.TrendingWrapper;
import com.example.wntprototype.APIWrappers.TrendingContent;
import com.example.wntprototype.APIWrappers.WebSearchAPI.WebSearchWrapper;
import com.example.wntprototype.DataCache;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentListBinding;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ListFragment extends Fragment {
    DataCache cache = DataCache.getCache();
    List<TrendingContent> searchList;
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
        APISearch search = new APISearch();

        if (cache.hasData()) {
            searchList = DataCache.getCache().getData();

            //TODO: Parse the APIData as a string.
            int numID = 1;
            for (TrendingContent api : searchList) {
                apiToStringList.add("#" + numID++ + ": " + api.getPhrase());
            }
            arrayAdapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_list_item_1, apiToStringList);
            listFormat.setAdapter(arrayAdapter);
            listFormat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Snackbar sb = Snackbar.make(root, apiToStringList.get(i), Snackbar.LENGTH_SHORT);
                    sb.setAction("Article URL", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                            builder.setMessage("Here's your url!");
                            AlertDialog urlLink = builder.create();
                            urlLink.show();
                        }
                    });
                    sb.show();
                }
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}