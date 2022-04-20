package com.example.wntprototype.APIWrappers.APIs;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.TrendingContent;
import com.example.wntprototype.APIWrappers.NewsData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrendingWrapper extends AsyncTask<APISearch, Void, List<TrendingContent>> {

    private final String api_key = "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d";

    @Override
    protected List<TrendingContent> doInBackground(APISearch... apiSearches) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://google-trend-api.p.rapidapi.com/realTimeTrends?geo=US")
                .get()
                .addHeader("X-RapidAPI-Host", "google-trend-api.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", api_key)
                .build();
        ArrayList<TrendingContent> toReturn = new ArrayList<TrendingContent>();
        try {
            Response response = client.newCall(request).execute();
            if(response.code() != 200) return toReturn;
            JSONObject data = new JSONObject(response.body().string());
            JSONArray values = data.optJSONObject("storySummaries").optJSONArray("trendingStories");
            for (int i = 0; i < values.length(); i++) {
                JSONArray trends = values.optJSONObject(i).optJSONArray("entityNames");
                JSONArray articles = values.optJSONObject(i).optJSONArray("articles");
                String imageUrl = values.optJSONObject(i).optJSONObject("image").optString("imgUrl");
                List<NewsData> newsArticles = new ArrayList<>();
                for(int j = 0; j < articles.length(); j++){
                    JSONObject article = articles.optJSONObject(j);
                    NewsData temp = new NewsData();
                    temp.title = article.optString("articleTitle");
                    temp.source = article.optString("source");
                    temp.url = article.optString("url");
                    temp.snippet = article.optString("snippet");
                    temp.urlToImage = imageUrl;
                    newsArticles.add(temp);
                }
                TrendingContent temp = new TrendingContent();
                temp.setSources(newsArticles);
                temp.setPhrase(values.optJSONObject(i).optString("title"));
                temp.setUrlToImage(imageUrl);
                toReturn.add(temp);
            }
            for(TrendingContent temp: toReturn){
                for(NewsData article: temp.getSources()){
                    temp.setValue(temp.getSources().size());
                }
            }
            return toReturn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
