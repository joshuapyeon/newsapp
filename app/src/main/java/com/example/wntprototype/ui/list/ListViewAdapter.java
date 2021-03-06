package com.example.wntprototype.ui.list;

import java.io.InputStream;
import java.util.*;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import java.net.URL;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wntprototype.R;


import com.example.wntprototype.APIWrappers.NewsData;
import com.example.wntprototype.ui.Shareable;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class ListViewAdapter extends BaseExpandableListAdapter implements Shareable {
    List<String> titleList;
    List<String> titleImage;
    HashMap<String, List<NewsData>> articleMap;
    int articleLayout;
    public ListViewAdapter(List<String> titleList, List<String> titleImage, HashMap<String, List<NewsData>> articleMap, int layout) {
        this.titleList = titleList;
        this.articleMap = articleMap;
        this.articleLayout = layout;
        this.titleImage = titleImage;
    }

    /**
     * Return the # of titles.
     * @return The # of titles from the serach.
     */
    @Override
    public int getGroupCount() {
        return titleList.size() ;
    }

    /**
     * Return the # of children associated with the title at position i
     * @param i The position of the user-selected title
     * @return The # of articles associated with that title
     */
    @Override
    public int getChildrenCount(int i) {
        return articleMap.get(titleList.get(i)).size();
    }

    /**
     * Get the title associated with the position i
     * @param i The position of the title on the ExpandableListView
     * @return The title associated with that position i
     */
    @Override
    public Object getGroup(int i) {
        return titleList.get(i);
    }

    /**
     * Get the info on ONE of the articles at position i1 associated with the title at position i
     * @param i The position of the title
     * @param i1 The position of the child within the expandable
     * @return The article the user selected.
     */
    @Override
    public Object getChild(int i, int i1) {
        return articleMap.get(titleList.get(i)).get(i1);
    }

    @Override
    public long getGroupId(int i) {
        return 0;
    }

    @Override
    public long getChildId(int i, int i1) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.listtitle, viewGroup, false);

        TextView keyphrase = view.findViewById(R.id.titleName);

        String title = titleList.get(i);

        keyphrase.setText(title);

        keyphrase.setTypeface(null, Typeface.BOLD);

        keyphrase.setTextSize(17);

        keyphrase.setTextColor(Color.BLACK);

        ImageView ig = view.findViewById(R.id.image1);
        String image = titleImage.get(i);
        if(!image.equals(""))
            Glide.with(view).load(image).into(ig);

        return view;
    }

    private NewsData findArticle(String title) {
        List<NewsData> toFind = articleMap.get(title);
        for(NewsData nd : toFind) {
            if(nd.url.equals(title))
                return nd;
        }
        return null;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(articleLayout,viewGroup,false);

        TextView articleSpace = view.findViewById(R.id.txtName);

        NewsData selectedArticle = articleMap.get(titleList.get(i)).get(i1);
        String articleTitle = selectedArticle.title;
        articleSpace.setText(articleTitle);
        articleSpace.setTextSize(16);
        articleSpace.setTypeface(null,Typeface.ITALIC);
        articleSpace.setTextColor(Color.rgb(128,137,138));

        articleSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(selectedArticle.hasUrl()) {
                    Toast toast = new Toast(viewGroup.getContext());
                    toast.setText("Opening URL!");
                    toast.show();
                    String URL = selectedArticle.url;
                    Intent openBrowser = new Intent();
                    openBrowser.setAction(Intent.ACTION_VIEW);
                    openBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
                    openBrowser.setData(Uri.parse(URL));
                    viewGroup.getContext().startActivity(openBrowser);
                }
                else {
                    Snackbar sb = Snackbar.make(view, "URL Unavailable", Snackbar.LENGTH_SHORT);
                    sb.show();
                }
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
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
