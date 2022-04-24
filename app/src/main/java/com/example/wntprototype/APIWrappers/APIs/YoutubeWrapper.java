package com.example.wntprototype.APIWrappers.APIs;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.NewsData;
import com.example.wntprototype.APIWrappers.TrendingContent;

import org.json.JSONArray;
import org.json.JSONObject;

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
                .url("https://youtube-trending.p.rapidapi.com/trending?country=US&type=default")
                .get()
                .addHeader("X-RapidAPI-Host", "youtube-trending.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d")
                .build();
        ArrayList<TrendingContent> toReturn = new ArrayList<TrendingContent>();

        try {
            Response response = client.newCall(request).execute();
            if(response.code() != 200) return toReturn;
            JSONArray data = new JSONArray(response.body().string());
            for(int i = 0; i < data.length(); i++){
                TrendingContent temp = new TrendingContent();
                JSONObject video = data.optJSONObject(i);
                temp.setPhrase(video.optString("title"));
                temp.setValue(video.optInt("views"));
                JSONArray thumbnails = video.optJSONArray("thumbnails");
                if(thumbnails.length() > 1) {
                    temp.setUrlToImage(thumbnails.optJSONObject(1).optString("url"));
                }
                List<NewsData> newsList = new ArrayList<NewsData>();
                NewsData news = new NewsData();
                news.title = video.optString("title");
                news.source = video.optString("channelName");
                news.url = video.optString("videoUrl");
                news.urlToImage = temp.getUrlToImage();
                news.snippet = video.optString("description");
                newsList.add(news);

                temp.setSources(newsList);
                toReturn.add(temp);
            }
            return toReturn;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
