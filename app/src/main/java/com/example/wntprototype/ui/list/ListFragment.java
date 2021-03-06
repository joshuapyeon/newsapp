package com.example.wntprototype.ui.list;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ExpandableListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.wntprototype.APIWrappers.NewsData;
import com.example.wntprototype.APIWrappers.TrendingContent;
import com.example.wntprototype.DataCache;
import com.example.wntprototype.R;
import com.example.wntprototype.databinding.FragmentListBinding;
import com.example.wntprototype.ui.Shareable;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListFragment extends Fragment implements Shareable {
    /**
     * The cache that holds the data.
     */
    DataCache cache = DataCache.getCache();
    /**
     * The list of trending content that holds the data of the cache
     */
    List<TrendingContent> searchList;
    /**
     * The list of titles for each article
     */
    List<String> titleList = new ArrayList<>();
    /**
     * The list of images for each article (if any)
     */
    List<String> titleImage = new ArrayList<>();
    /**
     * THe fragment list binding
     */
    private FragmentListBinding binding;
    /**
     * The expandable list view
     */
    ExpandableListView listFormat;
    /**
     * The HashMap that maps a phrase to a list of associated articles
     */
    HashMap<String, List<NewsData>> articleMap = new HashMap<String, List<NewsData>>();
    /**
     * The adapter for the listview
     */
    ListViewAdapter newsAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listFormat = root.findViewById(R.id.keywordView);
        if (cache.hasData()) {
            searchList = DataCache.getCache().getData();

            //Get the title word/phrase and put it and it's associated articles in a map.
            for (TrendingContent api : searchList) {
                titleList.add(api.getPhrase()); //Add the title phrase
                titleImage.add(api.getUrlToImage());    //Add the tile image url

                if(api.hasArticles())   //Map the title phrase to the associated sources
                    articleMap.put(api.getPhrase(), api.getSources());
                else    //If not sources available, then map it to an empty list
                    articleMap.put(api.getPhrase(), new ArrayList<NewsData>() {
                    });

            }
            newsAdapter = new ListViewAdapter(titleList, titleImage, articleMap, R.layout.list_row);
            listFormat.setAdapter(newsAdapter);
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public String getSharingType() {
        return "text/plain";
    }

    @Override
    public Object getSharingContent() {
        String list = "";
        for (int i = 0; i < titleList.size(); i++)
            list = list.concat(titleList.get(i) + "\n");
        return list;
    }
}