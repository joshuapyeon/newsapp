package com.example.wntprototype.ui.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.DataCache;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentListBinding;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    DataCache cache = DataCache.getCache();
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

        if (cache.hasData()) {
            searchList = DataCache.getCache().getData();

            //TODO: Parse the APIData as a string.
            int numID = 0;
            for (APIData api : searchList) {
                apiToStringList.add("#" + numID++ + ": " + api.getToParse());
            }
            arrayAdapter = new ArrayAdapter(root.getContext(), android.R.layout.simple_list_item_1, apiToStringList);
            listFormat.setAdapter(arrayAdapter);
        }
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}