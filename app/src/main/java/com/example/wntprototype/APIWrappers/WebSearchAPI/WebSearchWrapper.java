package com.example.wntprototype.APIWrappers.WebSearchAPI;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.NewsData;
import com.example.wntprototype.APIWrappers.TrendingContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This gives the top trending stories
 */
public class WebSearchWrapper extends AsyncTask<APISearch, Void, List<TrendingContent>> {

    private final String api_key = "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d";

    @Override
    protected List<TrendingContent> doInBackground(APISearch... apiSearches) {
        OkHttpClient client = new OkHttpClient();

        //Code that builds the date parameter
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -1);  // number of days to add
        String yesterdaysDate = sdf.format(c.getTime());  // dt is now the new date


        Request request = null;
        if(apiSearches[0].hasQuery()) {
            String encodedQuery = URLEncoder.encode(apiSearches[0].getQuery());
            request = new Request.Builder()
                    .url("https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/search/NewsSearchAPI?q=" + encodedQuery + "&pageNumber=1&pageSize=50&autoCorrect=true&fromPublishedDate=" + yesterdaysDate + "&toPublishedDate=null")
                    .get()
                    .addHeader("X-RapidAPI-Host", "contextualwebsearch-websearch-v1.p.rapidapi.com")
                    .addHeader("X-RapidAPI-Key", api_key)
                    .build();
        }else {
            request = new Request.Builder()
                    .url("https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/search/TrendingNewsAPI?pageNumber=1&pageSize=50&withThumbnails=false&location=us")
                    .get()
                    .addHeader("x-rapidapi-host", "contextualwebsearch-websearch-v1.p.rapidapi.com")
                    .addHeader("x-rapidapi-key", api_key)
                    .build();
        }

        List<TrendingContent> toReturn = new ArrayList<TrendingContent>();
        try {
            Response response = client.newCall(request).execute();
            JSONObject data = new JSONObject(response.body().string());
            JSONArray values = data.optJSONArray("value");
            for(int i = 0; i < values.length(); i++){
                NewsData temp = new NewsData();
                TrendingContent content = new TrendingContent();
                JSONObject article = values.optJSONObject(i);
                temp.title = article.optString("title");
                temp.snippet = article.optString("snippet");
                temp.url = article.optString("url");
                temp.source = article.optJSONObject("provider").optString("name");
                temp.urlToImage = article.optJSONObject("image").optString("url");
                temp.setToParse();
                content.setArticles(new ArrayList<NewsData>());
                content.getArticles().add(temp);
                content.setValue(1);
                content.setPhrase(temp.title);
                toReturn.add(content);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
