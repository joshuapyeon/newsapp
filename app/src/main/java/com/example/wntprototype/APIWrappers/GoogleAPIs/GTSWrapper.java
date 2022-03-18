package com.example.wntprototype.APIWrappers.GoogleAPIs;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APIData;
import com.example.wntprototype.APIWrappers.APISearch;


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
public class GTSWrapper extends AsyncTask<APISearch, Void, List<APIData>> {

    /**
     * The API key.
     */
    private final String api_key = "0befbe4810mshf7b6de507ca3575p1eadedjsn2080a980ab3d";

    @Override
    /**
     * Builds and sends the data in a separate thread.
     */
    protected List<APIData> doInBackground(APISearch... apiSearches) {
        return buildDataList(getResponseText(apiSearches[0]));
    }

    /**
     * A String parsing function that gets all of the searched data.
     * (The API content is not a specific file type, so I had to parse it.  I just used the base
     * file pattern to consistently build this structure)
     * @param text The API content to parse.
     * @return A list of the Google Trending Searches API data.
     */
    private List<APIData> buildDataList(String text) {
        List<APIData> toReturn = new ArrayList<APIData>();
        int semicolonsLeft = 4;
        int index = 0;
        while (semicolonsLeft > 0){
            if(text.charAt(index) == ':'){
                semicolonsLeft--;
                index++;
            }
            index++;
        }
        while(index < text.length()){
            if (text.charAt(index) == '"'){
                GTSData temp = new GTSData();
                temp.content = "";
                Boolean flag = true;
                index++;
                while(flag){
                    if(text.charAt(index) == '"'){
                        flag = false;
                    }else {
                        temp.content += text.charAt(index);
                        index++;
                    }
                }
                temp.setToParse();
                toReturn.add(temp);
            }
            index++;
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
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
