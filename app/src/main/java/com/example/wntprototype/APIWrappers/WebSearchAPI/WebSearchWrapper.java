package com.example.wntprototype.APIWrappers.WebSearchAPI;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.APIWrappers.APISearch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * This gives the top trending stories
 */
public class WebSearchWrapper extends AsyncTask<APISearch, Void, List<APIData>> {
    @Override
    protected List<APIData> doInBackground(APISearch... apiSearches) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://contextualwebsearch-websearch-v1.p.rapidapi.com/api/search/TrendingNewsAPI?pageNumber=1&pageSize=10&withThumbnails=false&location=us")
                .get()
                .addHeader("x-rapidapi-host", "contextualwebsearch-websearch-v1.p.rapidapi.com")
                .addHeader("x-rapidapi-key", "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d")
                .build();

        List<APIData> toReturn = new ArrayList<APIData>();
        try {
            Response response = client.newCall(request).execute();
            JSONObject data = new JSONObject(response.body().string());
            JSONArray values = data.optJSONArray("value");
            for(int i = 0; i < values.length(); i++){
                WebSearchData temp = new WebSearchData();
                JSONObject article = values.optJSONObject(i);
                temp.title = article.optString("title");
                temp.body = article.optString("body");
                temp.description = article.optString("description");
                temp.snippet = article.optString("snippet");
                temp.url = article.optString("url");
                temp.provider = article.optJSONObject("provider").optString("name");
                temp.setToParse();
                toReturn.add(temp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toReturn;
    }
}
