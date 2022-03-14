package com.example.wntprototype.APIWrappers;

import android.os.AsyncTask;

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

public class NewsAPIWrapper extends AsyncTask<APISearch, Void, List<ArticleData>> {

    public NewsAPIWrapper(){
    }

    @Override
    protected List<ArticleData> doInBackground(APISearch... apiSearches) {
        return GetNews(apiSearches[0]);
    }

    private List<ArticleData> GetNews(APISearch search){
        URL url = null;
        HttpURLConnection urlConnection = null;
        try{
            url = buildURL(search);
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

    private ArrayList<ArticleData> buildArticleList(String response) {
        JSONObject temp = null;
        try{
            temp = new JSONObject(response);
            ArrayList<ArticleData> toReturn = new ArrayList<ArticleData>();
            JSONArray articles = temp.optJSONArray("articles");
            for(int i = 0; i < articles.length(); i++){
                JSONObject article = articles.optJSONObject(i);
                ArticleData ad = new ArticleData();
                ad.author = article.optString("author");
                ad.title = article.optString("title");
                ad.sourceName = article.optJSONObject("source").optString("name");
                ad.description = article.optString("description");
                ad.url = article.optString("url");
                ad.urlToImage = article.optString("urlToImage");
                ad.publishDate = article.optString("publishedAt");
                ad.content = article.optString("content");
                toReturn.add(ad);
            }
            return toReturn;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private URL buildURL(APISearch search) throws MalformedURLException {
        String tempUrl = "https://newsapi.org/v2/top-headlines?";
        String andSymbol = "";
        if(search.hasQuery){
            tempUrl += andSymbol + "q=" + search.query;
            andSymbol = "&";
        }
        if(search.hasCountry){
            tempUrl += andSymbol + "country=" + search.country.toString();
            andSymbol = "&";
        }
//        if(search.hasSource){
//            tempUrl += andSymbol + "sources=" + search.source;
//        }
        if(search.hasCategory){
            tempUrl += andSymbol + "category=" + search.category.toString();
            andSymbol = "&";
        }
//        if(search.hasDomain){
//            tempUrl += andSymbol + "domains=" + search.domain;
//            andSymbol = "&";
//        }
//        if(search.hasLanguage){
//            tempUrl += andSymbol + "language=" + search.language;
//            andSymbol = "&";
//        }
//        tempUrl += andSymbol + "apiKey=f0ff0e93fe38440b8bd676c8b3fe962c";
        tempUrl += andSymbol +  "apiKey=f0ff0e93fe38440b8bd676c8b3fe962c";

        return new URL(tempUrl);
    }

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
