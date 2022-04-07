package com.example.wntprototype.APIWrappers.GoogleAPIs;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.NewsData;
import com.example.wntprototype.APIWrappers.TrendingContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class YoutubeWrapper extends AsyncTask<APISearch, Void, List<TrendingContent>> {
    @Override
    protected List<TrendingContent> doInBackground(APISearch... apiSearches) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://youtube-search-and-download.p.rapidapi.com/trending?hl=en&gl=US")
                .get()
                .addHeader("X-RapidAPI-Host", "youtube-search-and-download.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d")
                .build();
        ArrayList<TrendingContent> toReturn = new ArrayList<TrendingContent>();
        try {
            Response response = client.newCall(request).execute();
            JSONObject data = new JSONObject(response.body().string());
            JSONArray dataList = data.getJSONArray("contents");
            for(int i = 0; i < dataList.length(); i++){
                JSONObject video = dataList.optJSONObject(i).optJSONObject("video");
                TrendingContent temp = new TrendingContent();
                temp.setPhrase(video.optString("title"));
                JSONArray images = video.getJSONArray("thumbnails");
                String value = "";
                String valueString = video.optString("viewCountText");
                for(int w = 0; w < valueString.length(); w++){
                    char val = valueString.charAt(w);
                    if(val >= 48 && val <=57) value = val + value;
                }
                temp.setValue(Integer.parseInt(value));
                List<NewsData> newsList = new ArrayList<NewsData>();
                for(int j = 0; j < images.length(); j++){
                    NewsData news = new NewsData();
                    news.source = video.optString("channelName");
                    news.urlToImage = images.optJSONObject(j).optString("url");
                    newsList.add(news);
                }
                temp.setArticles(newsList);
                toReturn.add(temp);
            }
            return toReturn;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
