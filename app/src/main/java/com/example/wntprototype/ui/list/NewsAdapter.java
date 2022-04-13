package com.example.wntprototype.ui.list;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.renderscript.ScriptGroup;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.wntprototype.APIWrappers.NewsData;
import com.example.wntprototype.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<NewsData> {
    private Context mContext;
    private int mResource;

    ImageView iv;
    TextView tv;
    public NewsAdapter(@NonNull Context context, int resource, @NonNull ArrayList<NewsData> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    /**
     * Helper method. Supposed to turn an imageURL String to a Drawable. DOES NOT WORK
     * @param news The NewsData item containing the imageURL
     * @return A Drawable of the imageURL
     */
    private Drawable urlToInt(NewsData news) {
        if(news.hasUrlToImage()) {
            String image = news.urlToImage;
            Drawable d = Drawable.createFromPath(image);
            return d;
        }
        return null;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        convertView = layoutInflater.inflate(mResource, parent, false);

        iv = convertView.findViewById(R.id.image);
        tv = convertView.findViewById(R.id.txtName);
        Drawable parsedImage = urlToInt(getItem(position));
        tv.setText(getItem(position).title);
        if(parsedImage != null) iv.setImageDrawable(parsedImage);
        else Log.d("parsedImage", "IS NULL!");
        return convertView;
    }


}
