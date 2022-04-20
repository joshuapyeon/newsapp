package com.example.wntprototype.APIWrappers.NewsAPI;

import android.os.AsyncTask;

import com.example.wntprototype.APIWrappers.APISearch;
import com.example.wntprototype.APIWrappers.TrendingContent;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * THIS CLASS IS NOT FUNCTIONAL, IT WILL RETURN NULL!!!!
 *
 * This class gets data from NewsAPI and puts it into a helpful data structure.
 */
public class NewsAPIWrapper extends AsyncTask<APISearch, Void, List<TrendingContent>> {

    /**
     * The API Key.
     */
    private final String api_key = "f0ff0e93fe38440b8bd676c8b3fe962c";


    @Override
    /**
     * This method gets the API Data and formats it correctly and sends it back to the main thread.
     */
    protected List<TrendingContent> doInBackground(APISearch... apiSearches) {
        URL url = null;
        HttpURLConnection urlConnection = null;
        try{
            url = buildURL(apiSearches[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            int status = urlConnection.getResponseCode();
            String response = StreamToString(urlConnection.getInputStream());
            return buildArticleList(response);
        }catch(MalformedURLException e){
            e.getStackTrace();
        }catch(Exception e){
            e.getStackTrace();
        }
        return null;
    }

    /**
     * This function builds the data structure of ArticleData, and converts it to list of
     * APIData.
     * @param response The String of the JSON file retrieved from the API data pull
     * @return A list of APIData with news information
     */
    private ArrayList<TrendingContent> buildArticleList(String response) {
        JSONObject temp = null;
        try{
            temp = new JSONObject(response);
            ArrayList<TrendingContent> toReturn = new ArrayList<TrendingContent>();
            JSONArray articles = temp.optJSONArray("articles");
            for(int i = 0; i < articles.length(); i++){
                JSONObject article = articles.optJSONObject(i);
                NewsAPIData ad = new NewsAPIData();
                ad.author = article.optString("author");
                ad.title = article.optString("title");
                ad.sourceName = article.optJSONObject("source").optString("name");
                ad.description = article.optString("description");
                ad.url = article.optString("url");
                ad.urlToImage = article.optString("urlToImage");
                ad.publishDate = article.optString("publishedAt");
                ad.content = article.optString("content");
            }
            return toReturn;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     * This method builds the URL for the API data pull
     * @param search The Search parameters.
     * @return The correctly formatted URL.
     * @throws MalformedURLException Throws if there is an exception in the url construction.
     */
    private URL buildURL(APISearch search) throws MalformedURLException {
        String tempUrl = "https://newsapi.org/v2/top-headlines?";
        String andSymbol = "";
        if(search.hasQuery()){
            tempUrl += andSymbol + "q=" + search.getQuery();
            andSymbol = "&";
        }
//        if(search.hasCountry){
//            tempUrl += andSymbol + "country=" + search.country.toString();
//            andSymbol = "&";
//        }
//        if(search.hasSource){
//            tempUrl += andSymbol + "sources=" + search.source;
//        }
//        if(search.hasCategory){
//            tempUrl += andSymbol + "category=" + search.category.toString();
//            andSymbol = "&";
//        }
//        if(search.hasDomain){
//            tempUrl += andSymbol + "domains=" + search.domain;
//            andSymbol = "&";
//        }
//        if(search.hasLanguage){
//            tempUrl += andSymbol + "language=" + search.language;
//            andSymbol = "&";
//        }
        tempUrl += andSymbol +  "apiKey=" + api_key;

        return new URL(tempUrl);
    }

    /**
     * Converts the URL connection Stream into a JSON string to be formatted.
     * @param stream The Stream of Data from the API data pull.
     * @return A String of all the data.
     * @throws IOException If there is an error with the BufferedReader construction.
     */
    private String StreamToString(InputStream stream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));
        String data;
        String result = "";
        while((data = bufferedReader.readLine()) != null){
            result += data;
        }
        if (null != stream){
            stream.close();
        }
        return result;
    }
}
