package com.example.wntprototype.ui.list;

import java.util.*;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.wntprototype.R;


import com.example.wntprototype.APIWrappers.NewsData;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class ListViewAdapter extends BaseExpandableListAdapter {
    List<String> titleList;
    HashMap<String, List<NewsData>> articleMap;
    int articleLayout;
    public ListViewAdapter(List<String> titleList, HashMap<String, List<NewsData>> articleMap, int layout) {
        this.titleList = titleList;
        this.articleMap = articleMap;
        this.articleLayout = layout;
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
        view = LayoutInflater.from(viewGroup.getContext()).inflate(android.R.layout.simple_expandable_list_item_1, viewGroup, false);

        TextView keyphrase = view.findViewById(android.R.id.text1);

        String title = String.valueOf(i);

        keyphrase.setText(title);

        keyphrase.setTypeface(null, Typeface.BOLD_ITALIC);

        keyphrase.setTextColor(Color.BLUE);
        return view ;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(viewGroup.getContext()).inflate(articleLayout,viewGroup,false);

        TextView articleSpace = view.findViewById(R.id.txtName);

        ImageView imageView = view.findViewById(R.id.image);

        NewsData selectedArticle = articleMap.get(titleList.get(i)).get(i1);
        String articleTitle = selectedArticle.title;
        articleSpace.setText(articleTitle);

        String articleImage = selectedArticle.urlToImage;

        articleSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar sb = Snackbar.make(view, selectedArticle.title, Snackbar.LENGTH_SHORT);
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }
}
