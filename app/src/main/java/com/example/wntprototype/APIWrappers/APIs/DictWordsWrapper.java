package com.example.wntprototype.APIWrappers.APIs;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.NewsData;
import com.example.wntprototype.APIWrappers.TrendingContent;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DictWordsWrapper extends AsyncTask<APISearch, Void, List<TrendingContent>> {
    @Override
    protected List<TrendingContent> doInBackground(APISearch... apiSearches) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://trending-words-and-dictionary.p.rapidapi.com/trending")
                .get()
                .addHeader("X-RapidAPI-Host", "trending-words-and-dictionary.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d")
                .build();
        ArrayList<TrendingContent> toReturn = new ArrayList<TrendingContent>();
        try {
            Response response = client.newCall(request).execute();
            if(response.code() != 200) return toReturn;
            JSONArray words = new JSONArray(response.body().string());
            for(int i = 0; i < words.length(); i++){
                TrendingContent temp = new TrendingContent();
                temp.setPhrase(words.optString(i));
                temp.setValue(words.length()-i);
                temp.setSources(new ArrayList<NewsData>());
                toReturn.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
