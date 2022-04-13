package com.example.wntprototype.ui.list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

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
import java.util.List;

public class ListFragment extends Fragment implements Shareable {
    DataCache cache = DataCache.getCache();
    List<TrendingContent> searchList;
    List<String> apiToStringList = new ArrayList<>();
    ArrayList<NewsData> newsDataList = new ArrayList<NewsData>();
    private FragmentListBinding binding;
    ListView listFormat;
    ArrayAdapter<String> arrayAdapter;
    NewsAdapter newsAdapter;

    /**
     * Helper method. Retrieves all available NewsData objects from a TrendingContent object
     * PRECONDITION: There is at least 1 NewsData objects in NDList
     * @param NDList The list of NewsData objects in a TrendingContent object
     */
    public void getNewsData(List<NewsData> NDList) {
        for(int i = 0; i < NDList.size(); i++) {
            newsDataList.add(NDList.get(i));
        }
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listFormat = root.findViewById(R.id.listView);
        if (cache.hasData()) {
            searchList = DataCache.getCache().getData();


            for (TrendingContent api : searchList) {
                apiToStringList.add(api.getPhrase());
                getNewsData(api.getArticles());
            }
            newsAdapter = new NewsAdapter(root.getContext(), R.layout.list_row, newsDataList);
            listFormat.setAdapter(newsAdapter);
            listFormat.setOnItemClickListener((adapterView, view, i , l) -> {
                Snackbar sb = Snackbar.make(root, newsDataList.get(i).title, Snackbar.LENGTH_SHORT);
                sb.setAction("OPEN ARTICLE", view1 -> {
                    if(searchList.get(i).getArticles() != null && searchList.get(i).getArticles().get(0).hasUrl()) {
                        String URL = searchList.get(i).getArticles().get(0).url;
                        Log.d("URL being parsed", URL);
                        Intent openBrowser = new Intent();
                        openBrowser.setAction(Intent.ACTION_VIEW);
                        openBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
                        openBrowser.setData(Uri.parse(URL));
                        startActivity(openBrowser);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                        builder.setMessage("URL Unavailable");
                        AlertDialog urlLink = builder.create();
                        urlLink.show();
                    }
                });
                sb.show();
            });
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
        for (int i = 0; i < apiToStringList.size(); i++)
            list = list.concat(apiToStringList.get(i) + "\n");
        return list;
    }
}