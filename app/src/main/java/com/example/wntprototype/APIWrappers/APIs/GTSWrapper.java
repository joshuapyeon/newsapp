package com.example.wntprototype.APIWrappers.APIs;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.TrendingContent;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Google Trending Searches (GTS) API.
 *
 * This class gets the latest trending searches on Google related to the query.
 */
public class GTSWrapper extends AsyncTask<APISearch, Void, List<TrendingContent>> {

    /**
     * The API key.
     */
    private final String api_key = "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d";

    @Override
    /**
     * Builds and sends the data in a separate thread.
     */
    protected List<TrendingContent> doInBackground(APISearch... apiSearches) {
        return buildDataList(getResponseText(apiSearches[0]));
    }

    /**
     * A String parsing function that gets all of the searched data.
     * (The API content is not a specific file type, so I had to parse it.  I just used the base
     * file pattern to consistently build this structure)
     * @param text The API content to parse.
     * @return A list of the Google Trending Searches API data.
     */
    private List<TrendingContent> buildDataList(String text) {
        List<TrendingContent> toReturn = new ArrayList<TrendingContent>();
        if(text.equals("")) return toReturn;
        try {
            JSONObject data = new JSONObject(text);
            JSONArray results = data.optJSONArray("results");
            if(results == null) return null;
            for(int i = 0; i < results.length(); i++){
                TrendingContent temp = new TrendingContent();
                temp.setPhrase(results.optString(i));
                toReturn.add(temp);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < toReturn.size(); i++){
            toReturn.get(i).setValue(toReturn.size()-i);
        }
        return toReturn;
    }

    /**
     * This function builds tha API Call and gets the content.
     * @param search The search parameters.
     * @return A String of the API data.
     */
    private String getResponseText(APISearch search){
        OkHttpClient client = new OkHttpClient();
        String encodedQuery = URLEncoder.encode(search.getQuery());
        Request request = new Request.Builder()
                .url("https://google-trends-related-search.p.rapidapi.com/?keyword=" + encodedQuery + "&timerange=last1day&geo=US")
                .get()
                .addHeader("x-rapidapi-host", "google-trends-related-search.p.rapidapi.com")
                .addHeader("x-rapidapi-key", api_key)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.code() != 200) return "";
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
