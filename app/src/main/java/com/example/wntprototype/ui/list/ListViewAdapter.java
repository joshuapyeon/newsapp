package com.example.wntprototype.ui.list;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import java.net.URL;
import android.net.Uri;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.wntprototype.R;


import com.example.wntprototype.APIWrappers.NewsData;
import com.example.wntprototype.ui.Shareable;
import com.google.android.material.snackbar.Snackbar;

import java.util.HashMap;

public class ListViewAdapter extends BaseExpandableListAdapter implements Shareable {
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

        String title = titleList.get(i);

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
        Drawable image = urlToDrawable(selectedArticle);
        imageView.setImageDrawable(image);

        articleSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar sb = Snackbar.make(view, selectedArticle.title, Snackbar.LENGTH_SHORT);
                sb.setAction("OPEN LINK", view1 -> {
                    if(selectedArticle.hasUrl()) {
                        String URL = selectedArticle.url;
                        Intent openBrowser = new Intent();
                        openBrowser.setAction(Intent.ACTION_VIEW);
                        openBrowser.addCategory(Intent.CATEGORY_BROWSABLE);
                        openBrowser.setData(Uri.parse(URL));
                        viewGroup.getContext().startActivity(openBrowser);
                    }
                    else {
                        AlertDialog.Builder builder = new AlertDialog.Builder(viewGroup.getContext());
                        builder.setMessage("URL Unavailable");
                        AlertDialog urlLink = builder.create();
                        urlLink.show();
                    }
                });
                sb.show();
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    /**
     * Helper method. Supposed to turn an imageURL String to a Drawable. DOES NOT WORK
     * @param news The NewsData item containing the imageURL
     * @return A Drawable of the imageURL
     */
    private Drawable urlToDrawable(NewsData news) {
        if(news.hasUrlToImage()) {
            String image = news.urlToImage;
            try {
                InputStream is = (InputStream) new URL(image).getContent();
                Drawable d = Drawable.createFromStream(is, "some source");
                return d;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
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
