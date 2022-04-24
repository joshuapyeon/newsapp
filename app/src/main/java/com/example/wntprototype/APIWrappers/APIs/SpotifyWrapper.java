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

public class SpotifyWrapper extends AsyncTask<APISearch, Void, List<TrendingContent>> {
    @Override
    protected List<TrendingContent> doInBackground(APISearch... apiSearches) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://spotify23.p.rapidapi.com/charts/?type=viral&country=us&recurrence=daily&date=latest")
                .get()
                .addHeader("X-RapidAPI-Host", "spotify23.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d")
                .build();
        ArrayList<TrendingContent> toReturn = new ArrayList<TrendingContent>();
        try {
            Response response = client.newCall(request).execute();
            if(response.code() != 200) return toReturn;
            JSONObject base = new JSONObject(response.body().string());
            JSONArray content = base.optJSONArray("content");
            for(int i = 0; i < content.length(); i++){
                TrendingContent temp = new TrendingContent();
                JSONObject song = content.optJSONObject(i);
                temp.setUrlToImage(song.optString("thumbnail"));
                temp.setPhrase(fixTitle(song.optString("track_title")));
                temp.setValue(content.length()-i);
                NewsData news = new NewsData();
                news.title = temp.getPhrase();
                news.url = song.optString("track_url");
                JSONArray artists = song.optJSONArray("artists");
                List<NewsData> newsArticles = new ArrayList<>();
                for(int j = 0; j < artists.length(); j++){
                    news.source += artists.optString(i) + (j == artists.length()-1?"":", ");
                }
                newsArticles.add(news);
                temp.setSources(newsArticles);
                toReturn.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }

    private String fixTitle(String s){
        String toReturn = s;
        toReturn = toReturn.replaceAll("&#039;", "'");
        toReturn = toReturn.replaceAll("&aacute;", "a");
        toReturn = toReturn.replaceAll("&eacute;", "e");
        toReturn = toReturn.replaceAll("&iacute;", "i");
        toReturn = toReturn.replaceAll("&oacute;", "o");
        toReturn = toReturn.replaceAll("&uacute;", "u");
        toReturn = toReturn.replaceAll("&reg;", "");
        toReturn = toReturn.replaceAll("&Aacute;", "A");
        toReturn = toReturn.replaceAll("&Eacute;", "E");
        toReturn = toReturn.replaceAll("&Iacute;", "I");
        toReturn = toReturn.replaceAll("&Oacute;", "O");
        toReturn = toReturn.replaceAll("&Uacute;", "U");
        return toReturn;
    }
}
