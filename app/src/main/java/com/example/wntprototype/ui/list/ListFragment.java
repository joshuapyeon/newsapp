package com.example.wntprototype.ui.list;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListFragment extends Fragment {
    DataCache cache = DataCache.getCache();
    List<TrendingContent> searchList;
    List<String> apiToStringList = new ArrayList<>();
    List<NewsData> newsDataList = new ArrayList<NewsData>();
    private FragmentListBinding binding;
    ListView listFormat;
    ArrayAdapter<String> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        listFormat = root.findViewById(R.id.listFormat);
        if (cache.hasData()) {
            searchList = DataCache.getCache().getData();
            int numID = 1;
            for (TrendingContent api : searchList)
                apiToStringList.add("#" + numID++ + ": " + api.getPhrase());
            arrayAdapter = new ArrayAdapter<>(root.getContext(), android.R.layout.simple_list_item_1, apiToStringList);
            listFormat.setAdapter(arrayAdapter);
            listFormat.setOnItemClickListener((adapterView, view, i, l) -> {
                Log.d("Number: ", i+"");
                Snackbar sb = Snackbar.make(root, apiToStringList.get(i), Snackbar.LENGTH_SHORT);
                sb.setAction("OPEN ARTICLE", view1 -> {
                    NewsData nd = null;
                    if(searchList.get(i).getArticles() != null)
                        nd = searchList.get(i).getArticles().get(0);
                    AlertDialog.Builder builder = new AlertDialog.Builder(root.getContext());
                    if(nd != null) {
                        if (nd.hasUrl()) {
                            String url = nd.url;
                            builder.setMessage(url);
                            builder.setPositiveButton("See your Article", new DialogInterface.OnClickListener() {
                                @Override
                                /*
                                 * Opens the url if there is one to go to.
                                 */
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent openBrowser = new Intent();
                                    openBrowser.setAction(Intent.ACTION_VIEW);
                                    openBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
                                    openBrowser.setData(Uri.parse(url));
                                    startActivity(openBrowser);
                                }
                            });
                        }
                        else
                            builder.setMessage("URL Unavailable");
                    }
                    else builder.setMessage("URL Unavailable");
                    AlertDialog urlLink = builder.create();
                    urlLink.show();
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
}