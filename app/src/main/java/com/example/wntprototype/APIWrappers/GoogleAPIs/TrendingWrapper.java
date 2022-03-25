package com.example.wntprototype.APIWrappers.GoogleAPIs;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.APIWrappers.APISearch;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class TrendingWrapper extends AsyncTask<APISearch, Void, List<APIData>> {

    private final String api_key = "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d";

    @Override
    protected List<APIData> doInBackground(APISearch... apiSearches) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://google-trend-api.p.rapidapi.com/realTimeTrends?geo=US")
                .get()
                .addHeader("X-RapidAPI-Host", "google-trend-api.p.rapidapi.com")
                .addHeader("X-RapidAPI-Key", api_key)
                .build();
        ArrayList<APIData> toReturn = new ArrayList<APIData>();
        try {
            Response response = client.newCall(request).execute();
            JSONObject data = new JSONObject(response.body().string());
            JSONArray values = data.optJSONObject("storySummaries").optJSONArray("trendingStories");
            for (int i = 0; i < values.length(); i++) {
                JSONArray trends = values.optJSONObject(i).optJSONArray("entityNames");
                for (int j = 0; j < trends.length(); j++) {
                    TrendingData temp = new TrendingData();
                    temp.title = trends.optString(j);
                    temp.setToParse();
                    toReturn.add(temp);
                }
            }
            return toReturn;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
